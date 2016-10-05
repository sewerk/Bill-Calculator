package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

import lombok.Getter;
import pl.srw.billcalculator.pojo.ITauronPrices;

/**
 * Created by kseweryn on 02.06.15.
 */
@SuppressWarnings("FieldCanBeLocal")
@Getter
public class TauronG11CalculatedBill extends TauronCalculatedBill {

    private final int consumption;

    private final BigDecimal energiaElektrycznaNetCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaNetCharge;

    private final BigDecimal energiaElektrycznaVatCharge;
    private final BigDecimal oplataDystrybucyjnaZmiennaVatCharge;

    public TauronG11CalculatedBill(final int readingFrom, final int readingTo, final String dateFrom, final String dateTo, final ITauronPrices prices) {
        super(dateFrom, dateTo, readingTo - readingFrom,
                prices.getOplataAbonamentowa(), prices.getOplataPrzejsciowa(), prices.getOplataDystrybucyjnaStala(), prices.getOplataOze());
        consumption = readingTo - readingFrom;

        energiaElektrycznaNetCharge = countNetAndAddToSum(prices.getEnergiaElektrycznaCzynna(), consumption);
        oplataDystrybucyjnaZmiennaNetCharge = countNetAndAddToSum(prices.getOplataDystrybucyjnaZmienna(), consumption);

        energiaElektrycznaVatCharge = countVatAndAddToSum(energiaElektrycznaNetCharge);
        oplataDystrybucyjnaZmiennaVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaNetCharge);
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
