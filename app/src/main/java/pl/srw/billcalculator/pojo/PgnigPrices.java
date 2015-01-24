package pl.srw.billcalculator.pojo;

import java.math.BigDecimal;

import hrisey.Preferences;

/**
 * Created by Kamil Seweryn.
 */
@Preferences
public class PgnigPrices implements IPgnigPrices {

    private String oplataAbonamentowa = "8.67000";
    private String paliwoGazowe = "0.11815";
    private String dystrybucyjnaStala = "11.04000";
    private String dystrybucyjnaZmienna = "0.02734";
    private String wspolczynnikKonwersji = "11.150";

    public pl.srw.billcalculator.db.PgnigPrices convertToDb() {
        pl.srw.billcalculator.db.PgnigPrices dbPrices = new pl.srw.billcalculator.db.PgnigPrices();
        dbPrices.setDystrybucyjnaStala(dystrybucyjnaStala);
        dbPrices.setDystrybucyjnaZmienna(dystrybucyjnaZmienna);
        dbPrices.setOplataAbonamentowa(oplataAbonamentowa);
        dbPrices.setPaliwoGazowe(paliwoGazowe);
        dbPrices.setWspolczynnikKonwersji(wspolczynnikKonwersji);
        return dbPrices;
    }
}
