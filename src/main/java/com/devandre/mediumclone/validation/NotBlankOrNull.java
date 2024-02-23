package com.devandre.mediumclone.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is a custom annotation that can be applied to fields.
 * It uses the NotBlankOrNullValidator class to validate that the field is not blank or null.
 * The annotation is retained at runtime.
 *
 * @author Andre on 15/02/2024
 * @project medium-clone
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankOrNullValidator.class)
public @interface NotBlankOrNull {

    /**
     * Defines the default error message that will be used if the validation fails.
     *
     * @return The default error message.
     */
    String message() default "{javax.validation.constraints.NotBlank.message}";

    /**
     * Allows the specification of validation groups.
     * Validation groups can be used to group certain validations together.
     *
     * @return The default group classes.
     */
    Class<?>[] groups() default {};

    /**
     * Can be used to associate metadata with a constraint.
     * The Payload interface is not used by the API itself; its usage is up to the application.
     *
     * @return The default payload classes.
     */
    Class<? extends Payload>[] payload() default {};
}
