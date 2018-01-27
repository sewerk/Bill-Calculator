package pl.srw.billcalculator.data.settings.prices

import android.support.annotation.StringRes
import pl.srw.billcalculator.R

enum class PriceMeasure(@StringRes val resId: Int) {
    KWH(R.string.price_measure_zl_kWh),
    MWH(R.string.price_measure_zl_MWh),
    MONTH(R.string.price_measure_zl_month),
    NONE(R.string.empty)
}
