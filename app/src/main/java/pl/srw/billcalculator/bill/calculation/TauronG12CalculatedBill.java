package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

import lombok.Getter;
import pl.srw.billcalculator.pojo.ITauronPrices;

/**
 * Created by kseweryn on 02.06.15.
 */
@SuppressWarnings("FieldCanBeLocal")
@Getter
public class TauronG12CalculatedBill extends TauronCalculatedBill {

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

        energiaElektrycznaDayNetCharge = countNetAndAddToSum(prices.getEnergiaElektrycznaCzynnaDzien(), dayConsumption);
        oplataDystrybucyjnaZmiennaDayNetCharge = countNetAndAddToSum(prices.getOplataDystrybucyjnaZmiennaDzien(), dayConsumption);
        energiaElektrycznaNightNetCharge = countNetAndAddToSum(prices.getEnergiaElektrycznaCzynnaNoc(), nightConsumption);
        oplataDystrybucyjnaZmiennaNightNetCharge = countNetAndAddToSum(prices.getOplataDystrybucyjnaZmiennaNoc(), nightConsumption);

        energiaElektrycznaDayVatCharge = countVatAndAddToSum(energiaElektrycznaDayNetCharge);
        oplataDystrybucyjnaZmiennaDayVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaDayNetCharge);
        energiaElektrycznaNightVatCharge = countVatAndAddToSum(energiaElektrycznaNightNetCharge);
        oplataDystrybucyjnaZmiennaNightVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaNightNetCharge);
    }

    @Override
    public int getTotalConsumption() {
        return dayConsumption + nightConsumption;
    }

    @Override
    public BigDecimal getSellNetCharge() {
        return sum(energiaElektrycznaDayNetCharge, energiaElektrycznaNightNetCharge);
    }

    @Override
    public BigDecimal getSellVatCharge() {
        return sum(energiaElektrycznaDayVatCharge, energiaElektrycznaNightVatCharge);
    }

    @Override
    public BigDecimal getDistributeNetCharge() {
        return sum(getAbsDistributeNetCharge(),
                oplataDystrybucyjnaZmiennaDayNetCharge,
                oplataDystrybucyjnaZmiennaNightNetCharge);
    }

    @Override
    public BigDecimal getDistributeVatCharge() {
        return sum(getAbsDistributeVatCharge(),
                oplataDystrybucyjnaZmiennaDayVatCharge,
                oplataDystrybucyjnaZmiennaNightVatCharge);
    }
}
