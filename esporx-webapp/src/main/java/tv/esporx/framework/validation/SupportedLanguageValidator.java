package tv.esporx.framework.validation;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_PROTOTYPE)
public class SupportedLanguageValidator implements ConstraintValidator<SupportedLanguage, String> {
	private final Set<String> supportedLanguages = new HashSet<String>();

	@Override
	public void initialize(final SupportedLanguage annotation) {
		supportedLanguages.add("en");
		supportedLanguages.add("fr");
		supportedLanguages.add("de");
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		return supportedLanguages.contains(value);
	}

}
