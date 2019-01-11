package pl.srw.billcalculator.bill.calculation

import org.threeten.bp.LocalDate
import pl.srw.billcalculator.db.Prices
import pl.srw.billcalculator.settings.prices.DISABLED_OPLATA_HANDLOWA
import pl.srw.billcalculator.settings.prices.SharedPrefsPrices
import pl.srw.billcalculator.util.Dates
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Calculates and represent each bill position value.
 * <br/>
 * Net value precision is not modified.<br/>
 * Vat value precision is Net.precision+2
 */
abstract class CalculatedBill(
    private val greedy: Boolean,
    dateFrom: LocalDate,
    dateTo: LocalDate,
    prices: Prices
) {

    companion object {
        private val VAT = BigDecimal("0.23")
    }

    val monthCount: Int = Dates.countWholeMonth(dateFrom, dateTo)

    val grossChargeSum: BigDecimal
        get() = netChargeSum.add(vatChargeSum)

    var netChargeSum: BigDecimal = BigDecimal.ZERO
        private set

    private var _vatChargeSum = BigDecimal.ZERO
    val vatChargeSum: BigDecimal
        get() = _vatChargeSum.round()

    val oplataHandlowaNetCharge = if (isOplataHandlowaEnabled(prices)) {
        countNetAndAddToSum(prices.oplataHandlowa, monthCount)
    } else {
        BigDecimal.ZERO
    }
    val oplataHandlowaVatCharge = countVatAndAddToSum(oplataHandlowaNetCharge)

    protected fun countNetAndAddToSum(price: String, count: Int) = countNetAndAddToSum(price, BigDecimal(count))

    protected fun countNetAndAddToSum(price: String, count: String) = countNetAndAddToSum(price, BigDecimal(count))

    protected fun countNetAndAddToSum(price: String, count: BigInteger) = countNetAndAddToSum(price, BigDecimal(count))

    protected fun countNetAndAddToSum(price: String, count: BigDecimal): BigDecimal {
        val netCharge = BigDecimal(price).multiply(count)
        netChargeSum = netChargeSum.add(netCharge.round())
        return netCharge
    }

    protected fun countVatAndAddToSum(netCharge: BigDecimal): BigDecimal {
        val vatCharge = netCharge.multiply(VAT)
        _vatChargeSum = if (greedy)
            _vatChargeSum.add(vatCharge.round())
        else
            _vatChargeSum.add(vatCharge)
        return vatCharge
    }

    private fun isOplataHandlowaEnabled(prices: Prices) =
        if (prices is SharedPrefsPrices) prices.enabledOplataHandlowa
        else prices.oplataHandlowa != DISABLED_OPLATA_HANDLOWA

}
