package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

import lombok.Getter;
import pl.srw.billcalculator.pojo.ITauronPrices;

@SuppressWarnings("FieldCanBeLocal")
@Getter
public class TauronG11CalculatedBill extends TauronCalculatedBill {

    private final int consumption;
    private final int consumptionFromJuly16;

    private final BigDecimal energiaElektrycznaNetCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaNetCharge;
    private final BigDecimal oplataOzeNetCharge;

    private final BigDecimal energiaElektrycznaVatCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaVatCharge;
    private final BigDecimal oplataOzeVatCharge;

    public TauronG11CalculatedBill(final int readingFrom, final int readingTo, final String dateFrom, final String dateTo, final ITauronPrices prices) {
        super(dateFrom, dateTo, prices.getOplataAbonamentowa(), prices.getOplataPrzejsciowa(), prices.getOplataDystrybucyjnaStala());
        consumption = readingTo - readingFrom;
        consumptionFromJuly16 = countConsumptionPartFromJuly16(dateFrom, dateTo, consumption);

        energiaElektrycznaNetCharge = countNetAndAddToSum(prices.getEnergiaElektrycznaCzynna(), consumption);
        oplataDystrybucyjnaZmiennaNetCharge = countNetAndAddToSum(prices.getOplataDystrybucyjnaZmienna(), consumption);
        oplataOzeNetCharge = countNetAndAddToSum(prices.getOplataOze(), consumptionFromJuly16);

        energiaElektrycznaVatCharge = countVatAndAddToSum(energiaElektrycznaNetCharge);
        oplataDystrybucyjnaZmiennaVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaNetCharge);
        oplataOzeVatCharge = countVatAndAddToSum(oplataOzeNetCharge);
    }

    @Override
    public int getTotalConsumption() {
        return consumption;
    }

    @Override
    public BigDecimal getSellNetCharge() {
        return round(energiaElektrycznaNetCharge);
    }

    @Override
    public BigDecimal getSellVatCharge() {
        return round(energiaElektrycznaVatCharge);
    }
}
