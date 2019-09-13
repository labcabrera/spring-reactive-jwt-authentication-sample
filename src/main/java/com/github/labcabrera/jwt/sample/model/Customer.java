package com.github.labcabrera.jwt.sample.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer implements HasAuthorization {

	@Id
	protected String id;

	@NotNull
	protected String firstName;

	@NotNull
	protected String lastName;

	protected Gender gender;

	protected ContactInfo contactInfo;

	protected Address address;

	protected List<String> authorization;

}
