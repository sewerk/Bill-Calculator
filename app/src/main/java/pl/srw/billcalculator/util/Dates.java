package pl.srw.billcalculator.util;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.Period;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalAdjusters;

/**
 * Created by Kamil Seweryn.
 */
public class Dates {

    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static LocalDate parse(String text) {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static String format(int year, Month month, int day) {
        final LocalDate date = LocalDate.of(year, month, day);
        return date.format(FORMATTER);
    }

    public static String format(LocalDate date) {
        return date.format(FORMATTER);
    }

    public static String format(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        return dateFormat.format(date);
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
