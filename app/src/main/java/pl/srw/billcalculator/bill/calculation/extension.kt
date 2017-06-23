package pl.srw.billcalculator.bill.calculation

import java.math.BigDecimal

/**
 * Round value for scale=2
 */
fun BigDecimal.round(): BigDecimal = this.setScale(2, java.math.RoundingMode.HALF_UP)