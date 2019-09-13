package com.github.labcabrera.jwt.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.labcabrera.jwt.sample.model.Customer;
import com.github.labcabrera.jwt.sample.repository.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomerService {

	@Autowired
	private CustomerRepository repository;

	public Mono<Customer> findById(String id) {
		return repository.findById(id);
	}

	public Flux<Customer> findAll() {
		return repository.findAll();
	}

	public Mono<Customer> insert(Customer customer) {
		return repository.insert(customer);
	}

	public Mono<Customer> save(String id, Customer customer) {
		return findById(id).doOnSuccess(current -> {
			current.setFirstName(customer.getFirstName());
			current.setLastName(customer.getLastName());
			current.setContactInfo(customer.getContactInfo());
			repository.save(current).subscribe();
		});
	}

	public Flux<Customer> findByFirstNameAndLastName(String firstName, String lastName) {
		return repository.findByFirstNameAndLastName(firstName, lastName);
	}

	public Mono<Customer> remove(String id) {
		throw new RuntimeException("Not implemented");
	}

}
