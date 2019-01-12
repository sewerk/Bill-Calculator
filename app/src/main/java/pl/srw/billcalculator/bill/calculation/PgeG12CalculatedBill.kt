package pl.srw.billcalculator.bill.calculation

import org.threeten.bp.LocalDate
import pl.srw.billcalculator.pojo.IPgePrices

class PgeG12CalculatedBill(
    readingDayFrom: Int,
    readingDayTo: Int,
    readingNightFrom: Int,
    readingNightTo: Int,
    dateFrom: LocalDate,
    dateTo: LocalDate,
    prices: IPgePrices
) : CalculatedEnergyBill(
    dateFrom,
    dateTo,
    prices.oplataAbonamentowa,
    prices.oplataPrzejsciowa,
    prices.oplataStalaZaPrzesyl,
    prices
) {

    val dayConsumption = readingDayTo - readingDayFrom
    val nightConsumption = readingNightTo - readingNightFrom
    val dayConsumptionFromJuly16 = countConsumptionPartFromJuly16(dateFrom, dateTo, dayConsumption)
    val nightConsumptionFromJuly16 = countConsumptionPartFromJuly16(dateFrom, dateTo, nightConsumption)

    val zaEnergieCzynnaDayNetCharge = countNetAndAddToSum(prices.zaEnergieCzynnaDzien, dayConsumption)
    val skladnikJakosciowyDayNetCharge = countNetAndAddToSum(prices.skladnikJakosciowy, dayConsumption)
    val oplataSieciowaDayNetCharge = countNetAndAddToSum(prices.oplataSieciowaDzien, dayConsumption)
    val zaEnergieCzynnaNightNetCharge = countNetAndAddToSum(prices.zaEnergieCzynnaNoc, nightConsumption)
    val skladnikJakosciowyNightNetCharge = countNetAndAddToSum(prices.skladnikJakosciowy, nightConsumption)
    val oplataSieciowaNightNetCharge = countNetAndAddToSum(prices.oplataSieciowaNoc, nightConsumption)
    val oplataOzeDayNetCharge = countNetAndAddToSum(prices.oplataOze, (dayConsumptionFromJuly16 * 0.001).toString())
    val oplataOzeNightNetCharge = countNetAndAddToSum(prices.oplataOze, (nightConsumptionFromJuly16 * 0.001).toString())

    val zaEnergieCzynnaDayVatCharge = countVatAndAddToSum(zaEnergieCzynnaDayNetCharge)
    val skladnikJakosciowyDayVatCharge = countVatAndAddToSum(skladnikJakosciowyDayNetCharge)
    val oplataSieciowaDayVatCharge = countVatAndAddToSum(oplataSieciowaDayNetCharge)
    val zaEnergieCzynnaNightVatCharge = countVatAndAddToSum(zaEnergieCzynnaNightNetCharge)
    val skladnikJakosciowyNightVatCharge = countVatAndAddToSum(skladnikJakosciowyNightNetCharge)
    val oplataSieciowaNightVatCharge = countVatAndAddToSum(oplataSieciowaNightNetCharge)
    val oplataOzeDayVatCharge = countVatAndAddToSum(oplataOzeDayNetCharge)
    val oplataOzeNightVatCharge = countVatAndAddToSum(oplataOzeNightNetCharge)

    override val totalConsumption = dayConsumption + nightConsumption
}
