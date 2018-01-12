package pl.srw.billcalculator.settings.details

import android.support.annotation.StringRes

sealed class SettingsDetailsListItem {
    abstract fun visit(visitor: SettingsDetailsItemClickVisitor)
}

data class PickingSettingsDetailsListItem(@StringRes val title: Int,
                                          @StringRes val summary: Int) : SettingsDetailsListItem() {

    override fun visit(visitor: SettingsDetailsItemClickVisitor) {
        visitor.visit(this)
    }
}

data class InputSettingsDetailsListItem(val title: String,
                                        val summary: String,
                                        @StringRes val measure: Int) : SettingsDetailsListItem() {

    override fun visit(visitor: SettingsDetailsItemClickVisitor) {
        visitor.visit(this)
    }
}
