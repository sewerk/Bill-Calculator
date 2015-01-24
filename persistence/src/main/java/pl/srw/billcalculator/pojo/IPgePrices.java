package pl.srw.billcalculator.pojo;

/**
 * Created by Kamil Seweryn.
 */
public interface IPgePrices {
    
    String getZaEnergieCzynna();
    String getSkladnikJakosciowy();
    String getOplataSieciowa();
    String getOplataPrzejsciowa();
    String getOplataStalaZaPrzesyl();
    String getOplataAbonamentowa();

    String getZaEnergieCzynnaDzien();
    String getZaEnergieCzynnaNoc();
    String getOplataSieciowaDzien();
    String getOplataSieciowaNoc();
}
