package pl.srw.billcalculator.settings.prices;

import pl.srw.billcalculator.pojo.ITauronPrices;

public class TauronPrices extends SharedPreferencesPrices implements ITauronPrices {
    private static final String ENERGIA_ELEKTRYCZNA_CZYNNA = "energiaElektrycznaCzynna";
    private static final String OPLATA_DYSTRYBUCYJNA_ZMIENNA = "oplataDystrybucyjnaZmienna";
    private static final String OPLATA_DYSTRYBUCYJNA_STALA = "oplataDystrybucyjnaStala";
    private static final String OPLATA_PRZEJSCIOWA = "oplataPrzejsciowa";
    private static final String OPLATA_ABONAMENTOWA = "oplataAbonamentowa";
    private static final String OPLATA_OZE = "oplataOze";
    private static final String ENERGIA_ELEKTRYCZNA_CZYNNA_DZIEN = "energiaElektrycznaCzynnaDzien";
    private static final String OPLATA_DYSTRYBUCYJNA_ZMIENNA_DZIEN = "oplataDystrybucyjnaZmiennaDzien";
    private static final String OPLATA_DYSTRYBUCYJNA_ZMIENNA_NOC = "oplataDystrybucyjnaZmiennaNoc";
    private static final String ENERGIA_ELEKTRYCZNA_CZYNNA_NOC = "energiaElektrycznaCzynnaNoc";

    private final String energiaElektrycznaCzynna = "0.2547";
    private final String oplataDystrybucyjnaZmienna = "0.1913";
    private final String oplataDystrybucyjnaStala = "1.55";
    private final String oplataPrzejsciowa = "1.04";
    private final String oplataAbonamentowa = "4.80";
    private final String oplataOze = "0.00251";

    private final String energiaElektrycznaCzynnaDzien = "0.3134";
    private final String oplataDystrybucyjnaZmiennaDzien = "0.1990";
    private final String energiaElektrycznaCzynnaNoc = "0.1628";
    private final String oplataDystrybucyjnaZmiennaNoc = "0.0745";

    public pl.srw.billcalculator.db.TauronPrices convertToDb() {
        pl.srw.billcalculator.db.TauronPrices dbPrices = new pl.srw.billcalculator.db.TauronPrices();
        dbPrices.setEnergiaElektrycznaCzynna(getEnergiaElektrycznaCzynna());
        dbPrices.setOplataDystrybucyjnaZmienna(getOplataDystrybucyjnaZmienna());
        dbPrices.setOplataDystrybucyjnaStala(getOplataDystrybucyjnaStala());
        dbPrices.setOplataPrzejsciowa(getOplataPrzejsciowa());
        dbPrices.setOplataAbonamentowa(getOplataAbonamentowa());
        dbPrices.setOplataOze(getOplataOze());

        dbPrices.setEnergiaElektrycznaCzynnaDzien(getEnergiaElektrycznaCzynnaDzien());
        dbPrices.setOplataDystrybucyjnaZmiennaDzien(getOplataDystrybucyjnaZmiennaDzien());
        dbPrices.setEnergiaElektrycznaCzynnaNoc(getEnergiaElektrycznaCzynnaNoc());
        dbPrices.setOplataDystrybucyjnaZmiennaNoc(getOplataDystrybucyjnaZmiennaNoc());
        return dbPrices;
    }

    public void clear() {
        removeEnergiaElektrycznaCzynna();
        removeOplataDystrybucyjnaZmienna();
        removeOplataDystrybucyjnaStala();
        removeOplataPrzejsciowa();
        removeOplataAbonamentowa();
        removeOplataOze();
        removeEnergiaElektrycznaCzynnaDzien();
        removeEnergiaElektrycznaCzynnaNoc();
        removeOplataDystrybucyjnaZmiennaDzien();
        removeOplataDystrybucyjnaZmiennaNoc();
    }

