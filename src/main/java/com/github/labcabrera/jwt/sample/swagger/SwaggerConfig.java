package com.github.labcabrera.jwt.sample.swagger;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import com.fasterxml.classmate.TypeResolver;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.SpringIntegrationWebFluxRequestHandlerProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@Configuration
@EnableSwagger2WebFlux
@Import(SpringIntegrationWebFluxRequestHandlerProvider.class)
public class SwaggerConfig {

	public static final String API_KEY_NAME = "token";

	@Autowired
	private TypeResolver resolver;

	@Bean
	Docket docket(Environment env, List<SecurityScheme> authorizationTypes) {
		return new Docket(DocumentationType.SWAGGER_2)
			.forCodeGeneration(true)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.github.labcabrera"))
			.paths(PathSelectors.any()).build()
			.protocols(Stream.of("http", "https").collect(toSet()))
			.alternateTypeRules(new RecursiveAlternateTypeRule(resolver,
				Arrays.asList(
					AlternateTypeRules.newRule(resolver.resolve(Mono.class, WildcardType.class),
						resolver.resolve(WildcardType.class)),
					AlternateTypeRules.newRule(resolver.resolve(ResponseEntity.class, WildcardType.class),
						resolver.resolve(WildcardType.class)))))
			.alternateTypeRules(new RecursiveAlternateTypeRule(resolver,
				Arrays.asList(
					AlternateTypeRules.newRule(resolver.resolve(Flux.class, WildcardType.class),
						resolver.resolve(List.class, WildcardType.class)),
					AlternateTypeRules.newRule(resolver.resolve(ResponseEntity.class, WildcardType.class),
						resolver.resolve(WildcardType.class)))))
			.apiInfo(apiInfo(env))
			.securitySchemes(Arrays.asList(apiKey()));
	}

	private ApiKey apiKey() {
		return new ApiKey(API_KEY_NAME, "Authorization", "header");
	}

	private ApiInfo apiInfo(Environment env) {
		return new ApiInfoBuilder()
			.title(env.getProperty("app.api.swagger.doc.title"))
			.description(env.getProperty("app.api.swagger.doc.description"))
			.contact(new Contact(
				env.getProperty("app.api.swagger.doc.contact.name"),
				env.getProperty("app.api.swagger.doc.contact.url"),
				env.getProperty("app.api.swagger.doc.contact.email")))
			.license(env.getProperty("app.api.swagger.doc.license.name"))
			.licenseUrl(env.getProperty("app.api.swagger.doc.license.url"))
			.termsOfServiceUrl("")
			.version(env.getProperty("app.api.swagger.doc.version"))
			.build();
	}

}
