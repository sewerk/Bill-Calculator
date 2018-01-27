package pl.srw.billcalculator.settings.details.dialog

import pl.srw.billcalculator.R
import pl.srw.billcalculator.data.settings.prices.PGE
import pl.srw.billcalculator.data.settings.prices.PGNIG
import pl.srw.billcalculator.data.settings.prices.TAURON

object SettingsTitleDescriptionMatcher {
    val mapping: Map<String, Int> = mapOf(
            PGE.ENERGIA to R.string.za_energie_czynna_desc,
            PGE.OZE to R.string.oplata_oze_desc,
            PGE.SKL_JAKOSC to R.string.skladnik_jakosciowy_desc,
            PGE.SIECIOWA to R.string.oplata_sieciowa_desc,
            PGE.PRZEJSCIOWA to R.string.oplata_przejsciowa_desc,
            PGE.STALA to R.string.oplata_stala_za_przesyl_desc,
            PGE.ABONAMENTOWA to R.string.oplata_abonamentowa_desc,
            PGE.ENERGIA_DZIEN to R.string.za_energie_czynna_G12dzien_desc,
            PGE.ENERGIA_NOC to R.string.za_energie_czynna_G12noc_desc,
            PGE.SIECIOWA_DZIEN to R.string.oplata_sieciowa_G12dzien_desc,
            PGE.SIECIOWA_NOC to R.string.oplata_sieciowa_G12noc_desc,

            PGNIG.WSP_KONW to R.string.wsp_konwersji_desc,
            PGNIG.ABONAMENTOWA to R.string.abonamentowa_desc,
            PGNIG.PALIWO_GAZ to R.string.paliwo_gazowe_desc,
            PGNIG.DYSTR_STALA to R.string.dystrybucyjna_stala_desc,
            PGNIG.DYSTR_ZMIENNA to R.string.dystrybucyjna_zmienna_desc,

            TAURON.ENERGIA to R.string.za_energie_czynna_desc,
            TAURON.OZE to R.string.oplata_oze_desc,
            TAURON.DYST_ZMIENNA to R.string.oplata_sieciowa_desc,
            TAURON.DYST_STALA to R.string.oplata_stala_za_przesyl_desc,
            TAURON.PRZEJSCIOWA to R.string.oplata_przejsciowa_desc,
            TAURON.ABONAMENTOWA to R.string.oplata_abonamentowa_desc,
            TAURON.ENERGIA_DZIEN to R.string.za_energie_czynna_G12dzien_desc,
            TAURON.ENERGIA_NOC to R.string.za_energie_czynna_G12noc_desc,
            TAURON.DYST_ZMIENNA_DZIEN to R.string.oplata_sieciowa_G12dzien_desc,
            TAURON.DYST_ZMIENNA_NOC to R.string.oplata_sieciowa_G12noc_desc
    )
}
