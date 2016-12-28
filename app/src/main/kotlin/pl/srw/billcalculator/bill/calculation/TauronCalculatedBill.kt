package pl.srw.billcalculator.bill.calculation

import java.math.BigDecimal

abstract class TauronCalculatedBill(dateFrom: String, dateTo: String,
                                    oplataAbonamentowaPrice: String, oplataPrzejsciowaPrice: String, oplataStalaZaPrzesylPrice: String)
    : CalculatedEnergyBill(dateFrom, dateTo, oplataAbonamentowaPrice, oplataPrzejsciowaPrice, oplataStalaZaPrzesylPrice) {

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