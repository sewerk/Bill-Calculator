package pl.srw.billcalculator.bill.calculation

import pl.srw.billcalculator.pojo.IPgePrices

class PgeG11CalculatedBill(readingFrom: Int, readingTo: Int, dateFrom: String, dateTo: String, prices: IPgePrices)
    : CalculatedEnergyBill(dateFrom, dateTo, prices.oplataAbonamentowa, prices.oplataPrzejsciowa, prices.oplataStalaZaPrzesyl) {

    override val totalConsumption = readingTo - readingFrom
    val consumptionFromJuly16 = countConsumptionPartFromJuly16(dateFrom, dateTo, totalConsumption)

    val zaEnergieCzynnaNetCharge = countNetAndAddToSum(prices.zaEnergieCzynna, totalConsumption)
    val skladnikJakosciowyNetCharge = countNetAndAddToSum(prices.skladnikJakosciowy, totalConsumption)
    val oplataSieciowaNetCharge = countNetAndAddToSum(prices.oplataSieciowa, totalConsumption)
    val oplataOzeNetCharge = countNetAndAddToSum(prices.oplataOze, (consumptionFromJuly16 * 0.001).toString())

    val zaEnergieCzynnaVatCharge = countVatAndAddToSum(zaEnergieCzynnaNetCharge)
    val skladnikJakosciowyVatCharge = countVatAndAddToSum(skladnikJakosciowyNetCharge)
    val oplataSieciowaVatCharge = countVatAndAddToSum(oplataSieciowaNetCharge)
    val oplataOzeVatCharge = countVatAndAddToSum(oplataOzeNetCharge)
}
