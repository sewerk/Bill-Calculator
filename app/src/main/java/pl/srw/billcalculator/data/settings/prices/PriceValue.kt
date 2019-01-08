package pl.srw.billcalculator.data.settings.prices

interface PriceValue {
    val value: String
    val measure: PriceMeasure
}

data class AlwaysEnabledPriceValue(
    override val value: String,
    override val measure: PriceMeasure
) : PriceValue

/** Bill entry price data which can be hidden from bill calculation */
data class OptionalPriceValue(
    override val value: String,
    override val measure: PriceMeasure,
    val enabled: Boolean
) : PriceValue
