package org.jdonee.cooking.config;

import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.google.common.collect.Maps;

@Configuration
@EnableRedisHttpSession
public class ShiroConfig {

	/**
	 * 加载属性文件数据
	 * 
	 * @return
	 */
	@Bean
	public ShiroRedisProperties shiroProperties() {
		return new ShiroRedisProperties();
	}

	/**
	 * 与Session有关设置链接
	 * 
	 * @return
	 */
	@Bean
	public RedisOperationsSessionRepository sessionRepository() {
		RedisOperationsSessionRepository sessionRepository = new RedisOperationsSessionRepository(connectionFactory());
		sessionRepository.setDefaultMaxInactiveInterval(shiroProperties().getExpire());// 设置session的有效时长
		return sessionRepository;
	}

	/**
	 * FilterRegistrationBean
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
		return filterRegistration;
	}

	/**
	 * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean
	 * @return
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter() {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager());
		bean.setLoginUrl("/login");
		bean.setUnauthorizedUrl("/unauthor");

		Map<String, Filter> filters = Maps.newHashMap();
		filters.put("perms", urlPermissionsFilter());
		filters.put("anon", new AnonymousFilter());
		bean.setFilters(filters);

		Map<String, String> chains = Maps.newHashMap();
		chains.put("/druid/**", "anon");
		chains.put("/login", "anon");
		chains.put("/unauthor", "anon");
		chains.put("/logout", "logout");
		chains.put("/base/**", "anon");
		chains.put("/css/**", "anon");
		chains.put("/layer/**", "anon");
		chains.put("/**", "authc,perms");
		bean.setFilterChainDefinitionMap(chains);
		return bean;
	}

	/**
	 * 权限管理器
	 * 
	 * @return
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		// 数据库认证的实现
		manager.setRealm(userRealm());
		// session 管理器
		manager.setSessionManager(sessionManager());
		// 缓存管理器
		manager.setCacheManager(redisCacheManager());
		return manager;
	}

	/**
	 * DefaultWebSessionManager
	 * 
	 * @return
	 */
	@Bean(name = "sessionManager")
	public ServletContainerSessionManager sessionManager() {
		ServletContainerSessionManager sessionManager = new ServletContainerSessionManager();
		return sessionManager;
	}

	/**
	 * @see UserRealm--->AuthorizingRealm
	 * @return
	 */
	@Bean
	@DependsOn(value = { "lifecycleBeanPostProcessor", "shrioRedisCacheManager" })
	public UserRealm userRealm() {
		UserRealm userRealm = new UserRealm();
		userRealm.setCacheManager(redisCacheManager());
		userRealm.setCachingEnabled(true);
		userRealm.setAuthenticationCachingEnabled(true);
		userRealm.setAuthorizationCachingEnabled(true);
		return userRealm;
	}

	@Bean
	public URLPermissionsFilter urlPermissionsFilter() {
		return new URLPermissionsFilter();
	}

	@Bean(name = "shrioRedisCacheManager")
	@DependsOn(value = "shiroRedisTemplate")
	public ShrioRedisCacheManager redisCacheManager() {
		ShrioRedisCacheManager cacheManager = new ShrioRedisCacheManager(shiroRedisTemplate());
		cacheManager.createCache("shiro_redis:");
		return cacheManager;
	}

	@Bean(name = "shiroRedisTemplate")
	public RedisTemplate<byte[], byte[]> shiroRedisTemplate() {
		RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory());
		return template;
	}

	/**
	 * RedisTemplate
	 * 
	 * @return
	 */
	@Bean(name = "redisTemplate")
	public RedisTemplate<String, Object> sessionRedisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory());
		RedisSerializer stringSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringSerializer);
		template.setValueSerializer(sessionRedisSerializer());
		template.setHashKeySerializer(stringSerializer);
		template.setHashValueSerializer(sessionRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

	/**
	 * 设置redisTemplate的存储格式（在此与Session没有什么关系）
	 * 
	 * @return
	 */
	@Bean
	@SuppressWarnings("rawtypes")
	public RedisSerializer sessionRedisSerializer() {
		return new Jackson2JsonRedisSerializer<Object>(Object.class);
	}

	/**
	 * Redis连接客户端
	 * 
	 * @return
	 */
	@Bean(name = "connectionFactory")
	@DependsOn(value = "shiroProperties")
	public RedisConnectionFactory connectionFactory() {
		JedisConnectionFactory conn = new JedisConnectionFactory();
		conn.setDatabase(shiroProperties().getDatabase());
		conn.setHostName(shiroProperties().getHostName());
		conn.setPassword(shiroProperties().getPassword());
		conn.setPort(shiroProperties().getPort());
		conn.setTimeout(shiroProperties().getTimeout());
		return conn;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

}