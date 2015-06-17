package pl.srw.billcalculator.settings.prices;

import android.preference.PreferenceManager;

import hrisey.Preferences;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.pojo.ITauronPrices;

/**
 * Created by kseweryn on 13.04.15.
 */
@Preferences
public class TauronPrices implements ITauronPrices {

    private String energiaElektrycznaCzynna = "0.2547";
    private String oplataDystrybucyjnaZmienna = "0.1913";
    private String oplataDystrybucyjnaStala = "1.55";
    private String oplataPrzejsciowa = "1.04";
    private String oplataAbonamentowa = "4.80";

    private String energiaElektrycznaCzynnaDzien = "0.3134";
    private String oplataDystrybucyjnaZmiennaDzien = "0.1990";
    private String energiaElektrycznaCzynnaNoc = "0.1628";
    private String oplataDystrybucyjnaZmiennaNoc = "0.0745";

    public TauronPrices() {
        this(PreferenceManager.getDefaultSharedPreferences(BillCalculator.context));
    }

    public pl.srw.billcalculator.db.TauronPrices convertToDb() {
        pl.srw.billcalculator.db.TauronPrices dbPrices = new pl.srw.billcalculator.db.TauronPrices();
        dbPrices.setEnergiaElektrycznaCzynna(getEnergiaElektrycznaCzynna());
        dbPrices.setOplataDystrybucyjnaZmienna(getOplataDystrybucyjnaZmienna());
        dbPrices.setOplataDystrybucyjnaStala(getOplataDystrybucyjnaStala());
        dbPrices.setOplataPrzejsciowa(getOplataPrzejsciowa());
        dbPrices.setOplataAbonamentowa(getOplataAbonamentowa());

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
        setEnergiaElektrycznaCzynnaDzien(getEnergiaElektrycznaCzynnaDzien());
        setOplataDystrybucyjnaZmiennaDzien(getOplataDystrybucyjnaZmiennaDzien());
        setEnergiaElektrycznaCzynnaNoc(getEnergiaElektrycznaCzynnaNoc());
        setOplataDystrybucyjnaZmiennaNoc(getOplataDystrybucyjnaZmiennaNoc());
    }

    public void init() {
        if (!containsEnergiaElektrycznaCzynna())
            setDefault();
    }
}
