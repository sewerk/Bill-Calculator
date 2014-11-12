package pl.srw.billcalculator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Kamil Seweryn
 */
public class Display {

    public static final int PAY_VALUE_SCALE = 2;

    public static String price(BigDecimal cena, int scale) {
        return cena.setScale(scale, RoundingMode.HALF_UP).toString();
    }

    public static String toPay(BigDecimal value) {
        return price(value, PAY_VALUE_SCALE);
    }
}
