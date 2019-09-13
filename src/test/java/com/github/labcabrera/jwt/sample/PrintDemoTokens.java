package com.github.labcabrera.jwt.sample;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class PrintDemoTokens {

	@Test
	public void test() {
		printToken("distributor-01", Arrays.asList("distributor-01"));
		printToken("distributor-02", Arrays.asList("distributor-02"));
		printToken("api-operator", Arrays.asList("api-operator"));
	}

	private void printToken(String username, List<String> roles) {
		String issuer = "sample-app";
		String secret = "changeit";
		int expiration = 60000;

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expirationDate = now.plusMinutes(expiration);
		ZoneId zoneId = ZoneId.systemDefault();
		String token = Jwts.builder()
			.setIssuedAt(Date.from(now.atZone(zoneId).toInstant()))
			.setExpiration(Date.from(expirationDate.atZone(zoneId).toInstant())).setIssuer(issuer)
			.setSubject(username).claim("roles", roles)
			.signWith(SignatureAlgorithm.HS512, secret)
			.compact();
		System.out.println("Token " + username + ":");
		System.out.println("Bearer " + token);
		System.out.println();
	}

}
