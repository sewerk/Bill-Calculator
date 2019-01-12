package pl.srw.billcalculator.bill.calculation

import org.threeten.bp.LocalDate
import pl.srw.billcalculator.pojo.ITauronPrices
import java.math.BigDecimal

class TauronG12CalculatedBill(
    readingDayFrom: Int,
    readingDayTo: Int,
    readingNightFrom: Int,
    readingNightTo: Int,
    dateFrom: LocalDate,
    dateTo: LocalDate,
    prices: ITauronPrices
) : TauronCalculatedBill(
    dateFrom,
    dateTo,
    prices.oplataAbonamentowa,
    prices.oplataPrzejsciowa,
    prices.oplataDystrybucyjnaStala,
    prices
) {

    val dayConsumption = readingDayTo - readingDayFrom
    val nightConsumption = readingNightTo - readingNightFrom
    val dayConsumptionFromJuly16 = countConsumptionPartFromJuly16(dateFrom, dateTo, dayConsumption)
    val nightConsumptionFromJuly16 = countConsumptionPartFromJuly16(dateFrom, dateTo, nightConsumption)

    val energiaElektrycznaDayNetCharge = countNetAndAddToSum(prices.energiaElektrycznaCzynnaDzien, dayConsumption)
    val oplataDystrybucyjnaZmiennaDayNetCharge = countNetAndAddToSum(prices.oplataDystrybucyjnaZmiennaDzien, dayConsumption)
    val energiaElektrycznaNightNetCharge = countNetAndAddToSum(prices.energiaElektrycznaCzynnaNoc, nightConsumption)
    val oplataDystrybucyjnaZmiennaNightNetCharge = countNetAndAddToSum(prices.oplataDystrybucyjnaZmiennaNoc, nightConsumption)
    val oplataOzeDayNetCharge = countNetAndAddToSum(prices.oplataOze, dayConsumptionFromJuly16)
    val oplataOzeNightNetCharge = countNetAndAddToSum(prices.oplataOze, nightConsumptionFromJuly16)

    val energiaElektrycznaDayVatCharge = countVatAndAddToSum(energiaElektrycznaDayNetCharge)
    val oplataDystrybucyjnaZmiennaDayVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaDayNetCharge)
    val energiaElektrycznaNightVatCharge = countVatAndAddToSum(energiaElektrycznaNightNetCharge)
    val oplataDystrybucyjnaZmiennaNightVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaNightNetCharge)
    val oplataOzeDayVatCharge = countVatAndAddToSum(oplataOzeDayNetCharge)
    val oplataOzeNightVatCharge = countVatAndAddToSum(oplataOzeNightNetCharge)

    override val totalConsumption = dayConsumption + nightConsumption

    override val sellNetCharge: BigDecimal = energiaElektrycznaDayNetCharge
        .add(energiaElektrycznaNightNetCharge)
        .add(oplataHandlowaNetCharge)
        .round()

    override val sellVatCharge: BigDecimal = energiaElektrycznaDayVatCharge
        .add(energiaElektrycznaNightVatCharge)
        .add(oplataHandlowaVatCharge)
        .round()
}