    public void setDefault() {
        clear();
        setEnergiaElektrycznaCzynna(getEnergiaElektrycznaCzynna());
        setOplataDystrybucyjnaZmienna(getOplataDystrybucyjnaZmienna());
        setOplataDystrybucyjnaStala(getOplataDystrybucyjnaStala());
        setOplataPrzejsciowa(getOplataPrzejsciowa());
        setOplataAbonamentowa(getOplataAbonamentowa());
        setOplataOze(getOplataOze());
        setEnergiaElektrycznaCzynnaDzien(getEnergiaElektrycznaCzynnaDzien());
        setOplataDystrybucyjnaZmiennaDzien(getOplataDystrybucyjnaZmiennaDzien());
        setEnergiaElektrycznaCzynnaNoc(getEnergiaElektrycznaCzynnaNoc());
        setOplataDystrybucyjnaZmiennaNoc(getOplataDystrybucyjnaZmiennaNoc());
    }

    public void init() {
        if (!containsEnergiaElektrycznaCzynna())
            setDefault();
        else if (!containsOplataOze())
            setOplataOze(getOplataOze());
    }

    public String getEnergiaElektrycznaCzynna() {
        return getPref(ENERGIA_ELEKTRYCZNA_CZYNNA, this.energiaElektrycznaCzynna);
    }

    public void setEnergiaElektrycznaCzynna(String energiaElektrycznaCzynna) {
        setPref(ENERGIA_ELEKTRYCZNA_CZYNNA, energiaElektrycznaCzynna);
    }

    public boolean containsEnergiaElektrycznaCzynna() {
        return containsPref(ENERGIA_ELEKTRYCZNA_CZYNNA);
    }

    public void removeEnergiaElektrycznaCzynna() {
        removePref(ENERGIA_ELEKTRYCZNA_CZYNNA);
    }

    public String getOplataDystrybucyjnaZmienna() {
        return getPref(OPLATA_DYSTRYBUCYJNA_ZMIENNA, this.oplataDystrybucyjnaZmienna);
    }

    public void setOplataDystrybucyjnaZmienna(String oplataDystrybucyjnaZmienna) {
        setPref(OPLATA_DYSTRYBUCYJNA_ZMIENNA, oplataDystrybucyjnaZmienna);
    }

    public boolean containsOplataDystrybucyjnaZmienna() {
        return containsPref(OPLATA_DYSTRYBUCYJNA_ZMIENNA);
    }

    public void removeOplataDystrybucyjnaZmienna() {
        removePref(OPLATA_DYSTRYBUCYJNA_ZMIENNA);
    }

    public String getOplataDystrybucyjnaStala() {
        return getPref(OPLATA_DYSTRYBUCYJNA_STALA, this.oplataDystrybucyjnaStala);
    }

    public void setOplataDystrybucyjnaStala(String oplataDystrybucyjnaStala) {
        setPref(OPLATA_DYSTRYBUCYJNA_STALA, oplataDystrybucyjnaStala);
    }

    public boolean containsOplataDystrybucyjnaStala() {
        return containsPref(OPLATA_DYSTRYBUCYJNA_STALA);
    }

    public void removeOplataDystrybucyjnaStala() {
        removePref(OPLATA_DYSTRYBUCYJNA_STALA);
    }

    public String getOplataPrzejsciowa() {
        return getPref(OPLATA_PRZEJSCIOWA, this.oplataPrzejsciowa);
    }

    public void setOplataPrzejsciowa(String oplataPrzejsciowa) {
        setPref(OPLATA_PRZEJSCIOWA, oplataPrzejsciowa);
    }

    public boolean containsOplataPrzejsciowa() {
        return containsPref(OPLATA_PRZEJSCIOWA);
    }

    public void removeOplataPrzejsciowa() {
        removePref(OPLATA_PRZEJSCIOWA);
    }

    public String getOplataAbonamentowa() {
        return getPref(OPLATA_ABONAMENTOWA, this.oplataAbonamentowa);
    }

    public void setOplataAbonamentowa(String oplataAbonamentowa) {
        setPref(OPLATA_ABONAMENTOWA, oplataAbonamentowa);
    }

