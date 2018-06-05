package pl.srw.billcalculator.pojo

import pl.srw.billcalculator.db.Prices

interface IPgePrices : Prices {

    val zaEnergieCzynna: String
    val skladnikJakosciowy: String
    val oplataSieciowa: String
    val oplataPrzejsciowa: String
    val oplataStalaZaPrzesyl: String
    val oplataAbonamentowa: String
    val oplataOze: String
    val oplataHandlowa: String

    val zaEnergieCzynnaDzien: String
    val zaEnergieCzynnaNoc: String
    val oplataSieciowaDzien: String
    val oplataSieciowaNoc: String
}
