package com.example.common.config;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.common.constants.GlobalConstant;
import com.example.common.constants.RabbitMQConstant;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;
import com.example.common.util.CommonJwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public class JwtRequestFilter extends OncePerRequestFilter {
		@Autowired
		private CommonJwtUtil jwtUtil;
		@Autowired
		@Qualifier("handlerExceptionResolver")
		private HandlerExceptionResolver resolver;
		
		@Autowired 
		private RabbitTemplate sharedRabbitTemplate;
		
		@Override
		protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
			return CommonJwtUtil.isInWhiteList(request.getRequestURI()) || !CommonJwtUtil.isNotContainClientIdHeader(request) ;
		}
		
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
			try {		
				if (isAuthMissing(request)) {
					resolver.resolveException(request, response, null,
							new CustomException(APIStatus.UNAUTHORIZED, "Missing Authorization header"));
					return;
				}		
				final String token = this.getAuthHeader(request);
				if (jwtUtil.isInvalid(token)) {
					sharedRabbitTemplate.convertSendAndReceive(
							RabbitMQConstant.TOPIC_ACCOUNT, RabbitMQConstant.ROUTING_ACCOUNT_LOGOUT, token);
					resolver.resolveException(request, response, null,
							new CustomException(APIStatus.BAD_REQUEST, "JWT Token has been expired"));
					return;
				}
				ModifiedRequest modifiedRequest = populateRequestWithHeaders(request, token);
				filterChain.doFilter(modifiedRequest, response);
			} catch (IllegalArgumentException e) {
				resolver.resolveException(request, response, null,
						new CustomException(APIStatus.BAD_REQUEST, "Unable to get JWT Token"));
			} catch (ExpiredJwtException e) {
				resolver.resolveException(request, response, null,
						new CustomException(APIStatus.BAD_REQUEST, "JWT Token has been expired"));
			} catch (Exception e) {
				e.printStackTrace();
				resolver.resolveException(request, response, null,
						new CustomException(APIStatus.NOT_FOUND, e.getMessage()));
			}

		}
		
		

		private String getAuthHeader(HttpServletRequest request) {
			return request.getHeader(GlobalConstant.AUTHORIZATION_STRING);
		}

		private boolean isAuthMissing(HttpServletRequest request) {
			return Objects.isNull(request.getHeader(GlobalConstant.AUTHORIZATION_STRING))
					|| request.getHeader(GlobalConstant.AUTHORIZATION_STRING).isEmpty();
		}

		private ModifiedRequest populateRequestWithHeaders(HttpServletRequest request, String token) throws Exception {
			Claims claims = jwtUtil.getAllClaimsFromToken(token);
			ModifiedRequest modifiedRequest = new ModifiedRequest(request);
			modifiedRequest.addHeader(GlobalConstant.X_ACCOUNT_ID, String.valueOf(claims.get(GlobalConstant.ACCOUNT_ID_STRING)));
			return modifiedRequest;
		}
		
		private class ModifiedRequest extends HttpServletRequestWrapper {

			public ModifiedRequest(HttpServletRequest request) {
				super(request);

			}

			public void addHeader(String name, String value) {
				headerMap.put(name, value);
			}

			private Map<String, String> headerMap = new HashMap<String, String>();

			@Override
			public String getHeader(String name) {
				String headerValue = super.getHeader(name);
				if (headerMap.containsKey(name)) {
					headerValue = headerMap.get(name);
				}
				return headerValue;
			}

			/**
			 * get the Header names
			 */
			@Override
			public Enumeration<String> getHeaderNames() {
				List<String> names = Collections.list(super.getHeaderNames());
				for (String name : headerMap.keySet()) {
					names.add(name);
				}
				return Collections.enumeration(names);
			}

			@Override
			public Enumeration<String> getHeaders(String name) {
				List<String> values = Collections.list(super.getHeaders(name));
				if (headerMap.containsKey(name)) {
					values.add(headerMap.get(name));
				}
				return Collections.enumeration(values);
			}
		}
}
