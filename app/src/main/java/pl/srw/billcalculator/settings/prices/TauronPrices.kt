package pl.srw.billcalculator.settings.prices

import android.content.SharedPreferences
import pl.srw.billcalculator.data.settings.prices.EnergyTariff
import pl.srw.billcalculator.pojo.ITauronPrices
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TauronPrices @Inject constructor(private val prefs: SharedPreferences)
    : SharedPrefsPrices(prefs), Restorable, EnergyPrices, ITauronPrices {

    override var tariff by tariffPref(KEYS.TARIFF)
    override var energiaElektrycznaCzynna by stringPref(KEYS.ENERGIA_ELEKTRYCZNA_CZYNNA)
    override var oplataDystrybucyjnaZmienna by stringPref(KEYS.OPLATA_DYSTRYBUCYJNA_ZMIENNA)
    override var oplataDystrybucyjnaStala by stringPref(KEYS.OPLATA_DYSTRYBUCYJNA_STALA)
    override var oplataPrzejsciowa by stringPref(KEYS.OPLATA_PRZEJSCIOWA)
    override var oplataAbonamentowa by stringPref(KEYS.OPLATA_ABONAMENTOWA)
    override var oplataOze by stringPref(KEYS.OPLATA_OZE)
    override var energiaElektrycznaCzynnaDzien by stringPref(KEYS.ENERGIA_ELEKTRYCZNA_CZYNNA_DZIEN)
    override var oplataDystrybucyjnaZmiennaDzien by stringPref(KEYS.OPLATA_DYSTRYBUCYJNA_ZMIENNA_DZIEN)
    override var energiaElektrycznaCzynnaNoc by stringPref(KEYS.ENERGIA_ELEKTRYCZNA_CZYNNA_NOC)
    override var oplataDystrybucyjnaZmiennaNoc by stringPref(KEYS.OPLATA_DYSTRYBUCYJNA_ZMIENNA_NOC)
    override var oplataHandlowa by stringPref(KEYS.HANDLOWA)
    override var enabledOplataHandlowa by booleanPref(KEYS.HANDLOWA_ENABLED)

    fun convertToDb() = pl.srw.billcalculator.db.TauronPrices().also {
        it.setEnergiaElektrycznaCzynna(energiaElektrycznaCzynna)
        it.setOplataDystrybucyjnaZmienna(oplataDystrybucyjnaZmienna)
        it.setOplataDystrybucyjnaStala(oplataDystrybucyjnaStala)
        it.setOplataPrzejsciowa(oplataPrzejsciowa)
        it.setOplataAbonamentowa(oplataAbonamentowa)
        it.setOplataOze(oplataOze)
        it.setOplataHandlowa(oplataHandlowaForDb)

        it.setEnergiaElektrycznaCzynnaDzien(energiaElektrycznaCzynnaDzien)
        it.setOplataDystrybucyjnaZmiennaDzien(oplataDystrybucyjnaZmiennaDzien)
        it.setEnergiaElektrycznaCzynnaNoc(energiaElektrycznaCzynnaNoc)
        it.setOplataDystrybucyjnaZmiennaNoc(oplataDystrybucyjnaZmiennaNoc)
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
        oplataHandlowa = DEFAULTS.oplataHandlowa
        enabledOplataHandlowa = DEFAULTS.oplataHandlowaEnabled
    }

    override fun setDefaultIfNotSet() {
        if (!prefs.contains(KEYS.ENERGIA_ELEKTRYCZNA_CZYNNA)) {
            setDefault()
        } else if (!prefs.contains(KEYS.OPLATA_OZE)) {
            oplataOze = DEFAULTS.oplataOze
        } else if (!prefs.contains(KEYS.HANDLOWA)) {
            oplataHandlowa = DEFAULTS.oplataHandlowa
            enabledOplataHandlowa = DEFAULTS.oplataHandlowaEnabled
        }
    }

    private object DEFAULTS {
        @JvmField val tariff = EnergyTariff.G11
        const val energiaElektrycznaCzynna = "0.2445"
        const val oplataDystrybucyjnaZmienna = "0.1803"
        const val oplataDystrybucyjnaStala = "2.00"
        const val oplataPrzejsciowa = "1.90"
        const val oplataAbonamentowa = "4.56"
        const val oplataOze = "0.00"
        const val oplataHandlowa = "0.00"
        const val oplataHandlowaEnabled = true

        const val energiaElektrycznaCzynnaDzien = "0.3015"
        const val oplataDystrybucyjnaZmiennaDzien = "0.1928"
        const val energiaElektrycznaCzynnaNoc = "0.1556"
        const val oplataDystrybucyjnaZmiennaNoc = "0.0633"
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
        const val HANDLOWA = "tauron_oplata_handlowa"
        const val HANDLOWA_ENABLED = "tauron_oplata_handlowa_enabled"
    }
}
