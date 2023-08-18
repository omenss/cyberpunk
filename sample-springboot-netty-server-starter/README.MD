# netty-server


### 步骤1
```shell
mvn clean install 
```

### 步骤2
在自己的代码里引入依赖
```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>netty-server-spring-boot-starter</artifactId>
    <version>${currentVersion></version>
</dependency>
```

### 步骤3 
在application.yml中配置
```yaml
netty:
  server:
    port: 8080
    host: 127.0.0.1
    protocl: tcp
    enabled: true
    useEpoll: false
```

### 步骤4
启动自己的代码 会看到如下日志
```shell
2020-12-29 16:27:27.755  INFO 10292 --- [           main] c.g.x.netty.server.NettyServer           : NettyServer started on port(s): 8080 (tcp) with context path ''
2020-12-29 16:27:27.755  INFO 10292 --- [           main] c.g.x.netty.server.NettyServer           : Started NettyServer in 0.01 seconds (JVM running for 0.01)
```
