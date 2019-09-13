package com.github.labcabrera.jwt.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.github.labcabrera.jwt.sample.model.Customer;
import com.github.labcabrera.jwt.sample.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CustomerController implements CustomerControllerDefinition {

	@Autowired
	private CustomerService customerService;

	@Override
	public Mono<Customer> findOne(String id) {
		return customerService.findById(id);
	}

	@Override
	public Flux<Customer> find(String rsql, int page, int size) {
		return customerService.findByRsql(rsql, page, size);
	}

	@Override
	public Mono<Customer> create(Customer customer) {
		return customerService.insert(customer);
	}

	@Override
	public Mono<Customer> update(String id, Customer customer) {
		return customerService.save(id, customer);
	}

	@Override
	public Mono<Customer> delete(String id) {
		return customerService.remove(id);
	}

}
