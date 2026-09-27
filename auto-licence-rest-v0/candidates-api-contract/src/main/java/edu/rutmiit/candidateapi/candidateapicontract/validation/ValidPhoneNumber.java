package edu.rutmiit.candidateapi.candidateapicontract.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default "Некорректный номер телефона. Допустимые форматы " +
            "89647751789 или +79647758493";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}


