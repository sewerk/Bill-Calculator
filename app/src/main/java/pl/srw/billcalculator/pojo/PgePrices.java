package pl.srw.billcalculator.pojo;

import hrisey.Preferences;

/**
 * Created by Kamil Seweryn.
 */
@Preferences
public class PgePrices implements IPgePrices {

    private String zaEnergieCzynna = "0.2539";
    private String skladnikJakosciowy = "0.0108";
    private String oplataSieciowa = "0.2192";
    private String oplataPrzejsciowa = "0.77";
    private String oplataStalaZaPrzesyl = "1.78";
    private String oplataAbonamentowa = "5.31";

    private String zaEnergieCzynnaDzien = "0.2846";
    private String zaEnergieCzynnaNoc = "0.1906";
    private String oplataSieciowaDzien = "0.245";
    private String oplataSieciowaNoc = "0.0853";
    
    public pl.srw.billcalculator.db.PgePrices convertToDb() {
        pl.srw.billcalculator.db.PgePrices dbPrices = new pl.srw.billcalculator.db.PgePrices();
        dbPrices.setOplataAbonamentowa(oplataAbonamentowa);
        dbPrices.setOplataPrzejsciowa(oplataPrzejsciowa);
        dbPrices.setOplataSieciowa(oplataSieciowa);
        dbPrices.setOplataStalaZaPrzesyl(oplataStalaZaPrzesyl);
        dbPrices.setSkladnikJakosciowy(skladnikJakosciowy);
        dbPrices.setZaEnergieCzynna(zaEnergieCzynna);

        dbPrices.setOplataSieciowaDzien(oplataSieciowaDzien);
        dbPrices.setOplataSieciowaNoc(oplataSieciowaNoc);
        dbPrices.setZaEnergieCzynnaDzien(zaEnergieCzynnaDzien);
        dbPrices.setZaEnergieCzynnaNoc(zaEnergieCzynnaNoc);
        return dbPrices;
    }
}