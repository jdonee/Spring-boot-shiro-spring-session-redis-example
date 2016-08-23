package org.jdonee.cooking.config;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

public class ShrioRedisCacheManager extends AbstractCacheManager {

	private RedisTemplate<byte[], byte[]> redisTemplate;

	public ShrioRedisCacheManager(RedisTemplate<byte[], byte[]> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	protected Cache<byte[], byte[]> createCache(String name) throws CacheException {
		return new ShrioRedisCache<byte[], byte[]>(redisTemplate, name);
	}
}
