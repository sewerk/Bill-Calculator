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

    fun convertToDb() = pl.srw.billcalculator.db.PgnigPrices().apply {
        setDystrybucyjnaStala(this@PgnigPrices.dystrybucyjnaStala)
        setDystrybucyjnaZmienna(this@PgnigPrices.dystrybucyjnaZmienna)
        setOplataAbonamentowa(this@PgnigPrices.oplataAbonamentowa)
        setPaliwoGazowe(this@PgnigPrices.paliwoGazowe)
        setWspolczynnikKonwersji(this@PgnigPrices.wspolczynnikKonwersji)
        setOplataHandlowa(this@PgnigPrices.oplataHandlowa)
    }

    override fun setDefault() {
        oplataAbonamentowa = DEFAULTS.abonamentowa
        dystrybucyjnaStala = DEFAULTS.dystrybucyjna_stala
        dystrybucyjnaZmienna = DEFAULTS.dystrybucyjna_zmienna
        paliwoGazowe = DEFAULTS.paliwo_gazowe
        wspolczynnikKonwersji = DEFAULTS.wsp_konwersji
        oplataHandlowa = DEFAULTS.handlowa
    }

    override fun setDefaultIfNotSet() {
        if (!prefs.contains(KEYS.ABONAMENTOWA))
            setDefault()
        else if (!prefs.contains(KEYS.HANDLOWA))
            oplataHandlowa = DEFAULTS.handlowa
    }

    private object DEFAULTS{
        const val abonamentowa = "8.67"
        const val paliwo_gazowe = "0.09392"
        const val dystrybucyjna_stala = "11.39"
        const val dystrybucyjna_zmienna = "0.02821"
        const val wsp_konwersji = "11.241"
        const val handlowa = "0.00"
    }

    private object KEYS {
        const val ABONAMENTOWA = "abonamentowa"
        const val PALIWO_GAZOWE = "paliwo_gazowe"
        const val DYSTRYBUCYJNA_STALA = "dystrybucyjna_stala"
        const val DYSTRYBUCYJNA_ZMIENNA = "dystrybucyjna_zmienna"
        const val WSP_KONWERSJI = "wsp_konwersji"
        const val HANDLOWA = "pgnig_oplata_handlowa"
    }
}
