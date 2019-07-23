package com.nu.constraint;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nu.dto.OperationType;

public class OperationValidator implements ConstraintValidator<Operation, String> {
	private OperationType[] allowedTypes;
	private boolean ignoreCase;

	@Override
	public void initialize(Operation constraint) {
		allowedTypes = constraint.value();
		ignoreCase = constraint.ignoreCase();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		String text = null;
		if (ignoreCase) {
			text = value.toUpperCase();
		}
		return Arrays.asList(allowedTypes).stream().map(x -> x.name()).collect(Collectors.toList()).contains(text);
	}

}
