package com.github.labcabrera.jwt.sample.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import com.github.labcabrera.jwt.sample.model.Customer;
import com.github.labcabrera.jwt.sample.repository.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomerService {

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private ReactiveMongoTemplate template;

	public Mono<Customer> findById(String id) {
		return ReactiveSecurityContextHolder.getContext()
			.map(SecurityContext::getAuthentication)
			.flatMap(e -> findByIdWithAuthentication(id, e));
	}

	public Mono<Customer> findByIdWithAuthentication(String id, Authentication auth) {
		List<String> authorizations = auth.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList());
		Query query = new Query(Criteria.where("id").is(id).and("authorization").in(authorizations));
		return template.findOne(query, Customer.class);
	}

	public Flux<Customer> find() {
		return ReactiveSecurityContextHolder.getContext()
			.map(SecurityContext::getAuthentication)
			.flatMapMany(e -> findWithAuthentication(e));
	}

	public Flux<Customer> findWithAuthentication(Authentication authentication) {
		List<String> authorizations = authentication.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList());
		Query query = new Query(Criteria.where("authorization").in(authorizations));
		return template.find(query, Customer.class);
	}

	public Mono<Customer> insert(Customer customer) {
		return repository.insert(customer);
	}

	public Mono<Customer> save(String id, Customer customer) {
		throw new RuntimeException("Not implemented");
		//		return findById(id).doOnSuccess(current -> {
		//			current.setFirstName(customer.getFirstName());
		//			current.setLastName(customer.getLastName());
		//			current.setContactInfo(customer.getContactInfo());
		//			repository.save(current).subscribe();
		//		});
	}

	public Flux<Customer> findByFirstNameAndLastName(String firstName, String lastName) {
		return repository.findByFirstNameAndLastName(firstName, lastName);
	}

	public Mono<Customer> remove(String id) {
		throw new RuntimeException("Not implemented");
	}

}
