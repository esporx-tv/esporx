package tv.esporx.framework.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateTimeFormat {
    public static DateFormat getDefaultDateFormat() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        df.setLenient(false);
        return df;
    }

}
