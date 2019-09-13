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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.labcabrera.jwt.sample.model.Customer;
import com.github.labcabrera.jwt.sample.swagger.SwaggerConfig;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// tag::readme[]
@RequestMapping(value = "customers", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Customers")
public interface CustomerControllerDefinition {

	@GetMapping("/id")
	@ApiOperation(value = "Find customer by id", response = Customer.class, authorizations = {
		@Authorization(value = SwaggerConfig.API_KEY_NAME) })
	Mono<Customer> findOne(@ApiParam(value = "Customer identifier", required = true) @PathVariable final String id);

	@GetMapping
	@ApiOperation(value = "Find by rsql", response = Customer.class, authorizations = {
		@Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "Page", required = false, dataType = "string", paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "size", value = "Size", required = false, dataType = "string", paramType = "query", defaultValue = "10"),
		@ApiImplicitParam(name = "sort", value = "Sort", required = false, dataType = "string", paramType = "query", example = "") })
	Flux<Customer> find(
		@ApiParam(value = "RSQL expression", required = false, defaultValue = "") @RequestParam(value = "search", required = false, defaultValue = "") final String rsql,
		@ApiParam(value = "Page default value", required = false, example = "0") @RequestParam(name = "page", defaultValue = "0") final int page,
		@ApiParam(value = "Size default value", required = false, example = "10") @RequestParam(name = "size", defaultValue = "10") final int size);

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
// end::readme[]
