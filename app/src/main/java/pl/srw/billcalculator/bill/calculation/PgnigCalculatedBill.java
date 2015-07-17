package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import lombok.Getter;
import pl.srw.billcalculator.pojo.IPgnigPrices;

/**
 * Created by Kamil Seweryn.
 */
@SuppressWarnings("FieldCanBeLocal")
@Getter
public class PgnigCalculatedBill extends CalculatedBill {

    private final int consumptionM3;
    private final BigInteger consumptionKWh;

    private final BigDecimal oplataAbonamentowaNetCharge;
    private final BigDecimal paliwoGazoweNetCharge;
    private final BigDecimal dystrybucyjnaStalaNetCharge;
    private final BigDecimal dystrybucyjnaZmiennaNetCharge;

    private final BigDecimal oplataAbonamentowaVatCharge;
    private final BigDecimal paliwoGazoweVatCharge;
    private final BigDecimal dystrybucyjnaStalaVatCharge;
    private final BigDecimal dystrybucyjnaZmiennaVatCharge;

    public PgnigCalculatedBill(final int readingFrom, final int readingTo, final String dateFrom, final String dateTo, final IPgnigPrices prices) {
        super(true, dateFrom, dateTo);
        consumptionM3 = readingTo - readingFrom;
        consumptionKWh = new BigDecimal(consumptionM3).multiply(new BigDecimal(prices.getWspolczynnikKonwersji()))
                .setScale(0, RoundingMode.HALF_UP).toBigInteger();

        oplataAbonamentowaNetCharge = countNetAndAddToSum(prices.getOplataAbonamentowa(), getMonthCount());
        paliwoGazoweNetCharge = countNetAndAddToSum(prices.getPaliwoGazowe(), consumptionKWh);
        dystrybucyjnaStalaNetCharge = countNetAndAddToSum(prices.getDystrybucyjnaStala(), getMonthCount());
        dystrybucyjnaZmiennaNetCharge = countNetAndAddToSum(prices.getDystrybucyjnaZmienna(), consumptionKWh);

        oplataAbonamentowaVatCharge = countVatAndAddToSum(oplataAbonamentowaNetCharge);
        paliwoGazoweVatCharge = countVatAndAddToSum(paliwoGazoweNetCharge);
        dystrybucyjnaStalaVatCharge = countVatAndAddToSum(dystrybucyjnaStalaNetCharge);
        dystrybucyjnaZmiennaVatCharge = countVatAndAddToSum(dystrybucyjnaZmiennaNetCharge);

    }
}
