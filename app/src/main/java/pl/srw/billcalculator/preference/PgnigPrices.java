package pl.srw.billcalculator.preference;

import android.content.Context;
import android.preference.PreferenceManager;

import java.math.BigDecimal;

import hrisey.Preferences;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.pojo.IPgnigPrices;

/**
 * Created by Kamil Seweryn.
 */
public class PgnigPrices implements IPgnigPrices {
    private final WrappedPgnigPrices wrappedPgnigPrices;

//    private String oplataAbonamentowa = "8.67000";
//    private String paliwoGazowe = "0.11815";
//    private String dystrybucyjnaStala = "11.04000";
//    private String dystrybucyjnaZmienna = "0.02734";
//    private String wspolczynnikKonwersji = "11.150";

    public PgnigPrices() {
        wrappedPgnigPrices = new WrappedPgnigPrices(PreferenceManager.getDefaultSharedPreferences(BillCalculator.context));
    }

    @Override
    public String getOplataAbonamentowa() {
        return wrappedPgnigPrices.getAbonamentowa();
    }

    public void setOplataAbonamentowa(final String oplataAbonamentowa) {
        this.wrappedPgnigPrices.setAbonamentowa(oplataAbonamentowa);
    }

    @Override
    public String getPaliwoGazowe() {
        return wrappedPgnigPrices.getPaliwo_gazowe();
    }

    public void setPaliwoGazowe(final String paliwoGazowe) {
        this.wrappedPgnigPrices.setPaliwo_gazowe(paliwoGazowe);
    }

    @Override
    public String getDystrybucyjnaStala() {
        return wrappedPgnigPrices.getDystrybucyjna_stala();
    }

    public void setDystrybucyjnaStala(final String dystrybucyjnaStala) {
        this.wrappedPgnigPrices.setDystrybucyjna_stala(dystrybucyjnaStala);
    }

    @Override
    public String getDystrybucyjnaZmienna() {
        return wrappedPgnigPrices.getDystrybucyjna_zmienna();
    }

    public void setDystrybucyjnaZmienna(final String dystrybucyjnaZmienna) {
        this.wrappedPgnigPrices.setDystrybucyjna_zmienna(dystrybucyjnaZmienna);
    }

    @Override
    public String getWspolczynnikKonwersji() {
        return wrappedPgnigPrices.getWsp_konwersji();
    }

    public void setWspolczynnikKonwersji(final String wspolczynnikKonwersji) {
        this.wrappedPgnigPrices.setWsp_konwersji(wspolczynnikKonwersji);
    }

    public pl.srw.billcalculator.db.PgnigPrices convertToDb() {
        pl.srw.billcalculator.db.PgnigPrices dbPrices = new pl.srw.billcalculator.db.PgnigPrices();
        dbPrices.setDystrybucyjnaStala(getDystrybucyjnaStala());
        dbPrices.setDystrybucyjnaZmienna(getDystrybucyjnaZmienna());
        dbPrices.setOplataAbonamentowa(getOplataAbonamentowa());
        dbPrices.setPaliwoGazowe(getPaliwoGazowe());
        dbPrices.setWspolczynnikKonwersji(getWspolczynnikKonwersji());
        return dbPrices;
    }

    public void clear() {
        wrappedPgnigPrices.removeAbonamentowa();
        wrappedPgnigPrices.removeDystrybucyjna_stala();
        wrappedPgnigPrices.removeDystrybucyjna_zmienna();
        wrappedPgnigPrices.removePaliwo_gazowe();
        wrappedPgnigPrices.removeWsp_konwersji();
    }

    @Preferences
    private class WrappedPgnigPrices {
        private String abonamentowa = "8.67000";
        private String paliwo_gazowe = "0.11815";
        private String dystrybucyjna_stala = "11.04000";
        private String dystrybucyjna_zmienna = "0.02734";
        private String wsp_konwersji = "11.150";
    }
}
