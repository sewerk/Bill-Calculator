package pl.srw.billcalculator.calculation;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Getter;
import pl.srw.billcalculator.pojo.IPgnigPrices;

/**
 * Created by Kamil Seweryn.
 */
@Getter
public class PgnigCalculatedBill extends CalculatedBill {

    private final int consumptionM3;
    private final int consumptionKWh;

    private final BigDecimal oplataAbonamentowaNetCharge;
    private final BigDecimal paliwoGazoweNetCharge;
    private final BigDecimal dystrybucyjnaStalaNetCharge;
    private final BigDecimal dystrybucyjnaZmiennaNetCharge;

    private final BigDecimal oplataAbonamentowaVatCharge;
    private final BigDecimal paliwoGazoweVatCharge;
    private final BigDecimal dystrybucyjnaStalaVatCharge;
    private final BigDecimal dystrybucyjnaZmiennaVatCharge;

    public PgnigCalculatedBill(final int readingFrom, final int readingTo, final String dateFrom, final String dateTo, final IPgnigPrices prices) {
        super(dateFrom, dateTo);
        consumptionM3 = readingTo - readingFrom;
        consumptionKWh = new BigDecimal(consumptionM3).multiply(new BigDecimal(prices.getWspolczynnikKonwersji()))
                .setScale(0, RoundingMode.HALF_UP).intValue();

        oplataAbonamentowaNetCharge = multiplyAndAddToSum(prices.getOplataAbonamentowa(), getMonthCount());
        paliwoGazoweNetCharge = multiplyAndAddToSum(prices.getPaliwoGazowe(), consumptionKWh);
        dystrybucyjnaStalaNetCharge = multiplyAndAddToSum(prices.getDystrybucyjnaStala(), getMonthCount());
        dystrybucyjnaZmiennaNetCharge = multiplyAndAddToSum(prices.getDystrybucyjnaZmienna(), consumptionKWh);

        oplataAbonamentowaVatCharge = multiplyVatAndAddToSum(oplataAbonamentowaNetCharge);
        paliwoGazoweVatCharge = multiplyVatAndAddToSum(paliwoGazoweNetCharge);
        dystrybucyjnaStalaVatCharge = multiplyVatAndAddToSum(dystrybucyjnaStalaNetCharge);
        dystrybucyjnaZmiennaVatCharge = multiplyVatAndAddToSum(dystrybucyjnaZmiennaNetCharge);

    }
}
