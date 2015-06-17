package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

import lombok.Getter;
import pl.srw.billcalculator.pojo.IPgePrices;

/**
 * Created by Kamil Seweryn.
 */
@SuppressWarnings("FieldCanBeLocal")
@Getter
public class PgeG12CalculatedBill extends CalculatedEnergyBill {

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
        super(dateFrom, dateTo, prices.getOplataAbonamentowa(), prices.getOplataPrzejsciowa(), prices.getOplataStalaZaPrzesyl());
        dayConsumption = readingDayTo - readingDayFrom;
        nightConsumption = readingNightTo - readingNightFrom;

        zaEnergieCzynnaDayNetCharge = countNetAndAddToSum(prices.getZaEnergieCzynnaDzien(), dayConsumption);
        skladnikJakosciowyDayNetCharge = countNetAndAddToSum(prices.getSkladnikJakosciowy(), dayConsumption);
        oplataSieciowaDayNetCharge = countNetAndAddToSum(prices.getOplataSieciowaDzien(), dayConsumption);
        zaEnergieCzynnaNightNetCharge = countNetAndAddToSum(prices.getZaEnergieCzynnaNoc(), nightConsumption);
        skladnikJakosciowyNightNetCharge = countNetAndAddToSum(prices.getSkladnikJakosciowy(), nightConsumption);
        oplataSieciowaNightNetCharge = countNetAndAddToSum(prices.getOplataSieciowaNoc(), nightConsumption);

        zaEnergieCzynnaDayVatCharge = countVatAndAddToSum(zaEnergieCzynnaDayNetCharge);
        skladnikJakosciowyDayVatCharge = countVatAndAddToSum(skladnikJakosciowyDayNetCharge);
        oplataSieciowaDayVatCharge = countVatAndAddToSum(oplataSieciowaDayNetCharge);
        zaEnergieCzynnaNightVatCharge = countVatAndAddToSum(zaEnergieCzynnaNightNetCharge);
        skladnikJakosciowyNightVatCharge = countVatAndAddToSum(skladnikJakosciowyNightNetCharge);
        oplataSieciowaNightVatCharge = countVatAndAddToSum(oplataSieciowaNightNetCharge);
    }

    @Override
    public int getTotalConsumption() {
        return dayConsumption + nightConsumption;
    }
}
