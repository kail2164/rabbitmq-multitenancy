package com.example.apigateway.filter;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.example.apigateway.util.JWTUtils;
import com.example.apigateway.validator.RouterValidator;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {
	String X_ACCOUNT_ID = "X-Account-ID";
    private RouterValidator routerValidator;
    private JWTUtils jwtUtils;
    
    public AuthenticationFilter(RouterValidator routerValidator, JWTUtils jwtUtils) {
    	this.routerValidator = routerValidator;
    	this.jwtUtils = jwtUtils;
	}

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request))
                return onError(exchange, HttpStatus.UNAUTHORIZED);

            final String token = this.getAuthHeader(request);
            try {
            if (jwtUtils.isInvalid(token))
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            populateRequestWithHeaders(exchange, token);
            } catch (Exception e) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }


    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange,HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return Mono.error(new ResponseStatusException(httpStatus));
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) throws Exception{
        Claims claims = jwtUtils.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header(X_ACCOUNT_ID, String.valueOf(claims.get("accountId")))
                .build();
    }
}