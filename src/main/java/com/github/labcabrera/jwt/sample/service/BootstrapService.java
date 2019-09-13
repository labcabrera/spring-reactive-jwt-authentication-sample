package com.github.labcabrera.jwt.sample.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.labcabrera.jwt.sample.model.Address;
import com.github.labcabrera.jwt.sample.model.ContactInfo;
import com.github.labcabrera.jwt.sample.model.Country;
import com.github.labcabrera.jwt.sample.model.Customer;
import com.github.labcabrera.jwt.sample.model.Gender;
import com.github.labcabrera.jwt.sample.repository.CountryRepository;
import com.github.labcabrera.jwt.sample.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class BootstrapService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CountryRepository countryRepository;

	public void initialize() {
		if (countryRepository.count().block() == 0L) {
			log.info("Initializing countries");
			Flux<Country> countries = Flux.just(
				Country.builder().id("ESP").name("Spain").build());
			countryRepository.saveAll(countries).blockLast();
		}
		if (customerRepository.count().block() == 0L) {
			log.info("Initializing customers");
			Flux<Customer> customers = Flux.just(
				Customer.builder()
					.firstName("John")
					.lastName("Doe")
					.gender(Gender.MALE)
					.contactInfo(ContactInfo.builder().email("johndoe@test.net").build())
					.address(Address.builder().countryId("ESP").province("Madrid").road("Some Road 2º").zipCode("28001").build())
					.authorization(Arrays.asList("distributor-01", "api-operator"))
					.build(),
				Customer.builder()
					.firstName("Alice")
					.lastName("Smith")
					.gender(Gender.FEMALE)
					.contactInfo(ContactInfo.builder().email("alicesmith@test.net").build())
					.address(Address.builder().countryId("ESP").province("Bilbao").road("Another Road 3").zipCode("10001").build())
					.authorization(Arrays.asList("distributor-01", "api-operator"))
					.build(),
				Customer.builder()
					.firstName("Milan")
					.lastName("Kundera")
					.gender(Gender.MALE)
					.contactInfo(ContactInfo.builder().email("milankundera@test.net").build())
					.address(Address.builder().countryId("ESP").province("Bilbao").road("Another Road 4").zipCode("10002").build())
					.authorization(Arrays.asList("distributor-02", "api-operator"))
					.build());
			customerRepository.saveAll(customers).blockLast();
		}
	}
}
