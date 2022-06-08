package com.example.apigateway.handler;

import org.springframework.web.reactive.handler.WebFluxResponseStatusExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class ExceptionHandler extends WebFluxResponseStatusExceptionHandler {
	 @Override
	    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
	        if (ex instanceof ResponseStatusException) {            
	            System.out.println(ex.getMessage());
	        }        
	        return Mono.error(ex);
	    }
}
