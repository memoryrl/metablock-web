package cetus.annotation;

import kware.common.validator.YorNValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YorNValidator.class)
public @interface YOrN {
    String message() default "{custom.validation.constraints.YOrN.message}";
    boolean allowNull() default true;   // null 허용 여부
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}