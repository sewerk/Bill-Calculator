package pl.srw.billcalculator.bill.calculation

import java.math.BigDecimal

abstract class CalculatedEnergyBill(dateFrom: String, dateTo: String,
                                    oplataAbonamentowa: String, oplataPrzejsciowa: String, oplataStalaZaPrzesyl: String)
    : CalculatedBill(false, dateFrom, dateTo) {

    companion object {
        private val EXCISE = BigDecimal("0.02")
    }

    val oplataAbonamentowaNetCharge = countNetAndAddToSum(oplataAbonamentowa, monthCount)
    val oplataPrzejsciowaNetCharge = countNetAndAddToSum(oplataPrzejsciowa, monthCount)
    val oplataDystrybucyjnaStalaNetCharge = countNetAndAddToSum(oplataStalaZaPrzesyl, monthCount)

    val oplataAbonamentowaVatCharge = countVatAndAddToSum(oplataAbonamentowaNetCharge)
    val oplataPrzejsciowaVatCharge = countVatAndAddToSum(oplataPrzejsciowaNetCharge)
    val oplataDystrybucyjnaStalaVatCharge = countVatAndAddToSum(oplataDystrybucyjnaStalaNetCharge)

    val excise: BigDecimal
        get() = EXCISE.multiply(BigDecimal(totalConsumption))

    abstract val totalConsumption: Int
}