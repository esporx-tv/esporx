package tv.esporx.framework.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = TwitterIdValidator.class)
public @interface ValidTwitterId {
    String message() default "{event.submission.error.hashtags}";

    /* mandatory */
    Class<?>[] groups() default {};

    /* mandatory */
    Class<? extends Payload>[] payload() default {};

    String value() default "";
}
