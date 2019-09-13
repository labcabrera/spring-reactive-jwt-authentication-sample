package com.github.labcabrera.jwt.sample.rsql;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import com.github.rutledgepaulv.rqe.conversions.StringToTypeConverter;
import com.github.rutledgepaulv.rqe.conversions.parsers.StringToInstantConverter;
import com.github.rutledgepaulv.rqe.conversions.parsers.StringToObjectBestEffortConverter;

public class CustomSpringConversionServiceConverter implements StringToTypeConverter {

	private ConversionService conversionService;

	public CustomSpringConversionServiceConverter() {
		DefaultConversionService conversions = new DefaultConversionService();
		conversions.addConverter(new StringToInstantConverter());
		conversions.addConverter(new StringToObjectBestEffortConverter());
		conversions.addConverter(new StringToLocalDateTimeConverter());
		conversions.addConverter(new StringToLocalDateConverter());
		this.conversionService = conversions;
	}

	public CustomSpringConversionServiceConverter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return conversionService.canConvert(String.class, clazz);
	}

	@Override
	public Object apply(String s, Class<?> aClass) {
		return conversionService.convert(s, aClass);
	}
}