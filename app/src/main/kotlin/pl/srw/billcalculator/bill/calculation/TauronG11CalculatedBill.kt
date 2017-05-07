package pl.srw.billcalculator.bill.calculation

import org.threeten.bp.LocalDate
import pl.srw.billcalculator.pojo.ITauronPrices

class TauronG11CalculatedBill(readingFrom: Int, readingTo: Int, dateFrom: LocalDate, dateTo: LocalDate, prices: ITauronPrices)
    : TauronCalculatedBill(dateFrom, dateTo, prices.oplataAbonamentowa, prices.oplataPrzejsciowa, prices.oplataDystrybucyjnaStala) {

    override val totalConsumption = readingTo - readingFrom
    val consumptionFromJuly16 = countConsumptionPartFromJuly16(dateFrom, dateTo, totalConsumption)

    val energiaElektrycznaNetCharge = countNetAndAddToSum(prices.energiaElektrycznaCzynna, totalConsumption)
    val oplataDystrybucyjnaZmiennaNetCharge = countNetAndAddToSum(prices.oplataDystrybucyjnaZmienna, totalConsumption)
    val oplataOzeNetCharge = countNetAndAddToSum(prices.oplataOze, consumptionFromJuly16)

    val energiaElektrycznaVatCharge = countVatAndAddToSum(energiaElektrycznaNetCharge)
    val oplataDystrybucyjnaZmiennaVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaNetCharge)
    val oplataOzeVatCharge = countVatAndAddToSum(oplataOzeNetCharge)

    override val sellNetCharge = energiaElektrycznaNetCharge.round()

    override val sellVatCharge = energiaElektrycznaVatCharge.round()
}