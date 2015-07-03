package pl.srw.billcalculator.util;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.Period;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.util.Date;

/**
 * Created by Kamil Seweryn.
 */
public class Dates {

    private static final String DATE_PATTERN = "dd/MM/yyyy";

    public static LocalDate parse(String text) {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static String format(int year, Month month, int day) {
        final LocalDate date = LocalDate.of(year, month, day);
        return format(date, DATE_PATTERN);
    }

    public static String format(LocalDate date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(LocalDate date, String datePattern) {
        return date.format(DateTimeFormatter.ofPattern(datePattern));
    }

    public static String changeSeparator(String date, String separator) {
        return date.replaceAll("/", separator);
    }

    public static LocalDate toLocalDate(Date utilDate) {
        return DateTimeUtils.toLocalDate(new java.sql.Date(utilDate.getTime()));
    }
    
    public static Date toDate(LocalDate localDate) {
        return new Date(DateTimeUtils.toSqlDate(localDate).getTime());
    }

    public static int countMonth(String from, String to) {
        final Period period = Period.between(parse(from), parse(to));
        return period.getYears() * 12
                + period.getMonths()
                + ((period.getDays() > 14) ? 1 : 0);
    }

    public static LocalDate firstDayOfThisMonth() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate lastDayOfThisMonth() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }
}
