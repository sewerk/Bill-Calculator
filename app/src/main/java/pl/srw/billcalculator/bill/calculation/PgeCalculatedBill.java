package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

import lombok.Getter;
import pl.srw.billcalculator.pojo.IPgePrices;

/**
 * Created by Kamil Seweryn.
 */
@Getter
public abstract class PgeCalculatedBill extends CalculatedBill {

    public static final BigDecimal EXCISE = new BigDecimal("0.02");

    private final BigDecimal oplataAbonamentowaNetCharge;
    private final BigDecimal oplataPrzejsciowaNetCharge;
    private final BigDecimal oplataStalaZaPrzesylNetCharge;

    private final BigDecimal oplataAbonamentowaVatCharge;
    private final BigDecimal oplataPrzejsciowaVatCharge;
    private final BigDecimal oplataStalaZaPrzesylVatCharge;

    public PgeCalculatedBill(final String dateFrom, final String dateTo, final IPgePrices prices) {
        super(dateFrom, dateTo);

        oplataAbonamentowaNetCharge = multiplyAndAddToSum(prices.getOplataAbonamentowa(), getMonthCount());
        oplataPrzejsciowaNetCharge = multiplyAndAddToSum(prices.getOplataPrzejsciowa(), getMonthCount());
        oplataStalaZaPrzesylNetCharge = multiplyAndAddToSum(prices.getOplataStalaZaPrzesyl(), getMonthCount());

        oplataAbonamentowaVatCharge = multiplyVatAndAddToSum(oplataAbonamentowaNetCharge);
        oplataPrzejsciowaVatCharge = multiplyVatAndAddToSum(oplataPrzejsciowaNetCharge);
        oplataStalaZaPrzesylVatCharge = multiplyVatAndAddToSum(oplataStalaZaPrzesylNetCharge);
    }

    public BigDecimal getExcise() {
        return EXCISE.multiply(new BigDecimal(getTotalConsumption()));
    }

    public abstract int getTotalConsumption();
}
