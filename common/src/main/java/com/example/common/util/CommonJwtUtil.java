package com.example.common.util;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;

import com.example.common.constants.GlobalConstant;
import com.example.common.constants.RabbitMQConstant;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Configuration
public class CommonJwtUtil {
	private String secret = "12345678@Ab!";

	private static String[] whitelist = { "/docs/", "/api/common/auth", "/api/common/test/",
			"/health-check", "/webjars/", "/v3/", "/favicon.ico", "/configuration/" };

	public Claims getAllClaimsFromToken(String token) throws Exception {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	public static boolean isInWhiteList(String url) {
		return Arrays.stream(whitelist).anyMatch(url::contains);
	}

	public static boolean isNotContainClientIdHeader(HttpServletRequest request) {
		return Objects.isNull(request.getHeader(GlobalConstant.X_ACCOUNT_ID))
				|| request.getHeader(GlobalConstant.X_ACCOUNT_ID).isEmpty();
	}

	public boolean isInvalid(String token) throws Exception {
		boolean isValid = RabbitMQUtil.sendAndReceive(RabbitMQConstant.TOPIC_ACCOUNT,
				RabbitMQConstant.ROUTING_ACCOUNT_VALIDATE_TOKEN, token, Boolean.class);
		if (!isValid) {
			throw new CustomException(APIStatus.BAD_REQUEST, "Invalid token");
		}
		return this.isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) throws Exception {
		return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
	}
}
