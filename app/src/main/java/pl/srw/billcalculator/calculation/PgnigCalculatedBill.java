package pl.srw.billcalculator.calculation;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Getter;
import pl.srw.billcalculator.pojo.IPgnigPrices;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
@Getter
public class PgnigCalculatedBill {

    public static final BigDecimal VAT = new BigDecimal("0.23");

    private BigDecimal netChargeSum = BigDecimal.ZERO;
    private BigDecimal grossChargeSum = BigDecimal.ZERO;
    private BigDecimal vatChargeSum = BigDecimal.ZERO;

    private int consumptionM3;
    private int consumptionKWh;
    private int monthCount;

    private BigDecimal oplataAbonamentowaNetCharge;
    private BigDecimal paliwoGazoweNetCharge;
    private BigDecimal dystrybucyjnaStalaNetCharge;
    private BigDecimal dystrybucyjnaZmiennaNetCharge;

    private BigDecimal oplataAbonamentowaVatCharge;
    private BigDecimal paliwoGazoweVatCharge;
    private BigDecimal dystrybucyjnaStalaVatCharge;
    private BigDecimal dystrybucyjnaZmiennaVatCharge;

    public PgnigCalculatedBill(final int readingFrom, final int readingTo, final String dateFrom, final String dateTo, final IPgnigPrices prices) {
        consumptionM3 = readingTo - readingFrom;
        consumptionKWh = new BigDecimal(consumptionM3).multiply(new BigDecimal(prices.getWspolczynnikKonwersji()))
                .setScale(0, RoundingMode.HALF_UP).intValue();
        monthCount = Dates.countMonth(dateFrom, dateTo);

        oplataAbonamentowaNetCharge = multiplyAndAddToSum(prices.getOplataAbonamentowa(), monthCount);
        paliwoGazoweNetCharge = multiplyAndAddToSum(prices.getPaliwoGazowe(), consumptionKWh);
        dystrybucyjnaStalaNetCharge = multiplyAndAddToSum(prices.getDystrybucyjnaStala(), monthCount);
        dystrybucyjnaZmiennaNetCharge = multiplyAndAddToSum(prices.getDystrybucyjnaZmienna(), consumptionKWh);

        oplataAbonamentowaVatCharge = multiplyVatAndAddToSum(oplataAbonamentowaNetCharge);
        paliwoGazoweVatCharge = multiplyVatAndAddToSum(paliwoGazoweNetCharge);
        dystrybucyjnaStalaVatCharge = multiplyVatAndAddToSum(dystrybucyjnaStalaNetCharge);
        dystrybucyjnaZmiennaVatCharge = multiplyVatAndAddToSum(dystrybucyjnaZmiennaNetCharge);

        grossChargeSum = grossChargeSum.add(netChargeSum).add(vatChargeSum);
    }

    private BigDecimal multiplyAndAddToSum(final String oplataAbonamentowa, final int count) {
        BigDecimal netCharge = new BigDecimal(oplataAbonamentowa).multiply(new BigDecimal(count));
        netChargeSum = netChargeSum.add(netCharge.setScale(2, RoundingMode.HALF_UP));
        return netCharge;
    }

    private BigDecimal multiplyVatAndAddToSum(final BigDecimal netCharge) {
        BigDecimal vatCharge = netCharge.multiply(VAT);
        vatChargeSum = vatChargeSum.add(vatCharge.setScale(2, RoundingMode.HALF_UP));
        return vatCharge;
    }
}
