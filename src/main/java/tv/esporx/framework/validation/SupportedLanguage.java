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
@Constraint(validatedBy = SupportedLanguageValidator.class)
public @interface SupportedLanguage {
	String message() default "{channel.submission.error.language}";

    /* mandatory */
	Class<?>[] groups() default {};

    /* mandatory */
	Class<? extends Payload>[] payload() default {};

	String value() default "";
}
