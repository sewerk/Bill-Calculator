package pl.srw.billcalculator.data.settings.prices

import android.support.annotation.StringRes
import pl.srw.billcalculator.R

@SuppressWarnings("EnumNaming")
enum class EnergyTariff(@StringRes val stringRes: Int) {
    G11(R.string.settings_energy_tariff_pick_G11),
    G12(R.string.settings_energy_tariff_pick_G12);

    companion object {
        fun allStringResources(): List<Int> =
            values().map { it.stringRes }
        fun findByStringRes(searchRes: Int): EnergyTariff =
            values().find { it.stringRes == searchRes }!!
    }
}
