package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

import lombok.Getter;
import pl.srw.billcalculator.pojo.ITauronPrices;

@SuppressWarnings("FieldCanBeLocal")
@Getter
public class TauronG12CalculatedBill extends TauronCalculatedBill {

    private final int dayConsumption;
    private final int nightConsumption;
    private final int dayConsumptionFromJuly16;
    private final int nightConsumptionFromJuly16;

    private final BigDecimal energiaElektrycznaDayNetCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaDayNetCharge;
    private final BigDecimal energiaElektrycznaNightNetCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaNightNetCharge;
    private final BigDecimal oplataOzeDayNetCharge;
    private final BigDecimal oplataOzeNightNetCharge;

    private final BigDecimal energiaElektrycznaDayVatCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaDayVatCharge;
    private final BigDecimal energiaElektrycznaNightVatCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaNightVatCharge;
    private final BigDecimal oplataOzeDayVatCharge;
    private final BigDecimal oplataOzeNightVatCharge;

    public TauronG12CalculatedBill(final int readingDayFrom, final int readingDayTo,
                                   final int readingNightFrom, final int readingNightTo,
                                   final String dateFrom, final String dateTo, final ITauronPrices prices) {
        super(dateFrom, dateTo, prices.getOplataAbonamentowa(), prices.getOplataPrzejsciowa(), prices.getOplataDystrybucyjnaStala());
        dayConsumption = readingDayTo - readingDayFrom;
        nightConsumption = readingNightTo - readingNightFrom;
        dayConsumptionFromJuly16 = countConsumptionPartFromJuly16(dateFrom, dateTo, dayConsumption);
        nightConsumptionFromJuly16 = countConsumptionPartFromJuly16(dateFrom, dateTo, nightConsumption);

        energiaElektrycznaDayNetCharge = countNetAndAddToSum(prices.getEnergiaElektrycznaCzynnaDzien(), dayConsumption);
        oplataDystrybucyjnaZmiennaDayNetCharge = countNetAndAddToSum(prices.getOplataDystrybucyjnaZmiennaDzien(), dayConsumption);
        energiaElektrycznaNightNetCharge = countNetAndAddToSum(prices.getEnergiaElektrycznaCzynnaNoc(), nightConsumption);
        oplataDystrybucyjnaZmiennaNightNetCharge = countNetAndAddToSum(prices.getOplataDystrybucyjnaZmiennaNoc(), nightConsumption);
        oplataOzeDayNetCharge = countNetAndAddToSum(prices.getOplataOze(), dayConsumptionFromJuly16);
        oplataOzeNightNetCharge = countNetAndAddToSum(prices.getOplataOze(), nightConsumptionFromJuly16);

        energiaElektrycznaDayVatCharge = countVatAndAddToSum(energiaElektrycznaDayNetCharge);
        oplataDystrybucyjnaZmiennaDayVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaDayNetCharge);
        energiaElektrycznaNightVatCharge = countVatAndAddToSum(energiaElektrycznaNightNetCharge);
        oplataDystrybucyjnaZmiennaNightVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaNightNetCharge);
        oplataOzeDayVatCharge = countVatAndAddToSum(oplataOzeDayNetCharge);
        oplataOzeNightVatCharge = countVatAndAddToSum(oplataOzeNightNetCharge);
    }

    @Override
    public int getTotalConsumption() {
        return dayConsumption + nightConsumption;
    }

    @Override
    public BigDecimal getSellNetCharge() {
        return round(energiaElektrycznaDayNetCharge).add(round(energiaElektrycznaNightNetCharge));
    }

    @Override
    public BigDecimal getSellVatCharge() {
        return round(energiaElektrycznaDayVatCharge).add(round(energiaElektrycznaNightVatCharge));
    }
}
