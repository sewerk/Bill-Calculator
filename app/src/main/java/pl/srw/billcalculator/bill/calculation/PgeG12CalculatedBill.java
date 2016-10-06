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
    private final int dayConsumptionFromJuly16;
    private final int nightConsumptionFromJuly16;

    private final BigDecimal zaEnergieCzynnaDayNetCharge;
    private final BigDecimal skladnikJakosciowyDayNetCharge;
    private final BigDecimal oplataSieciowaDayNetCharge;
    private final BigDecimal zaEnergieCzynnaNightNetCharge;
    private final BigDecimal skladnikJakosciowyNightNetCharge;
    private final BigDecimal oplataSieciowaNightNetCharge;
    private final BigDecimal oplataOzeDayNetCharge;
    private final BigDecimal oplataOzeNightNetCharge;

    private final BigDecimal zaEnergieCzynnaDayVatCharge;
    private final BigDecimal skladnikJakosciowyDayVatCharge;
    private final BigDecimal oplataSieciowaDayVatCharge;
    private final BigDecimal zaEnergieCzynnaNightVatCharge;
    private final BigDecimal skladnikJakosciowyNightVatCharge;
    private final BigDecimal oplataSieciowaNightVatCharge;
    private final BigDecimal oplataOzeDayVatCharge;
    private final BigDecimal oplataOzeNightVatCharge;

    public PgeG12CalculatedBill(final int readingDayFrom, final int readingDayTo,
                                final int readingNightFrom, final int readingNightTo,
                                final String dateFrom, final String dateTo, final IPgePrices prices) {
        super(dateFrom, dateTo, prices.getOplataAbonamentowa(), prices.getOplataPrzejsciowa(), prices.getOplataStalaZaPrzesyl());
        dayConsumption = readingDayTo - readingDayFrom;
        nightConsumption = readingNightTo - readingNightFrom;
        dayConsumptionFromJuly16 = countConsumptionPartFromJuly16(dateFrom, dateTo, dayConsumption);
        nightConsumptionFromJuly16 = countConsumptionPartFromJuly16(dateFrom, dateTo, nightConsumption);

        zaEnergieCzynnaDayNetCharge = countNetAndAddToSum(prices.getZaEnergieCzynnaDzien(), dayConsumption);
        skladnikJakosciowyDayNetCharge = countNetAndAddToSum(prices.getSkladnikJakosciowy(), dayConsumption);
        oplataSieciowaDayNetCharge = countNetAndAddToSum(prices.getOplataSieciowaDzien(), dayConsumption);
        zaEnergieCzynnaNightNetCharge = countNetAndAddToSum(prices.getZaEnergieCzynnaNoc(), nightConsumption);
        skladnikJakosciowyNightNetCharge = countNetAndAddToSum(prices.getSkladnikJakosciowy(), nightConsumption);
        oplataSieciowaNightNetCharge = countNetAndAddToSum(prices.getOplataSieciowaNoc(), nightConsumption);
        oplataOzeDayNetCharge = countNetAndAddToSum(prices.getOplataOze(), dayConsumptionFromJuly16);
        oplataOzeNightNetCharge = countNetAndAddToSum(prices.getOplataOze(), nightConsumptionFromJuly16);

        zaEnergieCzynnaDayVatCharge = countVatAndAddToSum(zaEnergieCzynnaDayNetCharge);
        skladnikJakosciowyDayVatCharge = countVatAndAddToSum(skladnikJakosciowyDayNetCharge);
        oplataSieciowaDayVatCharge = countVatAndAddToSum(oplataSieciowaDayNetCharge);
        zaEnergieCzynnaNightVatCharge = countVatAndAddToSum(zaEnergieCzynnaNightNetCharge);
        skladnikJakosciowyNightVatCharge = countVatAndAddToSum(skladnikJakosciowyNightNetCharge);
        oplataSieciowaNightVatCharge = countVatAndAddToSum(oplataSieciowaNightNetCharge);
        oplataOzeDayVatCharge = countVatAndAddToSum(oplataOzeDayNetCharge);
        oplataOzeNightVatCharge = countVatAndAddToSum(oplataOzeNightNetCharge);
    }

    @Override
    public int getTotalConsumption() {
        return dayConsumption + nightConsumption;
    }
}
