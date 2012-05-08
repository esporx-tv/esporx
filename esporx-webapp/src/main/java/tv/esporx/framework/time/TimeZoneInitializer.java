package tv.esporx.framework.time;

import javax.annotation.PostConstruct;

import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

@Component
public class TimeZoneInitializer {

	@PostConstruct
	public void initializeTimezone() {
		DateTimeZone.setDefault(DateTimeZone.forID("Europe/Paris"));
	}

}
