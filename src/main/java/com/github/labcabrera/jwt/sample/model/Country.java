package com.github.labcabrera.jwt.sample.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "countries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {

	@Id
	private String id;

	private String name;

}
