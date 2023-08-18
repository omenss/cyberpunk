package com.cyberpunk.netty.config;

import com.cyberpunk.netty.annotation.EnableWebSocket;
import com.cyberpunk.netty.annotation.ServerEndpoint;
import com.cyberpunk.netty.exceptions.DeploymentException;
import com.cyberpunk.netty.server.MethodMapping;
import com.cyberpunk.netty.server.SimpleEndpointServer;
import com.cyberpunk.netty.server.WebsocketServer;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * @author lujun
 * @date 2023/8/18 13:41
 */
public class ServerEndpointRegistry extends ApplicationObjectSupport implements SmartInitializingSingleton, BeanFactoryAware, ResourceLoaderAware {

    @Autowired
    Environment environment;

    private AbstractBeanFactory beanFactory;

    private ResourceLoader resourceLoader;

    /**
     * 存储各个socketServer
     * key为 InetSocketAddress value为 WebsocketServer
     * 在启动的时候进行初始化
     * 网络请求调用的时候 从这里查询server 来进行调度
     */
    private final Map<InetSocketAddress, WebsocketServer> websocketServerMap = new HashMap<>(256);


    /**
     * 自定义实现当前Bean 的beanFactory
     *
     * @param beanFactory bean工厂
     * @throws BeansException bean异常
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof AbstractBeanFactory)) {
            throw new IllegalArgumentException("AutowiredAnnotationBeanPostProcessor requires a AbstractBeanFactory: " + beanFactory);
        }
        this.beanFactory = (AbstractBeanFactory) beanFactory;
    }

    /**
     * 在bean初始化完成后 进行的操作
     */
    @Override
    public void afterSingletonsInstantiated() {
        //注册各个endPoints 类似 springmvc 的handlerMapping
        registerEndpoints();
    }

    /**
     * 读取外部资源的方法
     *
     * @param resourceLoader 资源加载器
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    protected void registerEndpoints() {
        ApplicationContext context = getApplicationContext();
        if (context == null) {
            throw new IllegalStateException("ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
        }
        //扫描包
        scanPackage(context);
        //扫描类 查询所有带了ServerEndpoint的注解 将这些bean注册到spring容器中
        String[] endpointBeanNames = context.getBeanNamesForAnnotation(ServerEndpoint.class);
        Set<Class<?>> endpointClasses = new LinkedHashSet<>();
        for (String beanName : endpointBeanNames) {
            endpointClasses.add(context.getType(beanName));
        }
        for (Class<?> endpointClass : endpointClasses) {
            if (AopUtils.isCglibProxy(endpointClass)) {
                registerEndpoint(endpointClass.getSuperclass());
            } else {
                registerEndpoint(endpointClass);
            }
        }
        //启动server服务
        init();
    }

    /**
     * 启动server服务 一个webSocketServer 一个端口
     */
    private void init() {
        for (Map.Entry<InetSocketAddress, WebsocketServer> entry : websocketServerMap.entrySet()) {
            WebsocketServer websocketServer = entry.getValue();
            try {
                websocketServer.init();
                SimpleEndpointServer simpleEndpointServer = websocketServer.getSimpleEndpointServer();
                StringJoiner stringJoiner = new StringJoiner(",");
                simpleEndpointServer.getPathMatcherSet().forEach(pathMatcher -> stringJoiner.add("'" + pathMatcher.getPattern() + "'"));
                logger.info(String.format("\033[34mNetty WebSocket started on port: %s with context path(s): %s .\033[0m", simpleEndpointServer.getPort(), stringJoiner));
            } catch (InterruptedException e) {
                logger.error(String.format("websocket [%s] init fail", entry.getKey()), e);
            } catch (SSLException e) {
                logger.error(String.format("websocket [%s] ssl create fail", entry.getKey()), e);
            }
        }
    }


    /**
     * 把从spring 容器中 注入的bean 存放到 websocketServerMap
     *
     * @param endpointClass bean Class
     */
    private void registerEndpoint(Class<?> endpointClass) {
        ServerEndpoint annotation = AnnotatedElementUtils.findMergedAnnotation(endpointClass, ServerEndpoint.class);
        if (annotation == null) {
            throw new IllegalStateException("Missing @ServerEndpoint annotation for endpoint class " + endpointClass.getName());
        }
        ServerEndpointConfig serverEndpointConfig = buildConfig(annotation);
        ApplicationContext context = getApplicationContext();
        MethodMapping methodMapping;
        try {
            methodMapping = new MethodMapping(endpointClass, context, beanFactory);
        } catch (DeploymentException e) {
            throw new IllegalStateException("Failed to register ServerEndpointConfig: " + serverEndpointConfig, e);
        }
        InetSocketAddress inetSocketAddress = new InetSocketAddress(serverEndpointConfig.getHost(), serverEndpointConfig.getPort());
        //获取到path
        String path = resolveAnnotationValue(annotation.value(), String.class, "path");
        WebsocketServer websocketServer = websocketServerMap.get(inetSocketAddress);
        if (websocketServer == null) {
            SimpleEndpointServer simpleEndpointServer = new SimpleEndpointServer(methodMapping, serverEndpointConfig, path);
            websocketServer = new WebsocketServer(simpleEndpointServer, serverEndpointConfig);
            websocketServerMap.put(inetSocketAddress, websocketServer);
        } else {
            websocketServer.getSimpleEndpointServer().addPathPojoMethodMapping(path, methodMapping);
        }
    }

