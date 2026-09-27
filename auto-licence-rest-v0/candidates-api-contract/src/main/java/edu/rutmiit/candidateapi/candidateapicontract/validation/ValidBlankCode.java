package edu.rutmiit.candidateapi.candidateapicontract.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Аннотация для валидации blank code.
 * Примеры валидных значений:
 * MOS_GOV-CLINIC1824_123714091467
 * SPB_PRI-CLINIC1965_122784948405
 * ECB_GOV-CLINIC1235_122782340505
 */
@Documented
@Constraint(validatedBy = BlankCodeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBlankCode {

    String message() default "Некорректный blank code. ДопустимыЙ формат MOS_GOV-CLINIC1824_123714091467";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
