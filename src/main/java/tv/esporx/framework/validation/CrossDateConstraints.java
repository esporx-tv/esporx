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
@Constraint(validatedBy = CrossDateConstraintsValidator.class)
public @interface CrossDateConstraints {

	String message() default "{crossdate.validation.error}";

    /* mandatory */
	Class<?>[] groups() default {};

    /* mandatory */
	Class<? extends Payload>[] payload() default {};

	String startDateFieldName() default "startDate";

	String endDateFieldName() default "endDate";

    String[] nullableColumns() default {};
}
