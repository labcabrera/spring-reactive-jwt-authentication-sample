package com.github.labcabrera.jwt.sample.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;

import lombok.Data;

@Data
public class UserInfo {

	private String username;
	private List<String> claims;
	private LocalDateTime expiration;

	public UserInfo(Authentication authentication) {
		this.username = authentication.getName();
		this.claims = authentication.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList());
	}

}