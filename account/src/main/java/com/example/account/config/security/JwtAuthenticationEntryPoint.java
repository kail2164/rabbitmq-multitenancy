package com.example.account.config.security;

import java.io.IOException;
import java.io.Serializable;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 149927274614483338L;	

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());	
	}

}
