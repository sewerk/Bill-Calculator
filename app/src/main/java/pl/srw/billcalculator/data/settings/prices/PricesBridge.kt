package pl.srw.billcalculator.data.settings.prices

import android.support.annotation.VisibleForTesting
import pl.srw.billcalculator.settings.prices.PgePrices
import pl.srw.billcalculator.settings.prices.PgnigPrices
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.settings.prices.TauronPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.ProviderMapper
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wraps operations for prices with dynamic positions to operate
 * on shared-prefs prices with static fields
 */
@Singleton
class PricesBridge @Inject constructor(private val providerMapper: ProviderMapper) {

    fun getItemsForPgnig(): Map<String, PriceValue> {
        val prices = providerMapper.getPrices(Provider.PGNIG) as PgnigPrices
        return mapOf(PGNIG.WSP_KONW to PriceValue(prices.wspolczynnikKonwersji, PriceMeasure.NONE),
                PGNIG.ABONAMENTOWA to PriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH),
                PGNIG.PALIWO_GAZ to PriceValue(prices.paliwoGazowe, PriceMeasure.KWH),
                PGNIG.DYSTR_STALA to PriceValue(prices.dystrybucyjnaStala, PriceMeasure.MONTH),
                PGNIG.DYSTR_ZMIENNA to PriceValue(prices.dystrybucyjnaZmienna, PriceMeasure.KWH))
    }

    fun updatePgnig(items: Map<String, String>) {
        val prices = providerMapper.getPrices(Provider.PGNIG) as PgnigPrices
        if (prices.wspolczynnikKonwersji != items[PGNIG.WSP_KONW]) prices.wspolczynnikKonwersji = items[PGNIG.WSP_KONW]
        if (prices.oplataAbonamentowa != items[PGNIG.ABONAMENTOWA]) prices.oplataAbonamentowa = items[PGNIG.ABONAMENTOWA]
        if (prices.paliwoGazowe != items[PGNIG.PALIWO_GAZ]) prices.paliwoGazowe = items[PGNIG.PALIWO_GAZ]
        if (prices.dystrybucyjnaStala != items[PGNIG.DYSTR_STALA]) prices.dystrybucyjnaStala = items[PGNIG.DYSTR_STALA]
        if (prices.dystrybucyjnaZmienna != items[PGNIG.DYSTR_ZMIENNA]) prices.dystrybucyjnaZmienna = items[PGNIG.DYSTR_ZMIENNA]
    }

    fun getItemsForPgeG11(): Map<String, PriceValue> {
        val prices = providerMapper.getPrices(Provider.PGE) as PgePrices
        return mapOf(PGE.ENERGIA to PriceValue(prices.zaEnergieCzynna, PriceMeasure.KWH),
                PGE.OZE to PriceValue(prices.oplataOze, PriceMeasure.MWH),
                PGE.SKL_JAKOSC to PriceValue(prices.skladnikJakosciowy, PriceMeasure.KWH),
                PGE.SIECIOWA to PriceValue(prices.oplataSieciowa, PriceMeasure.KWH),
                PGE.PRZEJSCIOWA to PriceValue(prices.oplataPrzejsciowa, PriceMeasure.MONTH),
                PGE.STALA to PriceValue(prices.oplataStalaZaPrzesyl, PriceMeasure.MONTH),
                PGE.ABONAMENTOWA to PriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH))
    }

    fun getItemsForPgeG12(): Map<String, PriceValue> {
        val prices = providerMapper.getPrices(Provider.PGE) as PgePrices
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

    fun getItemsForTauronG11(): Map<String, PriceValue> {
        val prices = providerMapper.getPrices(Provider.TAURON) as TauronPrices
        return mapOf(TAURON.ENERGIA to PriceValue(prices.energiaElektrycznaCzynna, PriceMeasure.KWH),
                TAURON.OZE to PriceValue(prices.oplataOze, PriceMeasure.KWH),
                TAURON.DYST_ZMIENNA to PriceValue(prices.oplataDystrybucyjnaZmienna, PriceMeasure.KWH),
                TAURON.DYST_STALA to PriceValue(prices.oplataDystrybucyjnaStala, PriceMeasure.MONTH),
                TAURON.PRZEJSCIOWA to PriceValue(prices.oplataPrzejsciowa, PriceMeasure.MONTH),
                TAURON.ABONAMENTOWA to PriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH))
    }

    fun getItemsForTauronG12(): Map<String, PriceValue> {
        val prices = providerMapper.getPrices(Provider.TAURON) as TauronPrices
        return mapOf(TAURON.ENERGIA_DZIEN to PriceValue(prices.energiaElektrycznaCzynnaDzien, PriceMeasure.KWH),
                TAURON.ENERGIA_NOC to PriceValue(prices.energiaElektrycznaCzynnaNoc, PriceMeasure.KWH),
                TAURON.OZE to PriceValue(prices.oplataOze, PriceMeasure.KWH),
                TAURON.DYST_ZMIENNA_DZIEN to PriceValue(prices.oplataDystrybucyjnaZmiennaDzien, PriceMeasure.KWH),
                TAURON.DYST_ZMIENNA_NOC to PriceValue(prices.oplataDystrybucyjnaZmiennaNoc, PriceMeasure.KWH),
                TAURON.DYST_STALA to PriceValue(prices.oplataDystrybucyjnaStala, PriceMeasure.MONTH),
                TAURON.PRZEJSCIOWA to PriceValue(prices.oplataPrzejsciowa, PriceMeasure.MONTH),
                TAURON.ABONAMENTOWA to PriceValue(prices.oplataAbonamentowa, PriceMeasure.MONTH))
    }

    fun getTariff(provider: Provider): EnergyTariff {
        val prices = providerMapper.getPrices(provider) as SharedPreferencesEnergyPrices
        return EnergyTariff.valueOf(prices.tariff)
    }

    fun setDefaults(provider: Provider) {
        providerMapper.getPrices(provider).setDefault()
    }
}

@VisibleForTesting
object PGNIG {
    const val WSP_KONW = "Wspolczynnik konwersji"
    const val ABONAMENTOWA = "Opłata abonamentowa"
    const val PALIWO_GAZ = "Paliwo gazowe"
    const val DYSTR_STALA = "Dystrybucja stala"
    const val DYSTR_ZMIENNA = "Dystrybucja zmienna"
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
    const val OZE = "Opłata OZE całodobowa"
    const val DYST_ZMIENNA = "Opłata dystr. zm. całodobowa"
    const val DYST_STALA = "Opłata dystrybucyjna stała"
    const val PRZEJSCIOWA = "Opłata przejściowa"
    const val ABONAMENTOWA = "Opłata abonamentowa"

    const val ENERGIA_DZIEN = "Energia elektryczna czynna dzienna"
    const val ENERGIA_NOC = "Energia elektryczna czynna nocna"
    const val DYST_ZMIENNA_DZIEN = "Opłata dystr. zm. dzienna"
    const val DYST_ZMIENNA_NOC = "Opłata dystr. zm. nocna"
}
