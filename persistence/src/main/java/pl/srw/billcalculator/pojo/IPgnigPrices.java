package pl.srw.billcalculator.pojo;

import pl.srw.billcalculator.db.Prices;

/**
 * Created by Kamil Seweryn.
 */
public interface IPgnigPrices extends Prices {

    String getOplataAbonamentowa();
    String getPaliwoGazowe();
    String getDystrybucyjnaStala();
    String getDystrybucyjnaZmienna();
    String getWspolczynnikKonwersji();
}
