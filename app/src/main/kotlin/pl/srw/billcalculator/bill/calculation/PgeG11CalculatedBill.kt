package pl.srw.billcalculator.bill.calculation

import pl.srw.billcalculator.pojo.IPgePrices

class PgeG11CalculatedBill(readingFrom: Int, readingTo: Int, dateFrom: String, dateTo: String, prices: IPgePrices)
    : CalculatedEnergyBill(dateFrom, dateTo, prices.oplataAbonamentowa, prices.oplataPrzejsciowa, prices.oplataStalaZaPrzesyl) {

    val consumption = readingTo - readingFrom

    val zaEnergieCzynnaNetCharge = countNetAndAddToSum(prices.zaEnergieCzynna, consumption)
    val skladnikJakosciowyNetCharge = countNetAndAddToSum(prices.skladnikJakosciowy, consumption)
    val oplataSieciowaNetCharge = countNetAndAddToSum(prices.oplataSieciowa, consumption)

    val zaEnergieCzynnaVatCharge = countVatAndAddToSum(zaEnergieCzynnaNetCharge)
    val skladnikJakosciowyVatCharge = countVatAndAddToSum(skladnikJakosciowyNetCharge)
    val oplataSieciowaVatCharge = countVatAndAddToSum(oplataSieciowaNetCharge)

    override val totalConsumption = consumption
}
