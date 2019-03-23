package pl.srw.billcalculator.settings.prices

import android.content.SharedPreferences
import pl.srw.billcalculator.pojo.IPgnigPrices
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PgnigPrices @Inject constructor(private val prefs: SharedPreferences)
    : SharedPrefsPrices(prefs), Restorable, IPgnigPrices {

    override var oplataAbonamentowa by stringPref(KEYS.ABONAMENTOWA)
    override var paliwoGazowe by stringPref(KEYS.PALIWO_GAZOWE)
    override var dystrybucyjnaStala by stringPref(KEYS.DYSTRYBUCYJNA_STALA)
    override var dystrybucyjnaZmienna by stringPref(KEYS.DYSTRYBUCYJNA_ZMIENNA)
    override var wspolczynnikKonwersji by stringPref(KEYS.WSP_KONWERSJI)
    override var oplataHandlowa by stringPref(KEYS.HANDLOWA)
    override var enabledOplataHandlowa by booleanPref(KEYS.HANDLOWA_ENABLED)

    fun convertToDb() = pl.srw.billcalculator.db.PgnigPrices().also {
        it.setDystrybucyjnaStala(dystrybucyjnaStala)
        it.setDystrybucyjnaZmienna(dystrybucyjnaZmienna)
        it.setOplataAbonamentowa(oplataAbonamentowa)
        it.setPaliwoGazowe(paliwoGazowe)
        it.setWspolczynnikKonwersji(wspolczynnikKonwersji)
        it.setOplataHandlowa(oplataHandlowaForDb)
    }

    override fun setDefault() {
        oplataAbonamentowa = DEFAULTS.abonamentowa
        dystrybucyjnaStala = DEFAULTS.dystrybucyjna_stala
        dystrybucyjnaZmienna = DEFAULTS.dystrybucyjna_zmienna
        paliwoGazowe = DEFAULTS.paliwo_gazowe
        wspolczynnikKonwersji = DEFAULTS.wsp_konwersji
        oplataHandlowa = DEFAULTS.handlowa
        enabledOplataHandlowa = DEFAULTS.handlowa_enabled
    }

    override fun setDefaultIfNotSet() {
        if (!prefs.contains(KEYS.ABONAMENTOWA)) {
            setDefault()
            return
        }
        if (!prefs.contains(KEYS.HANDLOWA)) {
            oplataHandlowa = DEFAULTS.handlowa
            enabledOplataHandlowa = DEFAULTS.handlowa_enabled
        }
    }

    private object DEFAULTS {
        const val abonamentowa = "8.67"
        const val paliwo_gazowe = "0.09392"
        const val dystrybucyjna_stala = "11.39"
        const val dystrybucyjna_zmienna = "0.02821"
        const val wsp_konwersji = "11.241"
        const val handlowa = "0.00"
        const val handlowa_enabled = false
    }

    private object KEYS {
        const val ABONAMENTOWA = "abonamentowa"
        const val PALIWO_GAZOWE = "paliwo_gazowe"
        const val DYSTRYBUCYJNA_STALA = "dystrybucyjna_stala"
        const val DYSTRYBUCYJNA_ZMIENNA = "dystrybucyjna_zmienna"
        const val WSP_KONWERSJI = "wsp_konwersji"
        const val HANDLOWA = "pgnig_oplata_handlowa"
        const val HANDLOWA_ENABLED = "pgnig_oplata_handlowa_enabled"
    }
}
