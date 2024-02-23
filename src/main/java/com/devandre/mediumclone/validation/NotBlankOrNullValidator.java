package com.devandre.mediumclone.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;


/**
 * This class is a custom validator that implements the ConstraintValidator interface.
 * It validates whether a given string is not blank or null.
 *
 * @author Andre on 15/02/2024
 * @project medium-clone
 */
@NoArgsConstructor
public class NotBlankOrNullValidator implements ConstraintValidator<NotBlankOrNull, String> {

    // An instance of NotBlankValidator from the Hibernate Validator library.
    // This validator is used to check if a string is not blank.
    private final transient NotBlankValidator notBlankValidator = new NotBlankValidator();

    /**
     * This method overrides the isValid method from the ConstraintValidator interface.
     * It contains the actual validation logic.
     *
     * @param value The string value to validate.
     * @param context The context of the validation process.
     * @return true if the value is null or not blank, false otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // If the value is null, the method returns true, meaning the value is considered valid.
        if (value == null) {
            return true;
        }
        // If the value is not null, the method delegates the validation to the NotBlankValidator instance.
        // This checks if the string is not blank (i.e., it must contain at least one non-whitespace character).
        // The result of this validation is then returned.
        return notBlankValidator.isValid(value, context);
    }
}
