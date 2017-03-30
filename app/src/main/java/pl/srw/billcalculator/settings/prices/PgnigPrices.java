package pl.srw.billcalculator.settings.prices;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.srw.billcalculator.pojo.IPgnigPrices;

@Singleton
public class PgnigPrices extends SharedPreferencesPrices implements IPgnigPrices {
    private static final String ABONAMENTOWA = "abonamentowa";
    private static final String PALIWO_GAZOWE = "paliwo_gazowe";
    private static final String DYSTRYBUCYJNA_STALA = "dystrybucyjna_stala";
    private static final String DYSTRYBUCYJNA_ZMIENNA = "dystrybucyjna_zmienna";
    private static final String WSP_KONWERSJI = "wsp_konwersji";

    private final String abonamentowa = "8.67";
    private final String paliwo_gazowe = "0.0983";
    private final String dystrybucyjna_stala = "11.39";
    private final String dystrybucyjna_zmienna = "0.02821";
    private final String wsp_konwersji = "11.290";

    @Inject
    public PgnigPrices(SharedPreferences prefs) {
        super(prefs);
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

    @Override
    public void clear() {
        removeOplataAbonamentowa();
        removeDystrybucyjnaStala();
        removeDystrybucyjnaZmienna();
        removePaliwoGazowe();
        removeWspolczynnikKonwersji();
    }

    @Override
    public void setDefault() {
        clear();
        setOplataAbonamentowa(getOplataAbonamentowa());
        setDystrybucyjnaStala(getDystrybucyjnaStala());
        setDystrybucyjnaZmienna(getDystrybucyjnaZmienna());
        setPaliwoGazowe(getPaliwoGazowe());
        setWspolczynnikKonwersji(getWspolczynnikKonwersji());
    }

    @Override
    public void setDefaultIfNotSet() {
        if (!containsOplataAbonamentowa())
            setDefault();
    }

    public String getOplataAbonamentowa() {
        return getPref(ABONAMENTOWA, this.abonamentowa);
    }

    public void setOplataAbonamentowa(String abonamentowa) {
        setPref(ABONAMENTOWA, abonamentowa);
    }

    public boolean containsOplataAbonamentowa() {
        return containsPref(ABONAMENTOWA);
    }

    public void removeOplataAbonamentowa() {
        removePref(ABONAMENTOWA);
    }

    public String getPaliwoGazowe() {
        return getPref(PALIWO_GAZOWE, this.paliwo_gazowe);
    }

    public void setPaliwoGazowe(String paliwoGazowe) {
        setPref(PALIWO_GAZOWE, paliwoGazowe);
    }

    public boolean containsPaliwoGazowe() {
        return containsPref(PALIWO_GAZOWE);
    }

    public void removePaliwoGazowe() {
        removePref(PALIWO_GAZOWE);
    }

    public String getDystrybucyjnaStala() {
        return getPref(DYSTRYBUCYJNA_STALA, this.dystrybucyjna_stala);
    }

    public void setDystrybucyjnaStala(String dystrybucyjnaStala) {
        setPref(DYSTRYBUCYJNA_STALA, dystrybucyjnaStala);
    }

    public boolean containsDystrybucyjnaStala() {
        return containsPref(DYSTRYBUCYJNA_STALA);
    }

    public void removeDystrybucyjnaStala() {
        removePref(DYSTRYBUCYJNA_STALA);
    }

    public String getDystrybucyjnaZmienna() {
        return getPref(DYSTRYBUCYJNA_ZMIENNA, this.dystrybucyjna_zmienna);
    }

    public void setDystrybucyjnaZmienna(String dystrybucyjnaZmienna) {
        setPref(DYSTRYBUCYJNA_ZMIENNA, dystrybucyjnaZmienna);
    }

    public boolean containsDystrybucyjnaZmienna() {
        return containsPref(DYSTRYBUCYJNA_ZMIENNA);
    }

    public void removeDystrybucyjnaZmienna() {
        removePref(DYSTRYBUCYJNA_ZMIENNA);
    }

    public String getWspolczynnikKonwersji() {
        return getPref(WSP_KONWERSJI, this.wsp_konwersji);
    }

    public void setWspolczynnikKonwersji(String wspolczynnikKonwersji) {
        setPref(WSP_KONWERSJI, wspolczynnikKonwersji);
    }

    public boolean containsWspolczynnikKonwersji() {
        return containsPref(WSP_KONWERSJI);
    }

    public void removeWspolczynnikKonwersji() {
        removePref(WSP_KONWERSJI);
    }
}
