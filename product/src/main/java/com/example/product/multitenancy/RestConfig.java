package com.example.product.multitenancy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.NoArgsConstructor;

@Configuration
public class RestConfig implements WebMvcConfigurer {
	private HandlerInterceptor tenantInterceptor;

	@Autowired
	public RestConfig(HandlerInterceptor tenantInterceptor) {
		this.tenantInterceptor = tenantInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tenantInterceptor);
	}

}
