# Spring-boot-shiro-spring-session-redis-example
* [spring-boot](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
* [mybatis](https://github.com/mybatis/spring-boot-starter)
* [druid](https://github.com/alibaba/druid)
* [shiro](http://shiro.apache.org/)
* [redis](http://redis.io/)
* [jedis](https://github.com/xetorthio/jedis)
* [lombok](https://projectlombok.org/)
* [thymeleaf](http://www.thymeleaf.org/)

> * 项目启动后输入：http://localhost:8080/
> * 该项目中, 增加了对url的拦截[URLPermissionsFilter](https://github.com/leelance/spring-boot-all/blob/master/spring-boot-shiro/src/main/java/com/lance/shiro/config/URLPermissionsFilter.java)，
> * 用admin/123456,拥有index权限reports未任何权限, jdonee/123456尚未分配任何权限.
> * 参考[schema.sql](https://github.com/leelance/spring-boot-all/blob/master/spring-boot-shiro/src/main/resources/init-sql/schema.sql)
> * shiro Cache交于Redis进行管理
> * springmvc-shiro采用xml配置, 参考[demo-springmvc-shiro](https://github.com/leelance/demo/tree/master/demo-springmvc-shiro)
