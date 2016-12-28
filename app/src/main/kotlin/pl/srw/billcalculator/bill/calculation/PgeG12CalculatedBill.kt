package pl.srw.billcalculator.bill.calculation

import pl.srw.billcalculator.pojo.IPgePrices

class PgeG12CalculatedBill(readingDayFrom: Int, readingDayTo: Int,
                           readingNightFrom: Int, readingNightTo: Int,
                           dateFrom: String, dateTo: String, prices: IPgePrices)
    : CalculatedEnergyBill(dateFrom, dateTo, prices.oplataAbonamentowa, prices.oplataPrzejsciowa, prices.oplataStalaZaPrzesyl) {

    val dayConsumption = readingDayTo - readingDayFrom
    val nightConsumption = readingNightTo - readingNightFrom

    val zaEnergieCzynnaDayNetCharge = countNetAndAddToSum(prices.zaEnergieCzynnaDzien, dayConsumption)
    val skladnikJakosciowyDayNetCharge = countNetAndAddToSum(prices.skladnikJakosciowy, dayConsumption)
    val oplataSieciowaDayNetCharge = countNetAndAddToSum(prices.oplataSieciowaDzien, dayConsumption)
    val zaEnergieCzynnaNightNetCharge = countNetAndAddToSum(prices.zaEnergieCzynnaNoc, nightConsumption)
    val skladnikJakosciowyNightNetCharge = countNetAndAddToSum(prices.skladnikJakosciowy, nightConsumption)
    val oplataSieciowaNightNetCharge = countNetAndAddToSum(prices.oplataSieciowaNoc, nightConsumption)

    val zaEnergieCzynnaDayVatCharge = countVatAndAddToSum(zaEnergieCzynnaDayNetCharge)
    val skladnikJakosciowyDayVatCharge = countVatAndAddToSum(skladnikJakosciowyDayNetCharge)
    val oplataSieciowaDayVatCharge = countVatAndAddToSum(oplataSieciowaDayNetCharge)
    val zaEnergieCzynnaNightVatCharge = countVatAndAddToSum(zaEnergieCzynnaNightNetCharge)
    val skladnikJakosciowyNightVatCharge = countVatAndAddToSum(skladnikJakosciowyNightNetCharge)
    val oplataSieciowaNightVatCharge = countVatAndAddToSum(oplataSieciowaNightNetCharge)

    override val totalConsumption = dayConsumption + nightConsumption
}