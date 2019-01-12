package pl.srw.billcalculator.settings.details

import android.os.Parcelable
import android.support.annotation.StringRes
import kotlinx.android.parcel.Parcelize

sealed class SettingsDetailsListItem {
    abstract fun visit(visitor: SettingsDetailsItemClickVisitor)
}

@Parcelize
data class PickingSettingsDetailsListItem(
    @StringRes val title: Int,
    @StringRes val value: Int,
    val options: List<Int>
) : SettingsDetailsListItem(), Parcelable {

    override fun visit(visitor: SettingsDetailsItemClickVisitor) {
        visitor.visit(this)
    }
}

@Parcelize
data class InputSettingsDetailsListItem(
    val title: String,
    val optional: Boolean,
    val enabled: Boolean = true,
    val value: String,
    @StringRes val measure: Int,
    val description: Int?
) : SettingsDetailsListItem(), Parcelable {

    override fun visit(visitor: SettingsDetailsItemClickVisitor) {
        visitor.visit(this)
    }
}
