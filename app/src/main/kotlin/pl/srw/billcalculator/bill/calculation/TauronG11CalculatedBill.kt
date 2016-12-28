package pl.srw.billcalculator.bill.calculation

import pl.srw.billcalculator.pojo.ITauronPrices

class TauronG11CalculatedBill(readingFrom: Int, readingTo: Int, dateFrom: String, dateTo: String, prices: ITauronPrices)
    : TauronCalculatedBill(dateFrom, dateTo, prices.oplataAbonamentowa, prices.oplataPrzejsciowa, prices.oplataDystrybucyjnaStala) {

    override val totalConsumption = readingTo - readingFrom

    val energiaElektrycznaNetCharge = countNetAndAddToSum(prices.energiaElektrycznaCzynna, totalConsumption)
    val oplataDystrybucyjnaZmiennaNetCharge = countNetAndAddToSum(prices.oplataDystrybucyjnaZmienna, totalConsumption)

    val energiaElektrycznaVatCharge = countVatAndAddToSum(energiaElektrycznaNetCharge)
    val oplataDystrybucyjnaZmiennaVatCharge = countVatAndAddToSum(oplataDystrybucyjnaZmiennaNetCharge)

    override val sellNetCharge = energiaElektrycznaNetCharge.round()

    override val sellVatCharge = energiaElektrycznaVatCharge.round()
}