    private ServerEndpointConfig buildConfig(ServerEndpoint annotation) {
        String host = resolveAnnotationValue(annotation.host(), String.class, "host");
        int port = resolveAnnotationValue(annotation.port(), Integer.class, "port");
        String path = resolveAnnotationValue(annotation.value(), String.class, "value");
        int bossLoopGroupThreads = resolveAnnotationValue(annotation.bossLoopGroupThreads(), Integer.class, "bossLoopGroupThreads");
        int workerLoopGroupThreads = resolveAnnotationValue(annotation.workerLoopGroupThreads(), Integer.class, "workerLoopGroupThreads");
        boolean useCompressionHandler = resolveAnnotationValue(annotation.useCompressionHandler(), Boolean.class, "useCompressionHandler");

        int optionConnectTimeoutMillis = resolveAnnotationValue(annotation.optionConnectTimeoutMillis(), Integer.class, "optionConnectTimeoutMillis");
        int optionSoBacklog = resolveAnnotationValue(annotation.optionSoBacklog(), Integer.class, "optionSoBacklog");

        int childOptionWriteSpinCount = resolveAnnotationValue(annotation.childOptionWriteSpinCount(), Integer.class, "childOptionWriteSpinCount");
        int childOptionWriteBufferHighWaterMark = resolveAnnotationValue(annotation.childOptionWriteBufferHighWaterMark(), Integer.class, "childOptionWriteBufferHighWaterMark");
        int childOptionWriteBufferLowWaterMark = resolveAnnotationValue(annotation.childOptionWriteBufferLowWaterMark(), Integer.class, "childOptionWriteBufferLowWaterMark");
        int childOptionSoRcvbuf = resolveAnnotationValue(annotation.childOptionSoRcvbuf(), Integer.class, "childOptionSoRcvbuf");
        int childOptionSoSndbuf = resolveAnnotationValue(annotation.childOptionSoSndbuf(), Integer.class, "childOptionSoSndbuf");
        boolean childOptionTcpNodelay = resolveAnnotationValue(annotation.childOptionTcpNodelay(), Boolean.class, "childOptionTcpNodelay");
        boolean childOptionSoKeepalive = resolveAnnotationValue(annotation.childOptionSoKeepalive(), Boolean.class, "childOptionSoKeepalive");
        int childOptionSoLinger = resolveAnnotationValue(annotation.childOptionSoLinger(), Integer.class, "childOptionSoLinger");
        boolean childOptionAllowHalfClosure = resolveAnnotationValue(annotation.childOptionAllowHalfClosure(), Boolean.class, "childOptionAllowHalfClosure");

        int readerIdleTimeSeconds = resolveAnnotationValue(annotation.readerIdleTimeSeconds(), Integer.class, "readerIdleTimeSeconds");
        int writerIdleTimeSeconds = resolveAnnotationValue(annotation.writerIdleTimeSeconds(), Integer.class, "writerIdleTimeSeconds");
        int allIdleTimeSeconds = resolveAnnotationValue(annotation.allIdleTimeSeconds(), Integer.class, "allIdleTimeSeconds");

        int maxFramePayloadLength = resolveAnnotationValue(annotation.maxFramePayloadLength(), Integer.class, "maxFramePayloadLength");

        boolean useEventExecutorGroup = resolveAnnotationValue(annotation.useEventExecutorGroup(), Boolean.class, "useEventExecutorGroup");
        int eventExecutorGroupThreads = resolveAnnotationValue(annotation.eventExecutorGroupThreads(), Integer.class, "eventExecutorGroupThreads");

        String sslKeyPassword = resolveAnnotationValue(annotation.sslKeyPassword(), String.class, "sslKeyPassword");
        String sslKeyStore = resolveAnnotationValue(annotation.sslKeyStore(), String.class, "sslKeyStore");
        String sslKeyStorePassword = resolveAnnotationValue(annotation.sslKeyStorePassword(), String.class, "sslKeyStorePassword");
        String sslKeyStoreType = resolveAnnotationValue(annotation.sslKeyStoreType(), String.class, "sslKeyStoreType");
        String sslTrustStore = resolveAnnotationValue(annotation.sslTrustStore(), String.class, "sslTrustStore");
        String sslTrustStorePassword = resolveAnnotationValue(annotation.sslTrustStorePassword(), String.class, "sslTrustStorePassword");
        String sslTrustStoreType = resolveAnnotationValue(annotation.sslTrustStoreType(), String.class, "sslTrustStoreType");

        String[] corsOrigins = annotation.corsOrigins();
        if (corsOrigins.length != 0) {
            for (int i = 0; i < corsOrigins.length; i++) {
                corsOrigins[i] = resolveAnnotationValue(corsOrigins[i], String.class, "corsOrigins");
            }
        }
        Boolean corsAllowCredentials = resolveAnnotationValue(annotation.corsAllowCredentials(), Boolean.class, "corsAllowCredentials");
        return new ServerEndpointConfig(host, port, bossLoopGroupThreads, workerLoopGroupThreads
                , useCompressionHandler, optionConnectTimeoutMillis, optionSoBacklog, childOptionWriteSpinCount, childOptionWriteBufferHighWaterMark
                , childOptionWriteBufferLowWaterMark, childOptionSoRcvbuf, childOptionSoSndbuf, childOptionTcpNodelay, childOptionSoKeepalive
                , childOptionSoLinger, childOptionAllowHalfClosure, readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds
                , maxFramePayloadLength, useEventExecutorGroup, eventExecutorGroupThreads
                , sslKeyPassword, sslKeyStore, sslKeyStorePassword, sslKeyStoreType
                , sslTrustStore, sslTrustStorePassword, sslTrustStoreType
                , corsOrigins, corsAllowCredentials);
    }

