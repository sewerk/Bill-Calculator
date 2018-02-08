package pl.srw.billcalculator.pojo

import pl.srw.billcalculator.db.Prices

interface IPgnigPrices : Prices {

    val oplataAbonamentowa: String
    val paliwoGazowe: String
    val dystrybucyjnaStala: String
    val dystrybucyjnaZmienna: String
    val wspolczynnikKonwersji: String
}
