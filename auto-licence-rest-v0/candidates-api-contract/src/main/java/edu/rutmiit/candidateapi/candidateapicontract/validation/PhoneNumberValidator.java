package edu.rutmiit.candidateapi.candidateapicontract.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static java.lang.Character.isDigit;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber,String> {

    @Override
    public boolean isValid(String value,ConstraintValidatorContext context)
    {
        //Валидные значения "89647751789 или +79647758493"

        if (value == null || value.isBlank())
        {
            return true;
        }

        if ( (value.length()!=12 && value.length()!=11) || (value.charAt(0)!='8' && value.substring(0,2).equals("+7")))
        {
            return false;
        }

        for (int i=1;i<value.length();i++)
        {
            if (!isDigit(value.charAt(i)))
                return false;
        }

        return true;

    }



}

