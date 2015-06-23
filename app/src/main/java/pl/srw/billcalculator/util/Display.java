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
        return formatPay(withScale(value, PAY_VALUE_SCALE));
    }

    private static String formatPay(String amount) {
        StringBuilder amountBuilder = new StringBuilder(amount);
        int idx = 6;//123.56
        while (amount.length() - idx > 0) {
            if (amount.charAt(amount.length() - idx -1) != '-')
                amountBuilder.insert(amount.length() - idx, " ");
            idx+=3;
        }
        return amountBuilder.toString();
    }
}
