package com.example.apigateway.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtils {
	@Value("${jwt.secret}")
	private String secret;

	public Claims getAllClaimsFromToken(String token) throws Exception {
		if(token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();		
	}

	private boolean isTokenExpired(String token) throws Exception {
		return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
	}

	public boolean isInvalid(String token) throws Exception {
		return this.isTokenExpired(token);
	}
}
