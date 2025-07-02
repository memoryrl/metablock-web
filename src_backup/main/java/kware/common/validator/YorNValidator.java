package kware.common.validator;

import cetus.annotation.YOrN;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class YorNValidator implements ConstraintValidator<YOrN, String> {

    private boolean allowNull;

    @Override
    public void initialize(YOrN constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ( allowNull && value == null ) {
            return true;
        }
        boolean isValid = "Y".equals(value) || "N".equals(value);
        if(!isValid) {
            context.disableDefaultConstraintViolation();;
            context.buildConstraintViolationWithTemplate("{custom.validation.constraints.YOrN.message}")
                    .addConstraintViolation();
        }
        return isValid;
    }
}