    public boolean containsOplataAbonamentowa() {
        return containsPref(OPLATA_ABONAMENTOWA);
    }

    public void removeOplataAbonamentowa() {
        removePref(OPLATA_ABONAMENTOWA);
    }

    public String getOplataOze() {
        return getPref(OPLATA_OZE, this.oplataOze);
    }

    public void setOplataOze(String oplataOze) {
        setPref(OPLATA_OZE, oplataOze);
    }

    public boolean containsOplataOze() {
        return containsPref(OPLATA_OZE);
    }

    public void removeOplataOze() {
        removePref(OPLATA_OZE);
    }

    public String getEnergiaElektrycznaCzynnaDzien() {
        return getPref(ENERGIA_ELEKTRYCZNA_CZYNNA_DZIEN, this.energiaElektrycznaCzynnaDzien);
    }

    public void setEnergiaElektrycznaCzynnaDzien(String energiaElektrycznaCzynnaDzien) {
        setPref(ENERGIA_ELEKTRYCZNA_CZYNNA_DZIEN, energiaElektrycznaCzynnaDzien);
    }

    public boolean containsEnergiaElektrycznaCzynnaDzien() {
        return containsPref(ENERGIA_ELEKTRYCZNA_CZYNNA_DZIEN);
    }

    public void removeEnergiaElektrycznaCzynnaDzien() {
        removePref(ENERGIA_ELEKTRYCZNA_CZYNNA_DZIEN);
    }

    public String getOplataDystrybucyjnaZmiennaDzien() {
        return getPref(OPLATA_DYSTRYBUCYJNA_ZMIENNA_DZIEN, this.oplataDystrybucyjnaZmiennaDzien);
    }

    public void setOplataDystrybucyjnaZmiennaDzien(String oplataDystrybucyjnaZmiennaDzien) {
        setPref(OPLATA_DYSTRYBUCYJNA_ZMIENNA_DZIEN, oplataDystrybucyjnaZmiennaDzien);
    }

    public boolean containsOplataDystrybucyjnaZmiennaDzien() {
        return containsPref(OPLATA_DYSTRYBUCYJNA_ZMIENNA_DZIEN);
    }

    public void removeOplataDystrybucyjnaZmiennaDzien() {
        removePref(OPLATA_DYSTRYBUCYJNA_ZMIENNA_DZIEN);
    }

    public String getEnergiaElektrycznaCzynnaNoc() {
        return getPref(ENERGIA_ELEKTRYCZNA_CZYNNA_NOC, this.energiaElektrycznaCzynnaNoc);
    }

    public void setEnergiaElektrycznaCzynnaNoc(String energiaElektrycznaCzynnaNoc) {
        setPref(ENERGIA_ELEKTRYCZNA_CZYNNA_NOC, energiaElektrycznaCzynnaNoc);
    }

    public boolean containsEnergiaElektrycznaCzynnaNoc() {
        return containsPref(ENERGIA_ELEKTRYCZNA_CZYNNA_NOC);
    }

    public void removeEnergiaElektrycznaCzynnaNoc() {
        removePref(ENERGIA_ELEKTRYCZNA_CZYNNA_NOC);
    }

    public String getOplataDystrybucyjnaZmiennaNoc() {
        return getPref(OPLATA_DYSTRYBUCYJNA_ZMIENNA_NOC, this.oplataDystrybucyjnaZmiennaNoc);
    }

    public void setOplataDystrybucyjnaZmiennaNoc(String oplataDystrybucyjnaZmiennaNoc) {
        setPref(OPLATA_DYSTRYBUCYJNA_ZMIENNA_NOC, oplataDystrybucyjnaZmiennaNoc);
    }

    public boolean containsOplataDystrybucyjnaZmiennaNoc() {
        return containsPref(OPLATA_DYSTRYBUCYJNA_ZMIENNA_NOC);
    }

    public void removeOplataDystrybucyjnaZmiennaNoc() {
        removePref(OPLATA_DYSTRYBUCYJNA_ZMIENNA_NOC);
    }
}
