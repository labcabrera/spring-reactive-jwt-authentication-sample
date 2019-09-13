package com.github.labcabrera.jwt.sample.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.github.labcabrera.jwt.sample.model.Customer;

import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

	Flux<Customer> findByFirstNameAndLastName(String firstName, String lastName);

}