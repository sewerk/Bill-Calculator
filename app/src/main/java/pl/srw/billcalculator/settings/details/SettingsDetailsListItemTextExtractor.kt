package pl.srw.billcalculator.settings.details

import android.content.Context

object SettingsDetailsListItemTextExtractor {

    fun getTitle(context: Context, item: SettingsDetailsListItem): String = when (item) {
        is PickingSettingsDetailsListItem -> context.getString(item.title)
        is InputSettingsDetailsListItem -> item.title
    }

    fun getSummary(context: Context, item: SettingsDetailsListItem): String = when (item) {
        is PickingSettingsDetailsListItem -> context.getString(item.summary)
        is InputSettingsDetailsListItem -> {
            val measure = if (item.measure == 0) "" else "[${context.getString(item.measure)}]"
            "${item.summary} $measure"
        }
    }
}
