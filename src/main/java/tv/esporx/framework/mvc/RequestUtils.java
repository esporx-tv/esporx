package tv.esporx.framework.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import tv.esporx.framework.validation.SupportedLanguageValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static org.springframework.web.servlet.support.RequestContextUtils.getLocaleResolver;

@Component
@Scope(SCOPE_PROTOTYPE)
public class RequestUtils {

	private static final String DEFAULT_LOCALE = "en";

	@Autowired
	private SupportedLanguageValidator validator;

	public String currentLocale(final HttpServletRequest incomingRequest) {
		String result = DEFAULT_LOCALE;
		LocaleResolver localeResolver = getLocaleResolver(incomingRequest);
		if (localeResolver != null) {
			Locale locale = localeResolver.resolveLocale(incomingRequest);
			result = locale.getLanguage();
		}
		result = (validator.isValid(result, null)) ? result : DEFAULT_LOCALE;
		return result;
	}

}
