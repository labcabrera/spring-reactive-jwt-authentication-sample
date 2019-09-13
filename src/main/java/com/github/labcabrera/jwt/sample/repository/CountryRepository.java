package com.github.labcabrera.jwt.sample.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.github.labcabrera.jwt.sample.model.Country;

public interface CountryRepository extends ReactiveMongoRepository<Country, String> {

}