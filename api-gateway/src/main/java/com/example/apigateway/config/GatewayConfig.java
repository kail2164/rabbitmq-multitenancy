package com.example.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.apigateway.filter.AuthenticationFilter;

@Configuration
public class GatewayConfig {
	private AuthenticationFilter filter;
	
	public GatewayConfig(AuthenticationFilter  authenticationFilter) {
		this.filter = authenticationFilter;
	}

	@Value("${gateway.host}")
	private String HOST;

	private final String URI = "%s:%d";
	private final int PORT_ACCOUNT = 8081, PORT_PRODUCT = 8082;
	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
			.route("account-service",
					r -> r.path("/api/account/**").filters(f -> f.filter(filter)).uri(String.format(URI, HOST, PORT_ACCOUNT)))
			.route("product-service",
					r -> r.path("/api/product/**").filters(f -> f.filter(filter)).uri(String.format(URI, HOST, PORT_PRODUCT)))
			.build();
	}

}
