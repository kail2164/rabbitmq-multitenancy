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

import com.example.common.constants.GlobalConstant;
import com.example.common.constants.RabbitMQConstant;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;
import com.example.common.util.JwtUtils;
import com.example.common.util.RabbitMQUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.NoArgsConstructor;

@Primary
@Component
@NoArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
	private JwtUtils jwtUtil;	
	private HandlerExceptionResolver resolver;

	@Autowired
	public JwtRequestFilter(JwtUtils jwtUtil,@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		super();
		this.jwtUtil = jwtUtil;
		this.resolver = resolver;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return jwtUtil.isInWhiteList(request.getRequestURI()) || !jwtUtil.isNotContainAccountIdHeader(request);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			if (isAuthMissing(request)) {
				resolver.resolveException(request, response, null,
						new CustomException(APIStatus.UNAUTHORIZED, "Missing Authorization header"));
				return;
			}
			final String requestTokenHeader = request.getHeader("Authorization");
			// JWT Token is in the form "Bearer token". Remove Bearer word and get
			// only the Token
			String jwtToken = requestTokenHeader.substring(7);
			if (jwtUtil.isInvalid(jwtToken)) {
				callLogout(request, response, jwtToken);
				return;
			}
			String username = jwtUtil.getUsernameFromToken(jwtToken);
			// Once we get the token validate it.
			boolean authenticationIsNull = SecurityContextHolder.getContext().getAuthentication() == null;
			if (username != null && authenticationIsNull) {
				UserDetails userDetails = RabbitMQUtils.sendAndReceive(RabbitMQConstant.TOPIC_ACCOUNT,
						RabbitMQConstant.ROUTING_ACCOUNT_GET_USER_DETAILS, username, UserDetails.class);
				// if token is valid configure Spring Security to manually set
				// authentication
				if (jwtUtil.validateToken(jwtToken, userDetails)) {
					setAuthentication(userDetails, request);
				}
			}
			ModifiedRequest modifiedRequest = populateRequestWithHeaders(request, jwtToken);
			chain.doFilter(modifiedRequest, response);
		} catch (IllegalArgumentException e) {
			resolver.resolveException(request, response, null,
					new CustomException(APIStatus.BAD_REQUEST, "Unable to get JWT Token"));
		} catch (ExpiredJwtException e) {
			resolver.resolveException(request, response, null,
					new CustomException(APIStatus.INVALID_TOKEN, "JWT Token has been expired"));
		} catch (Exception e) {
			resolver.resolveException(request, response, null,
					new CustomException(APIStatus.BAD_REQUEST, e.getMessage()));
		}
	}

	private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		// After setting the Authentication in the context, we specify
		// that the current user is authenticated. So it passes the
		// Spring Security Configurations successfully.
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	}

	private void callLogout(HttpServletRequest request, HttpServletResponse response, String jwtToken)
			throws CustomException {
		RabbitMQUtils.send(RabbitMQConstant.TOPIC_ACCOUNT, RabbitMQConstant.ROUTING_ACCOUNT_LOGOUT, jwtToken);
		resolver.resolveException(request, response, null,
				new CustomException(APIStatus.BAD_REQUEST, "JWT Token has been expired"));
	}

	private boolean isAuthMissing(HttpServletRequest request) {
		return Objects.isNull(request.getHeader(GlobalConstant.AUTHORIZATION_STRING))
				|| request.getHeader(GlobalConstant.AUTHORIZATION_STRING).isEmpty();
	}

	private ModifiedRequest populateRequestWithHeaders(HttpServletRequest request, String token) throws Exception {
		Claims claims = jwtUtil.getAllClaimsFromToken(token);
		ModifiedRequest modifiedRequest = new ModifiedRequest(request);
		modifiedRequest.addHeader(GlobalConstant.X_ACCOUNT_ID,
				String.valueOf(claims.get(GlobalConstant.ACCOUNT_ID_STRING)));
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
