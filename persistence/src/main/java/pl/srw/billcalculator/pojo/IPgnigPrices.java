package pl.srw.billcalculator.pojo;

/**
 * Created by Kamil Seweryn.
 */
public interface IPgnigPrices extends IPrices {

    String getOplataAbonamentowa();
    String getPaliwoGazowe();
    String getDystrybucyjnaStala();
    String getDystrybucyjnaZmienna();
    String getWspolczynnikKonwersji();
}
