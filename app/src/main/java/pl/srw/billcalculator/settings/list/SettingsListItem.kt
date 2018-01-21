package pl.srw.billcalculator.settings.list

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import pl.srw.billcalculator.type.Provider

data class SettingsListItem(val provider: Provider,
                            @StringRes val titleRes: Int,
                            @StringRes val summaryRes: Int,
                            @DrawableRes val logoRes: Int)
