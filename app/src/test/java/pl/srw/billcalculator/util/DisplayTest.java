package pl.srw.billcalculator.util;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by kseweryn on 22.06.15.
 */
@RunWith(JUnitParamsRunner.class)
public class DisplayTest {

    @Test
    @Parameters({"0.99|0.99", "-0.99|-0.99",
            "123.56|123.56", "-123.56|-123.56",
            "1234.67|1 234.67", "-1234.67|-1 234.67",
            "12345.78|12 345.78", "-12345.78|-12 345.78",
            "123456.89|123 456.89", "-123456.89|-123 456.89",
            "34567890.12|34 567 890.12", "-34567890.12|-34 567 890.12"})
    public void testToPay(String input, String expected) {
        assertThat(Display.toPay(new BigDecimal(input)), is(expected));
    }

    @Test
    @Parameters({"0.98765,2|0.99", "1234.5678,3|1234.568", "12.1234,2|12.12"})
    public void testRoundUp(String inValue, int inScale, String expected) {
        assertThat(Display.withScale(new BigDecimal(inValue), inScale), is(expected));
    }
}