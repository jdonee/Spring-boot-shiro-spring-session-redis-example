package org.jdonee.cooking.config.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shiro Redis Cache配置
 * 
 * @author Frank.Zeng
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shiro.redis")
@NoArgsConstructor
public class ShiroRedisProperties {

	private int database = 0;

	private String hostName = "localhost";

	private String password;

	private int port = 6379;

	private int timeout = 0;

	private int expire = 1800;
}
