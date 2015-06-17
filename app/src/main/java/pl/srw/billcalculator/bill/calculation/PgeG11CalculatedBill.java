package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

import lombok.Getter;
import pl.srw.billcalculator.pojo.IPgePrices;

/**
 * Created by Kamil Seweryn.
 */
@SuppressWarnings("FieldCanBeLocal")
@Getter
public class PgeG11CalculatedBill extends CalculatedEnergyBill {

    private final int consumption;

    private final BigDecimal zaEnergieCzynnaNetCharge;
    private final BigDecimal skladnikJakosciowyNetCharge;
    private final BigDecimal oplataSieciowaNetCharge;

    private final BigDecimal zaEnergieCzynnaVatCharge;
    private final BigDecimal skladnikJakosciowyVatCharge;
    private final BigDecimal oplataSieciowaVatCharge;

    public PgeG11CalculatedBill(final int readingFrom, final int readingTo, final String dateFrom, final String dateTo, final IPgePrices prices) {
        super(dateFrom, dateTo, prices.getOplataAbonamentowa(), prices.getOplataPrzejsciowa(), prices.getOplataStalaZaPrzesyl());
        consumption = readingTo - readingFrom;

        zaEnergieCzynnaNetCharge = countNetAndAddToSum(prices.getZaEnergieCzynna(), consumption);
        skladnikJakosciowyNetCharge = countNetAndAddToSum(prices.getSkladnikJakosciowy(), consumption);
        oplataSieciowaNetCharge = countNetAndAddToSum(prices.getOplataSieciowa(), consumption);

        zaEnergieCzynnaVatCharge = countVatAndAddToSum(zaEnergieCzynnaNetCharge);
        skladnikJakosciowyVatCharge = countVatAndAddToSum(skladnikJakosciowyNetCharge);
        oplataSieciowaVatCharge = countVatAndAddToSum(oplataSieciowaNetCharge);
    }

    @Override
    public int getTotalConsumption() {
        return consumption;
    }
}
