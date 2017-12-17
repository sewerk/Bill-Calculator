package pl.srw.billcalculator.bill.calculation

import org.threeten.bp.LocalDate
import pl.srw.billcalculator.util.Dates
import java.math.BigDecimal

abstract class CalculatedEnergyBill(dateFrom: LocalDate, dateTo: LocalDate,
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

    protected fun countConsumptionPartFromJuly16(dateFrom: LocalDate, dateTo: LocalDate, consumption: Int): Int {
        val daysFromJuly16 = Dates.countDaysFromJuly16(dateFrom, dateTo)
        val periodInDays = Dates.countDays(dateFrom, dateTo)
        if (daysFromJuly16 == periodInDays) {
            return consumption
        }
        return BigDecimal(consumption)
                .multiply(BigDecimal.valueOf(daysFromJuly16 / periodInDays.toDouble()))
                .toInt()
    }

    val excise: BigDecimal
        get() = EXCISE.multiply(BigDecimal(totalConsumption))

    abstract val totalConsumption: Int
}
