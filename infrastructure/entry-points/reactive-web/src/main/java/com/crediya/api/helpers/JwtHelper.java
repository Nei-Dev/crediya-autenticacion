package com.crediya.api.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtHelper {
	
	private final Key key;
	private final long jwtExpirationInMs;
	
	public JwtHelper(
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.expiration-in-ms:3600000}") long jwtExpirationInMs
	) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		this.jwtExpirationInMs = jwtExpirationInMs;
	}
	
	public String generateToken(String subject, Map<String, Object> claims) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
		
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(subject)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}
	
	public Claims getClaimsFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
	
	public String resolveToken(ServerWebExchange exchange) {
		String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (token != null && token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return null;
	}
	
	public boolean validateToken(String token) {
		try {
			getClaimsFromToken(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			log.error("Token validation failed: {}", e.getMessage());
			return false;
		}
	}
	
	public String getSubjectFromToken(String token) {
		return getClaimsFromToken(token).getSubject();
	}
}
