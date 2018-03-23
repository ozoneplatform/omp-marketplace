package marketplace

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

import java.sql.Date

final class SqlDate {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(Constants.EXTERNAL_DATE_PARSE_FORMAT);

    private SqlDate() {}

    static Date now() {
        new Date((new java.util.Date()).time)
    }

    static <T extends java.util.Date> Date from(T date) {
        new Date(date.time)
    }

    static Date parse(String date) throws ParseException {
        new Date(DATE_FORMAT.parse(date).time)
    }

}
