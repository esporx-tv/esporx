package tv.esporx.framework.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordRepeatConstraintValidator.class)
public @interface PasswordRepeatConstraint {

    String message() default "{user.password.validation.error}";

    String passwordFieldName() default "password";

    String passwordConfirmationFieldName() default "passwordConfirmation";

    /* mandatory */
    Class<?>[] groups() default {};

    /* mandatory */
    Class<? extends Payload>[] payload() default {};
}
