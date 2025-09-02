package com.crediya.tokenservice;

import com.crediya.model.user.AuthClaims;
import com.crediya.model.user.User;
import com.crediya.model.user.UserClaims;
import com.crediya.model.user.UserRole;
import com.crediya.model.user.gateways.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProviderAdapter implements TokenService {
	
	private final Key key;
	private final long jwtExpirationInMs;
	
	public JwtProviderAdapter(
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.expiration}") long jwtExpirationInMs
	) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		this.jwtExpirationInMs = jwtExpirationInMs;
	}
	
	@Override
	public String generateToken(User user) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
		
		return Jwts.builder()
			.setSubject(user.getEmail())
			.claim(AuthClaims.USER_ID.getValue(), user.getIdUser())
			.claim(AuthClaims.ROLE.getValue(), user.getRole().name())
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}
	
	@Override
	public Mono<UserClaims> validateToken(String token) {
		try {
			Claims claims = getClaimsFromToken(token);
			return Mono.just(new UserClaims(
				claims.get(AuthClaims.USER_ID.getValue(), Long.class),
				claims.getSubject(),
				UserRole.valueOf(claims.get(AuthClaims.ROLE.getValue(), String.class))
			));
		} catch (Exception ex) {
			return Mono.error(new RuntimeException("Errorrrrr validating token", ex));
		}
	}
	
	private Claims getClaimsFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
