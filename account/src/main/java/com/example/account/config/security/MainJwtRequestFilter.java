package com.example.account.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.account.service.AuthenticationService;
import com.example.account.util.JwtUtil;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;

import io.jsonwebtoken.ExpiredJwtException;

@Primary
@Component
public class MainJwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver resolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			final String requestTokenHeader = request.getHeader("Authorization");
			String username = null;
			String jwtToken = null;
			// JWT Token is in the form "Bearer token". Remove Bearer word and get
			// only the Token
			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
				username = jwtUtil.getUsernameFromToken(jwtToken);
			}
			//Once we get the token validate it.
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = authenticationService.loadUserByUsername(username);
			// if token is valid configure Spring Security to manually set
			// authentication
				if (jwtUtil.validateToken(jwtToken, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					// After setting the Authentication in the context, we specify
					// that the current user is authenticated. So it passes the
					// Spring Security Configurations successfully.
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			chain.doFilter(request, response);
		} catch (IllegalArgumentException e) {
			resolver.resolveException(request, response, null,
					new CustomException(APIStatus.BAD_REQUEST, "Unable to get JWT Token"));
		} catch (ExpiredJwtException e) {
			resolver.resolveException(request, response, null,
					new CustomException(APIStatus.INVALID_TOKEN, "JWT Token has been expired"));
		} catch (Exception e) {
			resolver.resolveException(request, response, null,
					new CustomException(APIStatus.NOT_FOUND, e.getMessage()));
		}
	}

}
