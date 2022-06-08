package com.example.apigateway.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;

@Configuration
public class CustomExceptionConfig {
	@Bean
	public ErrorAttributes errorAttributes() {
		return new DefaultErrorAttributes() {

			@Override
			public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {			
				Map<String, Object> errorAttributes = new LinkedHashMap<>();				
				errorAttributes.put("status", 401);
				errorAttributes.put("message", "Token is invalid or expired");
				return errorAttributes;
			}

		};
	}
}
