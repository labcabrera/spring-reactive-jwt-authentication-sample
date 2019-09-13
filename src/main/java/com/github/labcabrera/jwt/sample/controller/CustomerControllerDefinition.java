package com.github.labcabrera.jwt.sample.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.labcabrera.jwt.sample.model.Customer;
import com.github.labcabrera.jwt.sample.swagger.SwaggerConfig;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping(value = "customers", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Customers")
public interface CustomerControllerDefinition {

	@GetMapping("/id")
	@ApiOperation(value = "Find customer by id", response = Customer.class, authorizations = {
		@Authorization(value = SwaggerConfig.API_KEY_NAME) })
	Mono<Customer> findOne(@ApiParam(value = "Customer identifier", required = true) @PathVariable final String id);

	@GetMapping
	@ApiOperation(value = "Find All", response = Customer.class, authorizations = {
		@Authorization(value = SwaggerConfig.API_KEY_NAME) })
	Flux<Customer> findAll();

	@PostMapping
	@ApiOperation(value = "Creates a new customer", authorizations = {
		@Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@ResponseStatus(HttpStatus.CREATED)
	Mono<Customer> create(@ApiParam(value = "Customer object", required = true) @RequestBody @Valid Customer customer);

	@PutMapping("/{id}")
	@ResponseBody
	@ApiOperation(value = "Update customer data", authorizations = {
		@Authorization(value = SwaggerConfig.API_KEY_NAME) })
	Mono<Customer> update(
		@ApiParam(value = "Customer identifier") @PathVariable String id,
		@ApiParam(value = "Updated customer data", required = true) @RequestBody @Valid Customer customer);

	@DeleteMapping("/{id}")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete a particular customer", authorizations = {
		@Authorization(value = SwaggerConfig.API_KEY_NAME) })
	Mono<Customer> delete(@ApiParam(value = "The customer's id that needs to be deleted") @PathVariable String id);

}
