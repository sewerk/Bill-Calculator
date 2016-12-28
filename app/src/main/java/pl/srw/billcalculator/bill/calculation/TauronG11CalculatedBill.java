package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

import pl.srw.billcalculator.pojo.ITauronPrices;

public class TauronG11CalculatedBill extends TauronCalculatedBill {

    private final int consumption;

    private final BigDecimal energiaElektrycznaNetCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaNetCharge;

    private final BigDecimal energiaElektrycznaVatCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaVatCharge;

    public TauronG11CalculatedBill(final int readingFrom, final int readingTo, final String dateFrom, final String dateTo, final ITauronPrices prices) {
        super(dateFrom, dateTo, prices.getOplataAbonamentowa(), prices.getOplataPrzejsciowa(), prices.getOplataDystrybucyjnaStala());
        consumption = readingTo - readingFrom;

        energiaElektrycznaNetCharge = countNetAndAddToSum(prices.getEnergiaElektrycznaCzynna(), consumption);
        oplataDystrybucyjnaZmiennaNetCharge = countNetAndAddToSum(prices.getOplataDystrybucyjnaZmienna(), consumption);

        energiaElektrycznaVatCharge = countVatAndAddToSum(energiaElektrycznaNetCharge);
        oplataDystrybucyjnaZmiennaVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaNetCharge);
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
