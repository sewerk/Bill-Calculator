package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

import lombok.Getter;
import pl.srw.billcalculator.pojo.ITauronPrices;

/**
 * Created by kseweryn on 02.06.15.
 */
@SuppressWarnings("FieldCanBeLocal")
@Getter
public class TauronG12CalculatedBill extends CalculatedEnergyBill {

    private final int dayConsumption;
    private final int nightConsumption;

    private final BigDecimal energiaElektrycznaDayNetCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaDayNetCharge;
    private final BigDecimal energiaElektrycznaNightNetCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaNightNetCharge;

    private final BigDecimal energiaElektrycznaDayVatCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaDayVatCharge;
    private final BigDecimal energiaElektrycznaNightVatCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaNightVatCharge;
    
    public TauronG12CalculatedBill(final int readingDayFrom, final int readingDayTo,
                                   final int readingNightFrom, final int readingNightTo,
                                   final String dateFrom, final String dateTo, final ITauronPrices prices) {
        super(dateFrom, dateTo, prices.getOplataAbonamentowa(), prices.getOplataPrzejsciowa(), prices.getOplataDystrybucyjnaStala());
        dayConsumption = readingDayTo - readingDayFrom;
        nightConsumption = readingNightTo - readingNightFrom;

        energiaElektrycznaDayNetCharge = multiplyAndAddToSum(prices.getEnergiaElektrycznaCzynnaDzien(), dayConsumption);
        oplataDystrybucyjnaZmiennaDayNetCharge = multiplyAndAddToSum(prices.getOplataDystrybucyjnaZmiennaDzien(), dayConsumption);
        energiaElektrycznaNightNetCharge = multiplyAndAddToSum(prices.getEnergiaElektrycznaCzynnaNoc(), nightConsumption);
        oplataDystrybucyjnaZmiennaNightNetCharge = multiplyAndAddToSum(prices.getOplataDystrybucyjnaZmiennaNoc(), nightConsumption);

        energiaElektrycznaDayVatCharge = multiplyVatAndAddToSum(energiaElektrycznaDayNetCharge);
        oplataDystrybucyjnaZmiennaDayVatCharge = multiplyVatAndAddToSum(oplataDystrybucyjnaZmiennaDayNetCharge);
        energiaElektrycznaNightVatCharge = multiplyVatAndAddToSum(energiaElektrycznaNightNetCharge);
        oplataDystrybucyjnaZmiennaNightVatCharge = multiplyVatAndAddToSum(oplataDystrybucyjnaZmiennaNightNetCharge);
    }

    @Override
    public int getTotalConsumption() {
        return dayConsumption + nightConsumption;
    }

}
