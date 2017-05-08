package pl.srw.billcalculator.util;

import android.support.annotation.Size;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.Period;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Locale;

public class Dates {

    public static final Locale PL_LOCALE = new Locale("pl", "PL");
    public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";

    public static LocalDate parse(String text, String pattern) {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(int year, Month month, int day, String pattern) {
        final LocalDate date = LocalDate.of(year, month, day);
        return format(date, pattern);
    }

    public static String format(LocalDate date, @Size(min = 4, max = 10) String datePattern) {
        return date.format(DateTimeFormatter.ofPattern(datePattern));
    }

    public static LocalDate toLocalDate(Date utilDate) {
        return DateTimeUtils.toLocalDate(new java.sql.Date(utilDate.getTime()));
    }
    
    public static Date toDate(LocalDate localDate) {
        return new Date(DateTimeUtils.toSqlDate(localDate).getTime());
    }

    public static int countWholeMonth(LocalDate from, LocalDate to) {
        final double count = getMonthCount(from, to);
        final int wholeMonth = (int) count;
        return wholeMonth
                + (count-wholeMonth >= 0.5 ? 1 : 0);
    }

    public static BigDecimal countMonth(LocalDate from, LocalDate to) {
        final double count = getMonthCount(from, to);
        return new BigDecimal(count).setScale(4, RoundingMode.HALF_UP);
    }

    private static double getMonthCount(LocalDate startDate, LocalDate endDate) {
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

    public static int countDaysFromJuly16(LocalDate startDate, LocalDate endDate) {
        final LocalDate julyFirst = LocalDate.of(2016, Month.JULY, 1);
        if (endDate.isBefore(julyFirst)) {
            return 0;
        }
        if (startDate.isBefore(julyFirst)) {
            return countDays(julyFirst, endDate);
        } else
            return countDays(startDate, endDate);
    }

    public static int countDays(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }
}
