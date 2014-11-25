package pl.srw.billcalculator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Kamil Seweryn
 */
public class Display {

    public static final int PAY_VALUE_SCALE = 2;

    public static String withScale(BigDecimal value, int scale) {
        return value.setScale(scale, RoundingMode.HALF_UP).toString();
    }

    public static String toPay(BigDecimal value) {
        return withScale(value, PAY_VALUE_SCALE);
    }
}
