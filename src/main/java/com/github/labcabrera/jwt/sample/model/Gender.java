package com.github.labcabrera.jwt.sample.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.STRING)
public enum Gender {

	MALE("male"),

	FEMALE("female");

	private String code;

	private Gender(String code) {
		this.code = code;
	}
	
	@JsonValue
	public String getCode() {
		return code;
	}

}
