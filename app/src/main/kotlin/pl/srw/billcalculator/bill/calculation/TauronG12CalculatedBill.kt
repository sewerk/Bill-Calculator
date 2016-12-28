package pl.srw.billcalculator.bill.calculation

import pl.srw.billcalculator.pojo.ITauronPrices

class TauronG12CalculatedBill(readingDayFrom: Int, readingDayTo: Int,
                              readingNightFrom: Int, readingNightTo: Int,
                              dateFrom: String, dateTo: String, prices: ITauronPrices)
    : TauronCalculatedBill(dateFrom, dateTo, prices.oplataAbonamentowa, prices.oplataPrzejsciowa, prices.oplataDystrybucyjnaStala) {

    val dayConsumption = readingDayTo - readingDayFrom
    val nightConsumption = readingNightTo - readingNightFrom

    val energiaElektrycznaDayNetCharge = countNetAndAddToSum(prices.energiaElektrycznaCzynnaDzien, dayConsumption)
    val oplataDystrybucyjnaZmiennaDayNetCharge = countNetAndAddToSum(prices.oplataDystrybucyjnaZmiennaDzien, dayConsumption)
    val energiaElektrycznaNightNetCharge = countNetAndAddToSum(prices.energiaElektrycznaCzynnaNoc, nightConsumption)
    val oplataDystrybucyjnaZmiennaNightNetCharge = countNetAndAddToSum(prices.oplataDystrybucyjnaZmiennaNoc, nightConsumption)

    val energiaElektrycznaDayVatCharge = countVatAndAddToSum(energiaElektrycznaDayNetCharge)
    val oplataDystrybucyjnaZmiennaDayVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaDayNetCharge)
    val energiaElektrycznaNightVatCharge = countVatAndAddToSum(energiaElektrycznaNightNetCharge)
    val oplataDystrybucyjnaZmiennaNightVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaNightNetCharge)

    override val totalConsumption = dayConsumption + nightConsumption

    override val sellNetCharge = energiaElektrycznaDayNetCharge.round().add(energiaElektrycznaNightNetCharge.round())

    override val sellVatCharge = energiaElektrycznaDayVatCharge.round().add(energiaElektrycznaNightVatCharge.round())
}