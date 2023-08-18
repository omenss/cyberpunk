package com.cyberpunk.netty.config;

import com.cyberpunk.netty.annotation.ServerEndpoint;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @author lujun
 * @date 2023/8/18 14:08
 */
public class EndpointClassPathScanner extends ClassPathBeanDefinitionScanner {
    public EndpointClassPathScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }


    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        //只对ServerEndpoint注解的类进行扫描
        addIncludeFilter(new AnnotationTypeFilter(ServerEndpoint.class));
        return super.doScan(basePackages);
    }

}