    private void scanPackage(ApplicationContext context) {
        String[] basePackages = null;
        //1.先从EnableWebSocket注解中获取
        String[] enableWebSocketBeanNames = context.getBeanNamesForAnnotation(EnableWebSocket.class);
        for (String enableWebSocketBeanName : enableWebSocketBeanNames) {
            Object enableWebSocketBean = context.getBean(enableWebSocketBeanName);
            EnableWebSocket enableWebSocket = AnnotationUtils.findAnnotation(enableWebSocketBean.getClass(), EnableWebSocket.class);
            assert enableWebSocket != null;
            if (enableWebSocket.scanBasePackages().length != 0) {
                basePackages = enableWebSocket.scanBasePackages();
                break;
            }
        }
        //2.如果EnableWebSocket 注解中没有获取到 那么就去启动类获取
        if (basePackages == null) {
            String[] springBootApplicationBeanName = context.getBeanNamesForAnnotation(SpringBootApplication.class);
            Object springBootApplicationBean = context.getBean(springBootApplicationBeanName[0]);
            SpringBootApplication springBootApplication = AnnotationUtils.findAnnotation(springBootApplicationBean.getClass(), SpringBootApplication.class);
            assert springBootApplication != null;
            if (springBootApplication.scanBasePackages().length != 0) {
                basePackages = springBootApplication.scanBasePackages();
            } else {
                String packageName = ClassUtils.getPackageName(springBootApplicationBean.getClass().getName());
                basePackages = new String[1];
                basePackages[0] = packageName;
            }
        }
        //在扫描包的时候 不实用默认的filters 自定义扫描规则 只扫描带了ServerEnpoint注解的类
        EndpointClassPathScanner scanHandle = new EndpointClassPathScanner((BeanDefinitionRegistry) context, false);
        if (resourceLoader != null) {
            scanHandle.setResourceLoader(resourceLoader);
        }
        //3.扫描basePackage 路径下的所有bean 注册到IOC
        for (String basePackage : basePackages) {
            scanHandle.doScan(basePackage);
        }
    }

    // todo debug
    private <T> T resolveAnnotationValue(Object value, Class<T> requiredType, String paramName) {
        if (value == null) {
            return null;
        }
        TypeConverter typeConverter = beanFactory.getTypeConverter();
        if (value instanceof String) {
            String strVal = beanFactory.resolveEmbeddedValue((String) value);
            BeanExpressionResolver beanExpressionResolver = beanFactory.getBeanExpressionResolver();
            if (beanExpressionResolver != null) {
                value = beanExpressionResolver.evaluate(strVal, new BeanExpressionContext(beanFactory, null));
            } else {
                value = strVal;
            }
        }
        try {
            return typeConverter.convertIfNecessary(value, requiredType);
        } catch (TypeMismatchException e) {
            throw new IllegalArgumentException("Failed to convert value of parameter '" + paramName + "' to required type '" + requiredType.getName() + "'");
        }
    }
}
