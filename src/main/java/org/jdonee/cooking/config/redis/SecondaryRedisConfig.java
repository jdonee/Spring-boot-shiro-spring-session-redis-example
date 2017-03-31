package org.jdonee.cooking.config.redis;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis服务器对象缓存配置(对象缓存)
 * 
 * @author Frank.Zeng
 *
 */
@Configuration
@EnableCaching
@Slf4j
public class SecondaryRedisConfig extends CachingConfigurerSupport {

	/**
	 * 加载属性文件数据
	 * 
	 * @return
	 */
	@Bean
	public SecondRedisProperties redisProperties() {
		return new SecondRedisProperties();
	}

	/**
	 * 主键生成器
	 * 
	 * @return
	 */
	@Bean
	public KeyGenerator commonKeyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				String key = sb.toString();
				log.info("key:" + key);
				return key;
			}
		};

	}

	@Bean
	public CacheManager cacheManager(@Qualifier("secondaryStringRedisTemplate") RedisTemplate redisTemplate) {
		CacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
		return redisCacheManager;
	}

	private JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		SecondRedisProperties.Pool props = redisProperties().getPool();
		config.setMaxTotal(props.getMaxActive());
		config.setMaxIdle(props.getMaxIdle());
		config.setMinIdle(props.getMinIdle());
		config.setMaxWaitMillis(props.getMaxWait());
		return config;
	}

	@Bean(name = "secondaryRedisConnectionFactory")
	public RedisConnectionFactory secondaryRedisConnectionFactory() {
		JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig());
		redisConnectionFactory.setDatabase(redisProperties().getDatabase());
		redisConnectionFactory.setPassword(redisProperties().getPassword());
		redisConnectionFactory.setHostName(redisProperties().getHost());
		redisConnectionFactory.setTimeout(redisProperties().getTimeout());
		redisConnectionFactory.setPort(redisProperties().getPort());
		redisConnectionFactory.afterPropertiesSet();
		log.info("2.1 初始化Redis缓存服务器(普通对象)... ...");
		return redisConnectionFactory;
	}

	@Bean(name = "secondaryStringRedisTemplate")
	public RedisTemplate<String, String> redisTemplate(
			@Qualifier("secondaryRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
		log.info("2.2 初始化Redis模板(普通对象)... ...");
		StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		om.setSerializationInclusion(Include.NON_EMPTY);// 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper
		GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(om);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// template.afterPropertiesSet();
		return template;
	}
}
