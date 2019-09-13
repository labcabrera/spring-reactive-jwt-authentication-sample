package com.github.labcabrera.jwt.sample.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import com.github.labcabrera.jwt.sample.model.Customer;
import com.github.labcabrera.jwt.sample.model.HasAuthorization;
import com.github.labcabrera.jwt.sample.rsql.RsqlParser;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class HasAutorizationEntityService<E extends HasAuthorization> {

	@Autowired
	private ReactiveMongoTemplate template;

	@Autowired
	private RsqlParser rsqlParser;

	public Mono<E> findById(String id) {
		return ReactiveSecurityContextHolder.getContext()
			.map(SecurityContext::getAuthentication)
			.flatMap(e -> findByIdWithAuthentication(id, e));
	}

	public Mono<E> findByIdWithAuthentication(String id, Authentication auth) {
		List<String> authorizations = auth.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList());
		Query query = new Query(Criteria.where("id").is(id).and("authorization").in(authorizations));
		return template.findOne(query, getEntityClass());
	}

	public Flux<E> findAll() {
		return ReactiveSecurityContextHolder.getContext()
			.map(SecurityContext::getAuthentication)
			.flatMapMany(e -> findAllWithAuthentication(e));
	}

	public Flux<E> findAllWithAuthentication(Authentication authentication) {
		List<String> authorizations = authentication.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList());
		Query query = new Query(Criteria.where("authorization").in(authorizations));
		return template.find(query, getEntityClass());
	}

	public Flux<E> findByRsql(String rsql, int page, int size) {
		return ReactiveSecurityContextHolder.getContext()
			.map(SecurityContext::getAuthentication)
			.flatMapMany(e -> findByRsqlWithAuthentication(rsql, page, size, e));
	}

	public Flux<E> findByRsqlWithAuthentication(String rsql, int page, int size, Authentication authentication) {
		List<String> authorizations = authentication.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList());
		Criteria criteria;
		if (StringUtils.isNotBlank(rsql)) {
			Criteria rsqlCriteria = rsqlParser.getCriteria(rsql, Customer.class);
			criteria = Criteria.where("authorization").in(authorizations).andOperator(rsqlCriteria);
		}
		else {
			criteria = Criteria.where("authorization").in(authorizations);
		}
		Query query = new Query(criteria);
		return template
			.find(query, getEntityClass())
			.skip(page * size)
			.take(size);
	}

	protected abstract Class<E> getEntityClass();
}
