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
@SuppressWarnings("ComplexMethod", "TooManyFunctions", "LargeClass")
@Singleton
class PricesBridge @Inject constructor(private val providerMapper: ProviderMapper) {
    private val pgnigPrices = providerMapper.getPrefsPrices(Provider.PGNIG) as PgnigPrices
    private val pgePrices = providerMapper.getPrefsPrices(Provider.PGE) as PgePrices
    private val tauronPrices = providerMapper.getPrefsPrices(Provider.TAURON) as TauronPrices

    fun getItemsForPgnig(): Map<String, PriceValue> {
        val prices = pgnigPrices
        return mapOf(
            PGNIG.WSP_KONW to AlwaysEnabledPriceValue(prices.wspolczynnikKonwersji, PriceMeasure.NONE),
            PGNIG.ABONAMENTOWA to AlwaysEnabledPriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH),
            PGNIG.HANDLOWA to OptionalPriceValue(prices.oplataHandlowa, PriceMeasure.MONTH, prices.enabledOplataHandlowa),
            PGNIG.PALIWO_GAZ to AlwaysEnabledPriceValue(prices.paliwoGazowe, PriceMeasure.KWH),
            PGNIG.DYSTR_STALA to AlwaysEnabledPriceValue(prices.dystrybucyjnaStala, PriceMeasure.MONTH),
            PGNIG.DYSTR_ZMIENNA to AlwaysEnabledPriceValue(prices.dystrybucyjnaZmienna, PriceMeasure.KWH)
        )
    }

    fun updatePgnig(name: String, value: String, enabled: Boolean) {
        val prices = pgnigPrices
        when (name) {
            PGNIG.WSP_KONW -> if (prices.wspolczynnikKonwersji != value) prices.wspolczynnikKonwersji = value
            PGNIG.ABONAMENTOWA -> if (prices.oplataAbonamentowa != value) prices.oplataAbonamentowa = value
            PGNIG.HANDLOWA -> {
                if (prices.oplataHandlowa != value) prices.oplataHandlowa = value
                if (prices.enabledOplataHandlowa != enabled) prices.enabledOplataHandlowa = enabled
            }
            PGNIG.PALIWO_GAZ -> if (prices.paliwoGazowe != value) prices.paliwoGazowe = value
            PGNIG.DYSTR_STALA -> if (prices.dystrybucyjnaStala != value) prices.dystrybucyjnaStala = value
            PGNIG.DYSTR_ZMIENNA -> if (prices.dystrybucyjnaZmienna != value) prices.dystrybucyjnaZmienna = value
            else IllegalArgumentException("Unknown PGNIG price name: $name")
        }
    }

    fun getItemsForPgeG11(): Map<String, PriceValue> {
        val prices = pgePrices
        return mapOf(
            PGE.ENERGIA to AlwaysEnabledPriceValue(prices.zaEnergieCzynna, PriceMeasure.KWH),
            PGE.OZE to AlwaysEnabledPriceValue(prices.oplataOze, PriceMeasure.MWH),
            PGE.SKL_JAKOSC to AlwaysEnabledPriceValue(prices.skladnikJakosciowy, PriceMeasure.KWH),
            PGE.SIECIOWA to AlwaysEnabledPriceValue(prices.oplataSieciowa, PriceMeasure.KWH),
            PGE.PRZEJSCIOWA to AlwaysEnabledPriceValue(prices.oplataPrzejsciowa, PriceMeasure.MONTH),
            PGE.STALA to AlwaysEnabledPriceValue(prices.oplataStalaZaPrzesyl, PriceMeasure.MONTH),
            PGE.HANDLOWA to OptionalPriceValue(prices.oplataHandlowa, PriceMeasure.MONTH, prices.enabledOplataHandlowa),
            PGE.ABONAMENTOWA to AlwaysEnabledPriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH)
        )
    }

    fun getItemsForPgeG12(): Map<String, PriceValue> {
        val prices = pgePrices
        return mapOf(
            PGE.ENERGIA_DZIEN to AlwaysEnabledPriceValue(prices.zaEnergieCzynnaDzien, PriceMeasure.KWH),
            PGE.SIECIOWA_DZIEN to AlwaysEnabledPriceValue(prices.oplataSieciowaDzien, PriceMeasure.KWH),
            PGE.OZE to AlwaysEnabledPriceValue(prices.oplataOze, PriceMeasure.MWH),
            PGE.SKL_JAKOSC to AlwaysEnabledPriceValue(prices.skladnikJakosciowy, PriceMeasure.KWH),
            PGE.ENERGIA_NOC to AlwaysEnabledPriceValue(prices.zaEnergieCzynnaNoc, PriceMeasure.KWH),
            PGE.SIECIOWA_NOC to AlwaysEnabledPriceValue(prices.oplataSieciowaNoc, PriceMeasure.KWH),
            PGE.PRZEJSCIOWA to AlwaysEnabledPriceValue(prices.oplataPrzejsciowa, PriceMeasure.MONTH),
            PGE.STALA to AlwaysEnabledPriceValue(prices.oplataStalaZaPrzesyl, PriceMeasure.MONTH),
            PGE.HANDLOWA to OptionalPriceValue(prices.oplataHandlowa, PriceMeasure.MONTH, prices.enabledOplataHandlowa),
            PGE.ABONAMENTOWA to AlwaysEnabledPriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH)
        )
    }

    fun updatePge(name: String, value: String, enabled: Boolean) {
        val prices = pgePrices
        when (name) {
            PGE.ENERGIA -> if (prices.zaEnergieCzynna != value) prices.zaEnergieCzynna = value
            PGE.OZE -> if (prices.oplataOze != value) prices.oplataOze = value
            PGE.SKL_JAKOSC -> if (prices.skladnikJakosciowy != value) prices.skladnikJakosciowy = value
            PGE.SIECIOWA -> if (prices.oplataSieciowa != value) prices.oplataSieciowa = value
            PGE.PRZEJSCIOWA -> if (prices.oplataPrzejsciowa != value) prices.oplataPrzejsciowa = value
            PGE.STALA -> if (prices.oplataStalaZaPrzesyl != value) prices.oplataStalaZaPrzesyl = value
            PGE.HANDLOWA -> {
                if (prices.oplataHandlowa != value) prices.oplataHandlowa = value
                if (prices.enabledOplataHandlowa != enabled) prices.enabledOplataHandlowa = enabled
            }
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
        return mapOf(
            TAURON.ENERGIA to AlwaysEnabledPriceValue(prices.energiaElektrycznaCzynna, PriceMeasure.KWH),
            TAURON.HANDLOWA to OptionalPriceValue(prices.oplataHandlowa, PriceMeasure.MONTH, prices.enabledOplataHandlowa),
            TAURON.OZE to AlwaysEnabledPriceValue(prices.oplataOze, PriceMeasure.KWH),
            TAURON.DYST_ZMIENNA to AlwaysEnabledPriceValue(prices.oplataDystrybucyjnaZmienna, PriceMeasure.KWH),
            TAURON.DYST_STALA to AlwaysEnabledPriceValue(prices.oplataDystrybucyjnaStala, PriceMeasure.MONTH),
            TAURON.PRZEJSCIOWA to AlwaysEnabledPriceValue(prices.oplataPrzejsciowa, PriceMeasure.MONTH),
            TAURON.ABONAMENTOWA to AlwaysEnabledPriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH)
        )
    }

    fun getItemsForTauronG12(): Map<String, PriceValue> {
        val prices = tauronPrices
        return mapOf(
            TAURON.ENERGIA_DZIEN to AlwaysEnabledPriceValue(prices.energiaElektrycznaCzynnaDzien, PriceMeasure.KWH),
            TAURON.ENERGIA_NOC to AlwaysEnabledPriceValue(prices.energiaElektrycznaCzynnaNoc, PriceMeasure.KWH),
            TAURON.HANDLOWA to OptionalPriceValue(prices.oplataHandlowa, PriceMeasure.MONTH, prices.enabledOplataHandlowa),
            TAURON.OZE to AlwaysEnabledPriceValue(prices.oplataOze, PriceMeasure.KWH),
            TAURON.DYST_ZMIENNA_DZIEN to AlwaysEnabledPriceValue(prices.oplataDystrybucyjnaZmiennaDzien, PriceMeasure.KWH),
            TAURON.DYST_ZMIENNA_NOC to AlwaysEnabledPriceValue(prices.oplataDystrybucyjnaZmiennaNoc, PriceMeasure.KWH),
            TAURON.DYST_STALA to AlwaysEnabledPriceValue(prices.oplataDystrybucyjnaStala, PriceMeasure.MONTH),
            TAURON.PRZEJSCIOWA to AlwaysEnabledPriceValue(prices.oplataPrzejsciowa, PriceMeasure.MONTH),
            TAURON.ABONAMENTOWA to AlwaysEnabledPriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH)
        )
    }

    fun updateTauron(name: String, value: String, enabled: Boolean) {
        val prices = tauronPrices
        when (name) {
            TAURON.ENERGIA -> if (prices.energiaElektrycznaCzynna != value) prices.energiaElektrycznaCzynna = value
            TAURON.OZE -> if (prices.oplataOze != value) prices.oplataOze = value
            TAURON.HANDLOWA -> {
                if (prices.oplataHandlowa != value) prices.oplataHandlowa = value
                if (prices.enabledOplataHandlowa != enabled) prices.enabledOplataHandlowa = enabled
            }
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
    const val HANDLOWA = "Opłata handlowa"
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
    const val HANDLOWA = "opłata handlowa"

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
    const val HANDLOWA = "Opłata handlowa"

    const val ENERGIA_DZIEN = "Energia elektryczna czynna dzienna"
    const val ENERGIA_NOC = "Energia elektryczna czynna nocna"
    const val DYST_ZMIENNA_DZIEN = "Opłata dystr. zm. dzienna"
    const val DYST_ZMIENNA_NOC = "Opłata dystr. zm. nocna"
}
