package com.github.labcabrera.jwt.sample.security;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

	private final String secret;

	public JwtServerAuthenticationConverter(Environment env) {
		if (!env.containsProperty("security.jwt.secret")) {
			throw new IllegalArgumentException("Missing required property 'security.jwt.secret'");
		}
		secret = env.getProperty("security.jwt.secret");
	}

	@Override
	public Mono<Authentication> convert(ServerWebExchange swe) {
		ServerHttpRequest request = swe.getRequest();
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String jwt = authHeader.substring(7);
			Authentication auth = buildAuthentication(jwt);
			return Mono.justOrEmpty(auth);
		}
		else {
			log.debug("Missing authorization header");
			return Mono.empty();
		}
	}

	private Authentication buildAuthentication(String jwt) {
		Jws<Claims> claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt);
		}
		catch (Exception ex) {
			log.debug("JWT parse error", ex);
			return null;
		}
		String username = claims.getBody().getSubject();
		if (username == null) {
			log.debug("Missing subject in JWT token");
			return null;
		}
		List<?> roles = claims.getBody().get("roles", List.class);
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
			username,
			username,
			roles.stream().map(e -> new SimpleGrantedAuthority(String.valueOf(e))).collect(Collectors.toList()));
		Map<String, Object> details = new LinkedHashMap<>();
		details.put("issuer", claims.getBody().getIssuer());
		details.put("expiration", claims.getBody().getExpiration());
		details.put("issuedAt", claims.getBody().getIssuedAt());
		result.setDetails(details);
		log.debug("Granted authorities: {}", result.getAuthorities());
		return result;
	}

}
