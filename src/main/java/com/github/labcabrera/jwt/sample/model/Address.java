package com.github.labcabrera.jwt.sample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

	private String countryId;

	private String province;

	private String road;

	private String zipCode;

}
