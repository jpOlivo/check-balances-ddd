package com.nu.constraint;


import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.nu.dto.OperationType;

@Documented
@Constraint(validatedBy = OperationValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Operation {
	String message() default "the only allowed values are IN, OUT";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	OperationType[] value() default {};

	boolean ignoreCase() default false;
}
