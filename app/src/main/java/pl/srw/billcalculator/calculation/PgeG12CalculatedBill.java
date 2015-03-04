package pl.srw.billcalculator.calculation;

import java.math.BigDecimal;

import lombok.Getter;
import pl.srw.billcalculator.pojo.IPgePrices;

/**
 * Created by Kamil Seweryn.
 */
@Getter
public class PgeG12CalculatedBill extends PgeCalculatedBill {

    private final int dayConsumption;
    private final int nightConsumption;

    private final BigDecimal zaEnergieCzynnaDayNetCharge;
    private final BigDecimal skladnikJakosciowyDayNetCharge;
    private final BigDecimal oplataSieciowaDayNetCharge;
    private final BigDecimal zaEnergieCzynnaNightNetCharge;
    private final BigDecimal skladnikJakosciowyNightNetCharge;
    private final BigDecimal oplataSieciowaNightNetCharge;

    private final BigDecimal zaEnergieCzynnaDayVatCharge;
    private final BigDecimal skladnikJakosciowyDayVatCharge;
    private final BigDecimal oplataSieciowaDayVatCharge;
    private final BigDecimal zaEnergieCzynnaNightVatCharge;
    private final BigDecimal skladnikJakosciowyNightVatCharge;
    private final BigDecimal oplataSieciowaNightVatCharge;


    public PgeG12CalculatedBill(final int readingDayFrom, final int readingDayTo,
                                final int readingNightFrom, final int readingNightTo,
                                final String dateFrom, final String dateTo, final IPgePrices prices) {
        super(dateFrom, dateTo, prices);
        dayConsumption = readingDayTo - readingDayFrom;
        nightConsumption = readingNightTo - readingNightFrom;

        zaEnergieCzynnaDayNetCharge = multiplyAndAddToSum(prices.getZaEnergieCzynnaDzien(), dayConsumption);
        skladnikJakosciowyDayNetCharge = multiplyAndAddToSum(prices.getSkladnikJakosciowy(), dayConsumption);
        oplataSieciowaDayNetCharge = multiplyAndAddToSum(prices.getOplataSieciowaDzien(), dayConsumption);
        zaEnergieCzynnaNightNetCharge = multiplyAndAddToSum(prices.getZaEnergieCzynnaNoc(), nightConsumption);
        skladnikJakosciowyNightNetCharge = multiplyAndAddToSum(prices.getSkladnikJakosciowy(), nightConsumption);
        oplataSieciowaNightNetCharge = multiplyAndAddToSum(prices.getOplataSieciowaNoc(), nightConsumption);

        zaEnergieCzynnaDayVatCharge = multiplyVatAndAddToSum(zaEnergieCzynnaDayNetCharge);
        skladnikJakosciowyDayVatCharge = multiplyVatAndAddToSum(skladnikJakosciowyDayNetCharge);
        oplataSieciowaDayVatCharge = multiplyVatAndAddToSum(oplataSieciowaDayNetCharge);
        zaEnergieCzynnaNightVatCharge = multiplyVatAndAddToSum(zaEnergieCzynnaNightNetCharge);
        skladnikJakosciowyNightVatCharge = multiplyVatAndAddToSum(skladnikJakosciowyNightNetCharge);
        oplataSieciowaNightVatCharge = multiplyVatAndAddToSum(oplataSieciowaNightNetCharge);
    }

    @Override
    public int getTotalConsumption() {
        return dayConsumption + nightConsumption;
    }
}
