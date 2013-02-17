package tv.esporx.framework.time;

import org.joda.time.DateTimeZone;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.TimeZone;

import static java.util.Locale.ENGLISH;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Component
@Lazy(false)
@Order(HIGHEST_PRECEDENCE)
public class Localizer {

	public Localizer() {
        DateTimeZone zone = DateTimeZone.forID("Europe/Paris");
        TimeZone.setDefault(zone.toTimeZone());
        DateTimeZone.setDefault(zone);
        Locale.setDefault(ENGLISH);
	}

}
