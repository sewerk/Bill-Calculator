package pl.srw.billcalculator.settings.details

import android.content.Context
import pl.srw.billcalculator.R

object SettingsDetailsListItemTextExtractor {

    fun getTitle(context: Context, item: SettingsDetailsListItem): String = when (item) {
        is PickingSettingsDetailsListItem -> context.getString(item.title)
        is InputSettingsDetailsListItem -> item.title
    }

    fun getSummary(context: Context, item: SettingsDetailsListItem): String = when (item) {
        is PickingSettingsDetailsListItem -> context.getString(item.value)
        is InputSettingsDetailsListItem -> {
            if (!item.enabled) {
                context.getString(R.string.price_disabled)
            } else {
                val measure = if (item.measure == R.string.empty) "" else "[${context.getString(item.measure)}]"
                "${item.value} $measure"
            }
        }
    }
}
