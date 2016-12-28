package pl.srw.billcalculator.bill.calculation

import java.math.BigDecimal

fun BigDecimal.round(): BigDecimal = this.setScale(2, java.math.RoundingMode.HALF_UP)