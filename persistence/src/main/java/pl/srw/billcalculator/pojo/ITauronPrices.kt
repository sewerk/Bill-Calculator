package pl.srw.billcalculator.pojo

import pl.srw.billcalculator.db.Prices

interface ITauronPrices : Prices {

    val energiaElektrycznaCzynna: String
    val oplataDystrybucyjnaZmienna: String
    val oplataDystrybucyjnaStala: String
    val oplataPrzejsciowa: String
    val oplataAbonamentowa: String
    val oplataOze: String

    val oplataDystrybucyjnaZmiennaNoc: String
    val oplataDystrybucyjnaZmiennaDzien: String
    val energiaElektrycznaCzynnaNoc: String
    val energiaElektrycznaCzynnaDzien: String
}
