package com.dambrz.projectmanagementsystemapi.validation;

import com.dambrz.projectmanagementsystemapi.validation.annotation.DateValue;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class DateFieldsValueValidator implements ConstraintValidator<DateValue, Object>{

    private String field;
    private String fieldMatch;
    private String message;

    public void initialize(DateValue constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
        this.message = constraintAnnotation.message();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {

        Date fieldValue = (Date) new BeanWrapperImpl(value).getPropertyValue(field);
        Date fieldMatchValue = (Date) new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        boolean isValid = false;

        if (fieldValue != null && fieldMatchValue != null) {
            if (fieldValue.before(fieldMatchValue) || fieldValue.equals(fieldMatchValue)) isValid = true;
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(field)
                    .addConstraintViolation();

            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(fieldMatch)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
