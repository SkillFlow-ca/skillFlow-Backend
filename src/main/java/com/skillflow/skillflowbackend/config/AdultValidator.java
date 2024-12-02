package com.skillflow.skillflowbackend.config;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Calendar;
import java.util.Date;

// Custom validator class
public class AdultValidator implements ConstraintValidator<Adult, Date> {

    private static final int MIN_AGE = 18;

    @Override
    public void initialize(Adult constraintAnnotation) {
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @Past annotation handle null values
        }
        Calendar dob = Calendar.getInstance();
        dob.setTime(value);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -MIN_AGE);
        return dob.before(now);    }


}
