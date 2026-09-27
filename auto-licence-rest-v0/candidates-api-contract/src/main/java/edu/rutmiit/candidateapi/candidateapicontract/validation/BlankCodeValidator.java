package edu.rutmiit.candidateapi.candidateapicontract.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/*
 * Проверяет, что введённая строка является корректным blank code.
 * Примеры валидных значений:
 * MOS_GOV-CLINIC1824_123714091467
 * SPB_PRI-CLINIC1965_122784948405
 * ECB_GOV-HOSPITAL1245_122782340505
 *
 */

public class BlankCodeValidator implements ConstraintValidator<ValidBlankCode, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String[] parsedString = value.split("_");
        String[] parsedClinicCode = parsedString[1].split("-");

        return (parsedString[0].length() == 3) &&
                (parsedClinicCode[0].length() == 3) &&
                (parsedClinicCode[1].contains("CLINIC") || parsedClinicCode[1].contains("HOSPITAL")) &&
                parsedString[2].length() == 12;
    }
}

