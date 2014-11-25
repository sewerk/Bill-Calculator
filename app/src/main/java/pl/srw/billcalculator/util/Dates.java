package pl.srw.billcalculator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kamil Seweryn.
 */
public class Dates {

    public static final String DATE_PATTERN = "dd/MM/yyyy";

    public static Date parse(String date) {
        SimpleDateFormat dateParser = new SimpleDateFormat(DATE_PATTERN);
        try {
            return dateParser.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String format(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        return new SimpleDateFormat(DATE_PATTERN).format(c.getTime());
    }

    public static int countMonth(String from, String to) {
        Date fromDate = parse(from);
        Date toDate = parse(to);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(toDate.getTime() - fromDate.getTime());
        int years = cal.get(Calendar.YEAR) - 1970;
        int days = cal.get(Calendar.DAY_OF_MONTH);
        int months = cal.get(Calendar.MONTH) + ((days > 14) ? 1 : 0);

        return years * 12 + months;
    }
}
