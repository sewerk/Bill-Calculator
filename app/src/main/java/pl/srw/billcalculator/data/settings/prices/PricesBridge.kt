package pl.srw.billcalculator.data.settings.prices

import android.support.annotation.VisibleForTesting
import pl.srw.billcalculator.settings.prices.EnergyPrices
import pl.srw.billcalculator.settings.prices.PgePrices
import pl.srw.billcalculator.settings.prices.PgnigPrices
import pl.srw.billcalculator.settings.prices.Restorable
import pl.srw.billcalculator.settings.prices.TauronPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.ProviderMapper
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wraps operations for prices with dynamic positions to operate
 * on shared-prefs prices with static fields
 */
@SuppressWarnings("ComplexMethod", "TooManyFunctions")
@Singleton
class PricesBridge @Inject constructor(private val providerMapper: ProviderMapper) {
    private val pgnigPrices = providerMapper.getPrefsPrices(Provider.PGNIG) as PgnigPrices
    private val pgePrices = providerMapper.getPrefsPrices(Provider.PGE) as PgePrices
    private val tauronPrices = providerMapper.getPrefsPrices(Provider.TAURON) as TauronPrices

    fun getItemsForPgnig(): Map<String, PriceValue> {
        val prices = pgnigPrices
        return mapOf(PGNIG.WSP_KONW to PriceValue(prices.wspolczynnikKonwersji, PriceMeasure.NONE),
                PGNIG.ABONAMENTOWA to PriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH),
                PGNIG.PALIWO_GAZ to PriceValue(prices.paliwoGazowe, PriceMeasure.KWH),
                PGNIG.DYSTR_STALA to PriceValue(prices.dystrybucyjnaStala, PriceMeasure.MONTH),
                PGNIG.DYSTR_ZMIENNA to PriceValue(prices.dystrybucyjnaZmienna, PriceMeasure.KWH))
    }

    fun updatePgnig(name: String, value: String) {
        val prices = pgnigPrices
        when (name) {
            PGNIG.WSP_KONW -> if (prices.wspolczynnikKonwersji != value) prices.wspolczynnikKonwersji = value
            PGNIG.ABONAMENTOWA -> if (prices.oplataAbonamentowa != value) prices.oplataAbonamentowa = value
            PGNIG.PALIWO_GAZ -> if (prices.paliwoGazowe != value) prices.paliwoGazowe = value
            PGNIG.DYSTR_STALA -> if (prices.dystrybucyjnaStala != value) prices.dystrybucyjnaStala = value
            PGNIG.DYSTR_ZMIENNA -> if (prices.dystrybucyjnaZmienna != value) prices.dystrybucyjnaZmienna = value
            else IllegalArgumentException("Unknown PGNIG price name: $name")
        }
    }

    fun getItemsForPgeG11(): Map<String, PriceValue> {
        val prices = pgePrices
        return mapOf(PGE.ENERGIA to PriceValue(prices.zaEnergieCzynna, PriceMeasure.KWH),
                PGE.OZE to PriceValue(prices.oplataOze, PriceMeasure.MWH),
                PGE.SKL_JAKOSC to PriceValue(prices.skladnikJakosciowy, PriceMeasure.KWH),
                PGE.SIECIOWA to PriceValue(prices.oplataSieciowa, PriceMeasure.KWH),
                PGE.PRZEJSCIOWA to PriceValue(prices.oplataPrzejsciowa, PriceMeasure.MONTH),
                PGE.STALA to PriceValue(prices.oplataStalaZaPrzesyl, PriceMeasure.MONTH),
                PGE.ABONAMENTOWA to PriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH))
    }

    fun getItemsForPgeG12(): Map<String, PriceValue> {
        val prices = pgePrices
        return mapOf(PGE.ENERGIA_DZIEN to PriceValue(prices.zaEnergieCzynnaDzien, PriceMeasure.KWH),
                PGE.SIECIOWA_DZIEN to PriceValue(prices.oplataSieciowaDzien, PriceMeasure.KWH),
                PGE.OZE to PriceValue(prices.oplataOze, PriceMeasure.MWH),
                PGE.SKL_JAKOSC to PriceValue(prices.skladnikJakosciowy, PriceMeasure.KWH),
                PGE.ENERGIA_NOC to PriceValue(prices.zaEnergieCzynnaNoc, PriceMeasure.KWH),
                PGE.SIECIOWA_NOC to PriceValue(prices.oplataSieciowaNoc, PriceMeasure.KWH),
                PGE.PRZEJSCIOWA to PriceValue(prices.oplataPrzejsciowa, PriceMeasure.MONTH),
                PGE.STALA to PriceValue(prices.oplataStalaZaPrzesyl, PriceMeasure.MONTH),
                PGE.ABONAMENTOWA to PriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH))
    }

    fun updatePge(name: String, value: String) {
        val prices = pgePrices
        when (name) {
            PGE.ENERGIA -> if (prices.zaEnergieCzynna != value) prices.zaEnergieCzynna = value
            PGE.OZE -> if (prices.oplataOze != value) prices.oplataOze = value
            PGE.SKL_JAKOSC -> if (prices.skladnikJakosciowy != value) prices.skladnikJakosciowy = value
            PGE.SIECIOWA -> if (prices.oplataSieciowa != value) prices.oplataSieciowa = value
            PGE.PRZEJSCIOWA -> if (prices.oplataPrzejsciowa != value) prices.oplataPrzejsciowa = value
            PGE.STALA -> if (prices.oplataStalaZaPrzesyl != value) prices.oplataStalaZaPrzesyl = value
            PGE.ABONAMENTOWA -> if (prices.oplataAbonamentowa != value) prices.oplataAbonamentowa = value
            PGE.ENERGIA_DZIEN -> if (prices.zaEnergieCzynnaDzien != value) prices.zaEnergieCzynnaDzien = value
            PGE.ENERGIA_NOC -> if (prices.zaEnergieCzynnaNoc != value) prices.zaEnergieCzynnaNoc = value
            PGE.SIECIOWA_DZIEN -> if (prices.oplataSieciowaDzien != value) prices.oplataSieciowaDzien = value
            PGE.SIECIOWA_NOC -> if (prices.oplataSieciowaNoc != value) prices.oplataSieciowaNoc = value
            else IllegalArgumentException("Unknown PGE price name: $name")
        }
    }

    fun getItemsForTauronG11(): Map<String, PriceValue> {
        val prices = tauronPrices
        return mapOf(TAURON.ENERGIA to PriceValue(prices.energiaElektrycznaCzynna, PriceMeasure.KWH),
                TAURON.OZE to PriceValue(prices.oplataOze, PriceMeasure.KWH),
                TAURON.DYST_ZMIENNA to PriceValue(prices.oplataDystrybucyjnaZmienna, PriceMeasure.KWH),
                TAURON.DYST_STALA to PriceValue(prices.oplataDystrybucyjnaStala, PriceMeasure.MONTH),
                TAURON.PRZEJSCIOWA to PriceValue(prices.oplataPrzejsciowa, PriceMeasure.MONTH),
                TAURON.ABONAMENTOWA to PriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH))
    }

    fun getItemsForTauronG12(): Map<String, PriceValue> {
        val prices = tauronPrices
        return mapOf(TAURON.ENERGIA_DZIEN to PriceValue(prices.energiaElektrycznaCzynnaDzien, PriceMeasure.KWH),
                TAURON.ENERGIA_NOC to PriceValue(prices.energiaElektrycznaCzynnaNoc, PriceMeasure.KWH),
                TAURON.OZE to PriceValue(prices.oplataOze, PriceMeasure.KWH),
                TAURON.DYST_ZMIENNA_DZIEN to PriceValue(prices.oplataDystrybucyjnaZmiennaDzien, PriceMeasure.KWH),
                TAURON.DYST_ZMIENNA_NOC to PriceValue(prices.oplataDystrybucyjnaZmiennaNoc, PriceMeasure.KWH),
                TAURON.DYST_STALA to PriceValue(prices.oplataDystrybucyjnaStala, PriceMeasure.MONTH),
                TAURON.PRZEJSCIOWA to PriceValue(prices.oplataPrzejsciowa, PriceMeasure.MONTH),
                TAURON.ABONAMENTOWA to PriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH))
    }

    fun updateTauron(name: String, value: String) {
        val prices = tauronPrices
        when (name) {
            TAURON.ENERGIA -> if (prices.energiaElektrycznaCzynna != value) prices.energiaElektrycznaCzynna = value
            TAURON.OZE -> if (prices.oplataOze != value) prices.oplataOze = value
            TAURON.DYST_ZMIENNA -> if (prices.oplataDystrybucyjnaZmienna != value) prices.oplataDystrybucyjnaZmienna = value
            TAURON.DYST_STALA -> if (prices.oplataDystrybucyjnaStala != value) prices.oplataDystrybucyjnaStala = value
            TAURON.PRZEJSCIOWA -> if (prices.oplataPrzejsciowa != value) prices.oplataPrzejsciowa = value
            TAURON.ABONAMENTOWA -> if (prices.oplataAbonamentowa != value) prices.oplataAbonamentowa = value
            TAURON.ENERGIA_DZIEN -> if (prices.energiaElektrycznaCzynnaDzien != value) prices.energiaElektrycznaCzynnaDzien = value
            TAURON.ENERGIA_NOC -> if (prices.energiaElektrycznaCzynnaNoc != value) prices.energiaElektrycznaCzynnaNoc = value
            TAURON.DYST_ZMIENNA_DZIEN -> if (prices.oplataDystrybucyjnaZmiennaDzien != value) prices.oplataDystrybucyjnaZmiennaDzien = value
            TAURON.DYST_ZMIENNA_NOC -> if (prices.oplataDystrybucyjnaZmiennaNoc != value) prices.oplataDystrybucyjnaZmiennaNoc = value
            else IllegalArgumentException("Unknown TAURON price name: $name")
        }
    }

    fun getTariff(provider: Provider): EnergyTariff {
        val prices = providerMapper.getPrefsPrices(provider) as EnergyPrices
        return prices.tariff
    }

    fun updateTariff(provider: Provider, value: EnergyTariff) {
        val prices = providerMapper.getPrefsPrices(provider) as EnergyPrices
        prices.tariff = value
    }

    fun setDefaults(provider: Provider) {
        val prices = providerMapper.getPrefsPrices(provider) as Restorable
        prices.setDefault()
    }
}

