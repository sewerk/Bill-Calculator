package pl.srw.billcalculator.bill.calculation

import org.threeten.bp.LocalDate
import pl.srw.billcalculator.pojo.IPgnigPrices
import pl.srw.billcalculator.util.Dates
import java.math.BigDecimal
import java.math.RoundingMode

class PgnigCalculatedBill(readingFrom: Int, readingTo: Int, dateFrom: LocalDate, dateTo: LocalDate, prices: IPgnigPrices)
    : CalculatedBill(true, dateFrom, dateTo) {

    val monthCountExact = Dates.countMonth(dateFrom, dateTo)

    val consumptionM3 = readingTo - readingFrom
    val consumptionKWh = BigDecimal(consumptionM3).multiply(BigDecimal(prices.wspolczynnikKonwersji))
            .setScale(0, RoundingMode.HALF_UP).toBigInteger()

    val oplataAbonamentowaNetCharge = countNetAndAddToSum(prices.oplataAbonamentowa, monthCount)
    val paliwoGazoweNetCharge = countNetAndAddToSum(prices.paliwoGazowe, consumptionKWh)
    val dystrybucyjnaStalaNetCharge = countNetAndAddToSum(prices.dystrybucyjnaStala, monthCountExact)
    val dystrybucyjnaZmiennaNetCharge = countNetAndAddToSum(prices.dystrybucyjnaZmienna, consumptionKWh)

    val oplataAbonamentowaVatCharge = countVatAndAddToSum(oplataAbonamentowaNetCharge)
    val paliwoGazoweVatCharge = countVatAndAddToSum(paliwoGazoweNetCharge)
    val dystrybucyjnaStalaVatCharge = countVatAndAddToSum(dystrybucyjnaStalaNetCharge)
    val dystrybucyjnaZmiennaVatCharge = countVatAndAddToSum(dystrybucyjnaZmiennaNetCharge)
}