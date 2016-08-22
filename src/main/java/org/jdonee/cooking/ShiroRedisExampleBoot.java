package org.jdonee.cooking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

@SpringBootApplication
@MapperScan("org.jdonee.cooking.mapper")
public class ShiroRedisExampleBoot extends SpringBootServletInitializer {

	/**
	 * 增加Shiro模板引擎
	 * 
	 * @return
	 */
	@Bean
	public ShiroDialect conditionalSecurityDialect() {
		return new ShiroDialect();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ShiroRedisExampleBoot.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ShiroRedisExampleBoot.class, args);
	}
}
