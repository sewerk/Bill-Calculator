package pl.srw.billcalculator.util;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.Period;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public static int countWholeMonth(String from, String to) {
        final double count = getMonthCount(from, to);
        final int wholeMonth = (int) count;
        return wholeMonth
                + (count-wholeMonth >= 0.5 ? 1 : 0);
    }

    public static BigDecimal countMonth(String from, String to) {
        final double count = getMonthCount(from, to);
        return new BigDecimal(count).setScale(4, RoundingMode.HALF_UP);
    }

    private static double getMonthCount(String from, String to) {
        final LocalDate startDate = parse(from);
        final LocalDate endDate = parse(to);
        final Period period = Period.between(startDate, endDate);
        if (period.getDays() == 0) // from 01.01 to 01.02 // full month
            return period.getMonths()
                    + period.getYears() * 12;

        if (period.getDays() == 1) {
            final int startMonthLength = startDate.getMonth().length(startDate.isLeapYear());
            if (startDate.getDayOfMonth() == startMonthLength) // from 30.04 to 31.05 // from 31.01 to 01.03
                return period.getMonths()
                        + period.getYears() * 12;
        }

        final int endMonthLength = endDate.getMonth().length(endDate.isLeapYear());
        final int days = startDate.getDayOfMonth() == 1 // from 01.01
                ? period.getDays()+1
                : period.getDays();
        final double monthPart = (double) days / endMonthLength;
        return monthPart
                + period.getMonths()
                + period.getYears() * 12;
    }

    public static LocalDate firstDayOfThisMonth() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate lastDayOfThisMonth() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }
}
