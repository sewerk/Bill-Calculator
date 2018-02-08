package pl.srw.billcalculator.settings.prices

import android.content.SharedPreferences
import pl.srw.billcalculator.data.settings.prices.EnergyTariff
import pl.srw.billcalculator.pojo.IPgePrices
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PgePrices @Inject constructor(private val prefs: SharedPreferences)
    : SharedPrefsPrices(prefs), Restorable, EnergyPrices, IPgePrices {

    override var tariff by tariffPref(KEYS.TARIFF)
    override var zaEnergieCzynna by stringPref(KEYS.CENA_ZA_ENERGIE_CZYNNA)
    override var zaEnergieCzynnaDzien by stringPref(KEYS.CENA_ZA_ENERGIE_CZYNNA_G_12_DZIEN)
    override var zaEnergieCzynnaNoc by stringPref(KEYS.CENA_ZA_ENERGIE_CZYNNA_G_12_NOC)
    override var oplataSieciowa by stringPref(KEYS.CENA_OPLATA_SIECIOWA)
    override var oplataSieciowaDzien by stringPref(KEYS.CENA_OPLATA_SIECIOWA_G_12_DZIEN)
    override var oplataSieciowaNoc by stringPref(KEYS.CENA_OPLATA_SIECIOWA_G_12_NOC)
    override var skladnikJakosciowy by stringPref(KEYS.CENA_SKLADNIK_JAKOSCIOWY)
    override var oplataPrzejsciowa by stringPref(KEYS.CENA_OPLATA_PRZEJSCIOWA)
    override var oplataStalaZaPrzesyl by stringPref(KEYS.CENA_OPLATA_STALA_ZA_PRZESYL)
    override var oplataAbonamentowa by stringPref(KEYS.CENA_OPLATA_ABONAMENTOWA)
    override var oplataOze by stringPref(KEYS.CENA_OPLATA_OZE)

    fun convertToDb() = pl.srw.billcalculator.db.PgePrices().apply {
        setOplataAbonamentowa(oplataAbonamentowa)
        setOplataPrzejsciowa(oplataPrzejsciowa)
        setOplataSieciowa(oplataSieciowa)
        setOplataStalaZaPrzesyl(oplataStalaZaPrzesyl)
        setSkladnikJakosciowy(skladnikJakosciowy)
        setZaEnergieCzynna(zaEnergieCzynna)
        setOplataOze(oplataOze)
        setOplataSieciowaDzien(oplataSieciowaDzien)
        setOplataSieciowaNoc(oplataSieciowaNoc)
        setZaEnergieCzynnaDzien(zaEnergieCzynnaDzien)
        setZaEnergieCzynnaNoc(zaEnergieCzynnaNoc)
    }

    override fun setDefault() {
        tariff = DEFAULTS.tariff
        zaEnergieCzynna = DEFAULTS.cena_za_energie_czynna
        zaEnergieCzynnaDzien = DEFAULTS.cena_za_energie_czynna_G12dzien
        zaEnergieCzynnaNoc = DEFAULTS.cena_za_energie_czynna_G12noc
        oplataSieciowa = DEFAULTS.cena_oplata_sieciowa
        oplataSieciowaDzien = DEFAULTS.cena_oplata_sieciowa_G12dzien
        oplataSieciowaNoc = DEFAULTS.cena_oplata_sieciowa_G12noc
        skladnikJakosciowy = DEFAULTS.cena_skladnik_jakosciowy
        oplataPrzejsciowa = DEFAULTS.cena_oplata_przejsciowa
        oplataStalaZaPrzesyl = DEFAULTS.cena_oplata_stala_za_przesyl
        oplataAbonamentowa = DEFAULTS.cena_oplata_abonamentowa
        oplataOze = DEFAULTS.cena_oplata_oze
    }

    override fun setDefaultIfNotSet() {
        if (!prefs.contains(KEYS.CENA_ZA_ENERGIE_CZYNNA))
            setDefault()
        else if (!prefs.contains(KEYS.CENA_OPLATA_OZE))
            oplataOze = DEFAULTS.cena_oplata_oze
    }

    private object DEFAULTS {
        @JvmField val tariff = EnergyTariff.G11
        const val cena_za_energie_czynna = "0.2410"
        const val cena_za_energie_czynna_G12dzien = "0.2760"
        const val cena_za_energie_czynna_G12noc = "0.1770"
        const val cena_oplata_sieciowa = "0.2075"
        const val cena_oplata_sieciowa_G12dzien = "0.2360"
        const val cena_oplata_sieciowa_G12noc = "0.0720"
        const val cena_skladnik_jakosciowy = "0.0127"
        const val cena_oplata_przejsciowa = "1.90"
        const val cena_oplata_stala_za_przesyl = "1.96"
        const val cena_oplata_abonamentowa = "5.10"
        const val cena_oplata_oze = "3.70"
    }

    private object KEYS {
        const val TARIFF = "pge_tariff"
        const val CENA_ZA_ENERGIE_CZYNNA = "cena_za_energie_czynna"
        const val CENA_ZA_ENERGIE_CZYNNA_G_12_DZIEN = "cena_za_energie_czynna_G12dzien"
        const val CENA_ZA_ENERGIE_CZYNNA_G_12_NOC = "cena_za_energie_czynna_G12noc"
        const val CENA_OPLATA_SIECIOWA = "cena_oplata_sieciowa"
        const val CENA_OPLATA_SIECIOWA_G_12_DZIEN = "cena_oplata_sieciowa_G12dzien"
        const val CENA_OPLATA_SIECIOWA_G_12_NOC = "cena_oplata_sieciowa_G12noc"
        const val CENA_SKLADNIK_JAKOSCIOWY = "cena_skladnik_jakosciowy"
        const val CENA_OPLATA_PRZEJSCIOWA = "cena_oplata_przejsciowa"
        const val CENA_OPLATA_STALA_ZA_PRZESYL = "cena_oplata_stala_za_przesyl"
        const val CENA_OPLATA_ABONAMENTOWA = "cena_oplata_abonamentowa"
        const val CENA_OPLATA_OZE = "cena_oplata_oze"
    }
}
