package com.example.common.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.common.constants.GlobalConstant;
import com.example.common.constants.RabbitMQConstant;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5086515243613385589L;
	public static final long JWT_TOKEN_VALIDITY = 12 * 60 * 60;
	private String secret = "12345678@Ab!";
	private static String[] whitelist = { "/docs/", "/api/auth", "/health-check",
			"/webjars/", "/v3/", "/favicon.ico", "/configuration/" };

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieveing any information from token we will need the secret key
	public Claims getAllClaimsFromToken(String token) {
		try {
			if (token.startsWith("Bearer ")) {
				token = token.substring(7);
			}
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
	public boolean isInWhiteList(String url) {
		return Arrays.stream(whitelist).anyMatch(url::contains);
	}

	public boolean isNotContainAccountIdHeader(HttpServletRequest request) {
		return Objects.isNull(request.getHeader(GlobalConstant.X_ACCOUNT_ID))
				|| request.getHeader(GlobalConstant.X_ACCOUNT_ID).isEmpty();
	}

	public boolean isInvalid(String token) throws Exception {
		boolean isValid = RabbitMQUtils.sendAndReceive(RabbitMQConstant.TOPIC_ACCOUNT,
				RabbitMQConstant.ROUTING_ACCOUNT_VALIDATE_TOKEN, token, Boolean.class);
		if (!isValid) {
			throw new CustomException(APIStatus.BAD_REQUEST, "Invalid token");
		}
		return this.isTokenExpired(token);
	}

	// check if the token has expired
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// check if the TokenStored has expired
//	public Boolean isTokenStoredExpired(TokenStored tokenStore) {
//		final Date expiration = tokenStore.getExpireAt();
//		return expiration.before(new Date());
//	}

	// generate token for user
	public String generateToken(UserDetails userDetails, Long id) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(GlobalConstant.ACCOUNT_ID_STRING, id);
		return doGenerateToken(claims, userDetails.getUsername());
	}

	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	// 3. According to JWS Compact
	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	// compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	// validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