@VisibleForTesting
object PGNIG {
    const val WSP_KONW = "Współczynnik konwersji"
    const val ABONAMENTOWA = "Opłata abonamentowa"
    const val PALIWO_GAZ = "Paliwo gazowe"
    const val DYSTR_STALA = "Dystrybucyjna stała"
    const val DYSTR_ZMIENNA = "Dystrybucyjna zmienna"
}

@VisibleForTesting
object PGE {
    const val ENERGIA = "za energię czynną"
    const val OZE = "opłata OZE"
    const val SKL_JAKOSC = "składnik jakościowy"
    const val SIECIOWA = "opłata sieciowa"
    const val PRZEJSCIOWA = "opłata przejściowa"
    const val STALA = "opł. stała za przesył"
    const val ABONAMENTOWA = "opłata abonamentowa"

    const val ENERGIA_DZIEN = "za energię czynną (strefa dzienna)"
    const val ENERGIA_NOC = "za energię czynną (strefa nocna)"
    const val SIECIOWA_DZIEN = "opłata sieciowa (strefa dzienna)"
    const val SIECIOWA_NOC = "opłata sieciowa (strefa nocna)"
}

@VisibleForTesting
object TAURON {
    const val ENERGIA = "Energia elektryczna czynna całodobowa"
    const val OZE = "Opłata OZE"
    const val DYST_ZMIENNA = "Opłata dystr. zm. całodobowa"
    const val DYST_STALA = "Opłata dystrybucyjna stała"
    const val PRZEJSCIOWA = "Opłata przejściowa"
    const val ABONAMENTOWA = "Opłata abonamentowa"

    const val ENERGIA_DZIEN = "Energia elektryczna czynna dzienna"
    const val ENERGIA_NOC = "Energia elektryczna czynna nocna"
    const val DYST_ZMIENNA_DZIEN = "Opłata dystr. zm. dzienna"
    const val DYST_ZMIENNA_NOC = "Opłata dystr. zm. nocna"
}
