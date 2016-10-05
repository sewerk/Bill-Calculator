package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

import lombok.Getter;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
@SuppressWarnings("FieldCanBeLocal")
@Getter
public abstract class CalculatedEnergyBill extends CalculatedBill {

    private static final BigDecimal EXCISE = new BigDecimal("0.02");
    public static final String OZE_NET_PRICE = "0.00251";

    private final int totalConsumption;

    private final BigDecimal oplataAbonamentowaNetCharge;
    private final BigDecimal oplataPrzejsciowaNetCharge;
    private final BigDecimal oplataDystrybucyjnaStalaNetCharge;
    private final BigDecimal oplataOzeNetCharge;

    private final BigDecimal oplataAbonamentowaVatCharge;
    private final BigDecimal oplataPrzejsciowaVatCharge;
    private final BigDecimal oplataDystrybucyjnaStalaVatCharge;
    private final BigDecimal oplataOzeVatCharge;

    protected CalculatedEnergyBill(final String dateFrom, final String dateTo, int totalConsumption,
                                   String oplataAbonamentowa, String oplataPrzejsciowa, String oplataStalaZaPrzesyl) {
        super(false, dateFrom, dateTo);
        this.totalConsumption = totalConsumption;

        oplataAbonamentowaNetCharge = countNetAndAddToSum(oplataAbonamentowa, getMonthCount());
        oplataPrzejsciowaNetCharge = countNetAndAddToSum(oplataPrzejsciowa, getMonthCount());
        oplataDystrybucyjnaStalaNetCharge = countNetAndAddToSum(oplataStalaZaPrzesyl, getMonthCount());
        oplataOzeNetCharge = countNetAndAddToSum(OZE_NET_PRICE, getConsumptionFromJuly16(dateFrom, dateTo));

        oplataAbonamentowaVatCharge = countVatAndAddToSum(oplataAbonamentowaNetCharge);
        oplataPrzejsciowaVatCharge = countVatAndAddToSum(oplataPrzejsciowaNetCharge);
        oplataDystrybucyjnaStalaVatCharge = countVatAndAddToSum(oplataDystrybucyjnaStalaNetCharge);
        oplataOzeVatCharge = countVatAndAddToSum(oplataOzeNetCharge);
    }

    private int getConsumptionFromJuly16(String dateFrom, String dateTo) {
        int daysFromJuly16 = Dates.countDaysFromJuly16(dateFrom, dateTo);
        int periodInDays = Dates.countDays(dateFrom, dateTo);
        return new BigDecimal(totalConsumption * daysFromJuly16)
                .divideToIntegralValue(BigDecimal.valueOf(periodInDays))
                .intValue();
    }

    public BigDecimal getExcise() {
        return EXCISE.multiply(new BigDecimal(totalConsumption));
    }
}
