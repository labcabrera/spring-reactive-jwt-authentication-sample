package com.github.labcabrera.jwt.sample.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.github.labcabrera.jwt.sample.swagger.SwaggerConfig;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.Data;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/user")
public class UserController {

	@ApiOperation(value = "Display current user information", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping
	public Mono<UserInfo> currentUser(@ApiIgnore ServerWebExchange exchange) {
		return ReactiveSecurityContextHolder.getContext()
			.map(SecurityContext::getAuthentication)
			.map(e -> new UserInfo(e));
	}

	@Data
	public static class UserInfo {

		private String username;
		private List<String> claims;

		public UserInfo(Authentication authentication) {
			this.username = authentication.getName();
			this.claims = authentication.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList());
		}

	}

}
