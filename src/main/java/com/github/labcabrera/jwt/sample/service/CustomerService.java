package com.github.labcabrera.jwt.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.labcabrera.jwt.sample.model.Customer;
import com.github.labcabrera.jwt.sample.repository.CustomerRepository;

import reactor.core.publisher.Mono;

@Component
public class CustomerService extends HasAutorizationEntityService<Customer> {

	@Autowired
	private CustomerRepository repository;

	public Mono<Customer> save(String id, Customer customer) {
		return repository.findById(id).doOnSuccess(current -> {
			current.setFirstName(customer.getFirstName());
			current.setLastName(customer.getLastName());
			current.setGender(customer.getGender());
			current.setAddress(customer.getAddress());
			repository.save(current).subscribe();
		});
	}

	public Mono<Customer> remove(String id) {
		return repository.findById(id).doOnSuccess(current -> {
			repository.deleteById(id).subscribe();
		});
	}

	@Override
	protected Class<Customer> getEntityClass() {
		return Customer.class;
	}

}
