package com.example.apigateway.validator;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouterValidator {
	private static final String[] ENDPOINTS = { "/api/account/auth" };

	public static final List<String> openApiEndpoints = Arrays.asList(ENDPOINTS);
	public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints.stream()
			.noneMatch(uri -> request.getURI().getPath().contains(uri));

}