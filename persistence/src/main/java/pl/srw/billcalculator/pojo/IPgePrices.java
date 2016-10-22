package pl.srw.billcalculator.pojo;

import pl.srw.billcalculator.db.Prices;

/**
 * Created by Kamil Seweryn.
 */
public interface IPgePrices extends Prices {
    
    String getZaEnergieCzynna();
    String getSkladnikJakosciowy();
    String getOplataSieciowa();
    String getOplataPrzejsciowa();
    String getOplataStalaZaPrzesyl();
    String getOplataAbonamentowa();
    String getOplataOze();

    String getZaEnergieCzynnaDzien();
    String getZaEnergieCzynnaNoc();
    String getOplataSieciowaDzien();
    String getOplataSieciowaNoc();
}
