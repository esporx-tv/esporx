package tv.esporx.framework.time;

import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TimeZoneInitializer {

	@PostConstruct
	public void initializeTimezone() {
		DateTimeZone.setDefault(DateTimeZone.forID("Europe/Paris"));
	}

}
