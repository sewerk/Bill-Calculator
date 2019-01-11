package pl.srw.billcalculator.bill.calculation

import org.threeten.bp.LocalDate
import pl.srw.billcalculator.db.Prices
import java.math.BigDecimal

abstract class TauronCalculatedBill(
    dateFrom: LocalDate,
    dateTo: LocalDate,
    oplataAbonamentowaPrice: String,
    oplataPrzejsciowaPrice: String,
    oplataStalaZaPrzesylPrice: String,
    prices: Prices
) : CalculatedEnergyBill(
    dateFrom,
    dateTo,
    oplataAbonamentowaPrice,
    oplataPrzejsciowaPrice,
    oplataStalaZaPrzesylPrice,
    prices
) {

    abstract val sellNetCharge: BigDecimal

    abstract val sellVatCharge: BigDecimal

    val sellGrossCharge: BigDecimal
        get() = sellNetCharge.add(sellVatCharge)

    val distributeNetCharge: BigDecimal
        get() = netChargeSum.subtract(sellNetCharge)

    val distributeVatCharge: BigDecimal
        get() = vatChargeSum.subtract(sellVatCharge)

    val distributeGrossCharge: BigDecimal
        get() = distributeNetCharge.add(distributeVatCharge)
}
