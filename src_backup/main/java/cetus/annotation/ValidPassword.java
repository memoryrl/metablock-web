package cetus.annotation;

import kware.common.validator.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // allowNull 속성 추가
    boolean allowNull() default false;
}
