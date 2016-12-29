package pl.srw.billcalculator.bill.calculation

import pl.srw.billcalculator.pojo.IPgePrices

class PgeG11CalculatedBill(readingFrom: Int, readingTo: Int, dateFrom: String, dateTo: String, prices: IPgePrices)
    : CalculatedEnergyBill(dateFrom, dateTo, prices.oplataAbonamentowa, prices.oplataPrzejsciowa, prices.oplataStalaZaPrzesyl) {

    override val totalConsumption = readingTo - readingFrom

    val zaEnergieCzynnaNetCharge = countNetAndAddToSum(prices.zaEnergieCzynna, totalConsumption)
    val skladnikJakosciowyNetCharge = countNetAndAddToSum(prices.skladnikJakosciowy, totalConsumption)
    val oplataSieciowaNetCharge = countNetAndAddToSum(prices.oplataSieciowa, totalConsumption)

    val zaEnergieCzynnaVatCharge = countVatAndAddToSum(zaEnergieCzynnaNetCharge)
    val skladnikJakosciowyVatCharge = countVatAndAddToSum(skladnikJakosciowyNetCharge)
    val oplataSieciowaVatCharge = countVatAndAddToSum(oplataSieciowaNetCharge)
}
