package pl.srw.billcalculator.settings.prices

import android.content.SharedPreferences
import pl.srw.billcalculator.data.settings.prices.EnergyTariff
import pl.srw.billcalculator.pojo.ITauronPrices
import pl.srw.billcalculator.settings.prices.TauronPrices.KEYS.ENERGIA_ELEKTRYCZNA_CZYNNA
import pl.srw.billcalculator.settings.prices.TauronPrices.KEYS.OPLATA_OZE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TauronPrices @Inject constructor(private val prefs: SharedPreferences)
    : SharedPrefsPrices(prefs), Restorable, EnergyPrices, ITauronPrices {

    override var tariff by tariffPref(KEYS.TARIFF)
    override var energiaElektrycznaCzynna: String by stringPref(KEYS.ENERGIA_ELEKTRYCZNA_CZYNNA)
    override var oplataDystrybucyjnaZmienna: String by stringPref(KEYS.OPLATA_DYSTRYBUCYJNA_ZMIENNA)
    override var oplataDystrybucyjnaStala: String by stringPref(KEYS.OPLATA_DYSTRYBUCYJNA_STALA)
    override var oplataPrzejsciowa: String by stringPref(KEYS.OPLATA_PRZEJSCIOWA)
    override var oplataAbonamentowa: String by stringPref(KEYS.OPLATA_ABONAMENTOWA)
    override var oplataOze: String by stringPref(KEYS.OPLATA_OZE)
    override var energiaElektrycznaCzynnaDzien: String by stringPref(KEYS.ENERGIA_ELEKTRYCZNA_CZYNNA_DZIEN)
    override var oplataDystrybucyjnaZmiennaDzien: String by stringPref(KEYS.OPLATA_DYSTRYBUCYJNA_ZMIENNA_DZIEN)
    override var energiaElektrycznaCzynnaNoc: String by stringPref(KEYS.ENERGIA_ELEKTRYCZNA_CZYNNA_NOC)
    override var oplataDystrybucyjnaZmiennaNoc: String by stringPref(KEYS.OPLATA_DYSTRYBUCYJNA_ZMIENNA_NOC)


    fun convertToDb() = pl.srw.billcalculator.db.TauronPrices().apply {
        setEnergiaElektrycznaCzynna(energiaElektrycznaCzynna)
        setOplataDystrybucyjnaZmienna(oplataDystrybucyjnaZmienna)
        setOplataDystrybucyjnaStala(oplataDystrybucyjnaStala)
        setOplataPrzejsciowa(oplataPrzejsciowa)
        setOplataAbonamentowa(oplataAbonamentowa)
        setOplataOze(oplataOze)

        setEnergiaElektrycznaCzynnaDzien(energiaElektrycznaCzynnaDzien)
        setOplataDystrybucyjnaZmiennaDzien(oplataDystrybucyjnaZmiennaDzien)
        setEnergiaElektrycznaCzynnaNoc(energiaElektrycznaCzynnaNoc)
        setOplataDystrybucyjnaZmiennaNoc(oplataDystrybucyjnaZmiennaNoc)
    }

    override fun setDefault() {
        tariff = DEFAULTS.tariff
        energiaElektrycznaCzynna = DEFAULTS.energiaElektrycznaCzynna
        oplataDystrybucyjnaZmienna = DEFAULTS.oplataDystrybucyjnaZmienna
        oplataDystrybucyjnaStala = DEFAULTS.oplataDystrybucyjnaStala
        oplataPrzejsciowa = DEFAULTS.oplataPrzejsciowa
        oplataAbonamentowa = DEFAULTS.oplataAbonamentowa
        oplataOze = DEFAULTS.oplataOze
        energiaElektrycznaCzynnaDzien = DEFAULTS.energiaElektrycznaCzynnaDzien
        oplataDystrybucyjnaZmiennaDzien = DEFAULTS.oplataDystrybucyjnaZmiennaDzien
        energiaElektrycznaCzynnaNoc = DEFAULTS.energiaElektrycznaCzynnaNoc
        oplataDystrybucyjnaZmiennaNoc = DEFAULTS.oplataDystrybucyjnaZmiennaNoc
    }

    override fun setDefaultIfNotSet() {
        if (!prefs.contains(ENERGIA_ELEKTRYCZNA_CZYNNA))
            setDefault()
        else if (!prefs.contains(OPLATA_OZE))
            oplataOze = DEFAULTS.oplataOze
    }

    private object KEYS {
        const val TARIFF = "preferences_tauron_tariff"
        const val ENERGIA_ELEKTRYCZNA_CZYNNA = "energiaElektrycznaCzynna"
        const val OPLATA_DYSTRYBUCYJNA_ZMIENNA = "oplataDystrybucyjnaZmienna"
        const val OPLATA_DYSTRYBUCYJNA_STALA = "oplataDystrybucyjnaStala"
        const val OPLATA_PRZEJSCIOWA = "oplataPrzejsciowa"
        const val OPLATA_ABONAMENTOWA = "oplataAbonamentowa"
        const val OPLATA_OZE = "oplataOze"
        const val ENERGIA_ELEKTRYCZNA_CZYNNA_DZIEN = "energiaElektrycznaCzynnaDzien"
        const val OPLATA_DYSTRYBUCYJNA_ZMIENNA_DZIEN = "oplataDystrybucyjnaZmiennaDzien"
        const val OPLATA_DYSTRYBUCYJNA_ZMIENNA_NOC = "oplataDystrybucyjnaZmiennaNoc"
        const val ENERGIA_ELEKTRYCZNA_CZYNNA_NOC = "energiaElektrycznaCzynnaNoc"
    }

    private object DEFAULTS {
        @JvmField val tariff = EnergyTariff.G11
        const val energiaElektrycznaCzynna = "0.2425"
        const val oplataDystrybucyjnaZmienna = "0.1763"
        const val oplataDystrybucyjnaStala = "1.62"
        const val oplataPrzejsciowa = "1.90"
        const val oplataAbonamentowa = "4.80"
        const val oplataOze = "0.0037"

        const val energiaElektrycznaCzynnaDzien = "0.2989"
        const val oplataDystrybucyjnaZmiennaDzien = "0.1888"
        const val energiaElektrycznaCzynnaNoc = "0.1540"
        const val oplataDystrybucyjnaZmiennaNoc = "0.0659"
    }
}
