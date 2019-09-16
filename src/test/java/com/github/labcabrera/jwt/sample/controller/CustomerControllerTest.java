package com.github.labcabrera.jwt.sample.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.labcabrera.jwt.sample.SampleApplication;
import com.github.labcabrera.jwt.sample.model.Customer;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ReactiveMongoTemplate template;

	@Value("${security.jwt.secret}")
	private String secret;

	private Customer customer;

	@Before
	public void before() {
		customer = template.findOne(new Query(Criteria.where("authorization").in(Arrays.asList("distributor-01"))), Customer.class).block();
		Assume.assumeNotNull(customer);
	}

	@Test
	public void testNoJwtHeader() {
		webTestClient
			.get()
			.uri("/customers/{id}", customer.getId())
			.exchange()
			.expectStatus().isUnauthorized();
	}

	@Test
	public void testInvalidJwtHeader() {
		webTestClient
			.get()
			.uri("/customers/{id}", customer.getId())
			.header("Authorization", "Bearer a.b.c")
			.exchange()
			.expectStatus().isUnauthorized();
	}

	@Test
	public void testFindByIdOk() {
		webTestClient
			.get()
			.uri("/customers/{id}", customer.getId())
			.header("Authorization", "Bearer " + generateTestToken("test", "distributor-01"))
			.exchange()
			.expectStatus().isOk();
	}

	@Test
	public void testFindByIdInvalidToken() {
		webTestClient
			.get()
			.uri("/customers/{id}", customer.getId())
			.header("Authorization", "Bearer " + generateTestToken("test", "distributor-xx"))
			.exchange()
			.expectStatus().isUnauthorized();
	}

	private String generateTestToken(String username, String role) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expirationDate = now.plusMinutes(60);
		ZoneId zoneId = ZoneId.systemDefault();
		return Jwts.builder()
			.setIssuedAt(Date.from(now.atZone(zoneId).toInstant()))
			.setExpiration(Date.from(expirationDate.atZone(zoneId).toInstant())).setIssuer("sample-app-test")
			.setSubject(username).claim("roles", Arrays.asList(role))
			.signWith(SignatureAlgorithm.HS512, secret)
			.compact();
	}

}
