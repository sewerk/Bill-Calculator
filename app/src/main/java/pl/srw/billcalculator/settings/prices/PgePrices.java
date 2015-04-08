package pl.srw.billcalculator.settings.prices;

import android.preference.PreferenceManager;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.pojo.IPgePrices;


/**
 * Created by Kamil Seweryn.
 */
public class PgePrices implements IPgePrices {

//    private String zaEnergieCzynna;
//    private String skladnikJakosciowy;
//    private String oplataSieciowa;
//    private String oplataPrzejsciowa;
//    private String oplataStalaZaPrzesyl;
//    private String oplataAbonamentowa;
//
//    private String zaEnergieCzynnaDzien;
//    private String zaEnergieCzynnaNoc;
//    private String oplataSieciowaDzien;
//    private String oplataSieciowaNoc;

    private final WrappedPgePrices wrappedPgePrices;

    public PgePrices() {
        wrappedPgePrices = new WrappedPgePrices(PreferenceManager.getDefaultSharedPreferences(BillCalculator.context));
    }

    @Override
    public String getZaEnergieCzynna() {
        return wrappedPgePrices.getCena_za_energie_czynna();
    }

    public void setZaEnergieCzynna(final String zaEnergieCzynna) {
        this.wrappedPgePrices.setCena_za_energie_czynna(zaEnergieCzynna);
    }

    @Override
    public String getSkladnikJakosciowy() {
        return wrappedPgePrices.getCena_skladnik_jakosciowy();
    }

    public void setSkladnikJakosciowy(final String skladnikJakosciowy) {
        this.wrappedPgePrices.setCena_skladnik_jakosciowy(skladnikJakosciowy);
    }

    @Override
    public String getOplataSieciowa() {
        return wrappedPgePrices.getCena_oplata_sieciowa();
    }

    public void setOplataSieciowa(final String oplataSieciowa) {
        this.wrappedPgePrices.setCena_oplata_sieciowa(oplataSieciowa);
    }

    @Override
    public String getOplataPrzejsciowa() {
        return wrappedPgePrices.getCena_oplata_przejsciowa();
    }

    public void setOplataPrzejsciowa(final String oplataPrzejsciowa) {
        this.wrappedPgePrices.setCena_oplata_przejsciowa(oplataPrzejsciowa);
    }

    @Override
    public String getOplataStalaZaPrzesyl() {
        return wrappedPgePrices.getCena_oplata_stala_za_przesyl();
    }

    public void setOplataStalaZaPrzesyl(final String oplataStalaZaPrzesyl) {
        this.wrappedPgePrices.setCena_oplata_stala_za_przesyl(oplataStalaZaPrzesyl);
    }

    @Override
    public String getOplataAbonamentowa() {
        return wrappedPgePrices.getCena_oplata_abonamentowa();
    }

    public void setOplataAbonamentowa(final String oplataAbonamentowa) {
        this.wrappedPgePrices.setCena_oplata_abonamentowa(oplataAbonamentowa);
    }

    @Override
    public String getZaEnergieCzynnaDzien() {
        return wrappedPgePrices.getCena_za_energie_czynna_G12dzien();
    }

    public void setZaEnergieCzynnaDzien(final String zaEnergieCzynnaDzien) {
        this.wrappedPgePrices.setCena_za_energie_czynna_G12dzien(zaEnergieCzynnaDzien);
    }

    @Override
    public String getZaEnergieCzynnaNoc() {
        return wrappedPgePrices.getCena_za_energie_czynna_G12noc();
    }

    public void setZaEnergieCzynnaNoc(final String zaEnergieCzynnaNoc) {
        this.wrappedPgePrices.setCena_za_energie_czynna_G12noc(zaEnergieCzynnaNoc);
    }

    @Override
    public String getOplataSieciowaDzien() {
        return wrappedPgePrices.getCena_oplata_sieciowa_G12dzien();
    }

    public void setOplataSieciowaDzien(final String oplataSieciowaDzien) {
        this.wrappedPgePrices.setCena_oplata_sieciowa_G12dzien(oplataSieciowaDzien);
    }

    @Override
    public String getOplataSieciowaNoc() {
        return wrappedPgePrices.getCena_oplata_sieciowa_G12noc();
    }

    public void setOplataSieciowaNoc(final String oplataSieciowaNoc) {
        this.wrappedPgePrices.setCena_oplata_sieciowa_G12noc(oplataSieciowaNoc);
    }

    public pl.srw.billcalculator.db.PgePrices convertToDb() {
        pl.srw.billcalculator.db.PgePrices dbPrices = new pl.srw.billcalculator.db.PgePrices();
        dbPrices.setOplataAbonamentowa(getOplataAbonamentowa());
        dbPrices.setOplataPrzejsciowa(getOplataPrzejsciowa());
        dbPrices.setOplataSieciowa(getOplataSieciowa());
        dbPrices.setOplataStalaZaPrzesyl(getOplataStalaZaPrzesyl());
        dbPrices.setSkladnikJakosciowy(getSkladnikJakosciowy());
        dbPrices.setZaEnergieCzynna(getZaEnergieCzynna());

        dbPrices.setOplataSieciowaDzien(getOplataSieciowaDzien());
        dbPrices.setOplataSieciowaNoc(getOplataSieciowaNoc());
        dbPrices.setZaEnergieCzynnaDzien(getZaEnergieCzynnaDzien());
        dbPrices.setZaEnergieCzynnaNoc(getZaEnergieCzynnaNoc());
        return dbPrices;
    }

    public void clear() {
        wrappedPgePrices.removeCena_za_energie_czynna();
        wrappedPgePrices.removeCena_za_energie_czynna_G12dzien();
        wrappedPgePrices.removeCena_za_energie_czynna_G12noc();
        wrappedPgePrices.removeCena_oplata_sieciowa();
        wrappedPgePrices.removeCena_oplata_sieciowa_G12dzien();
        wrappedPgePrices.removeCena_oplata_sieciowa_G12noc();
        wrappedPgePrices.removeCena_skladnik_jakosciowy();
        wrappedPgePrices.removeCena_oplata_przejsciowa();
        wrappedPgePrices.removeCena_oplata_stala_za_przesyl();
        wrappedPgePrices.removeCena_oplata_abonamentowa();
    }

    public void setDefault() {
        clear();
        wrappedPgePrices.setCena_za_energie_czynna(wrappedPgePrices.getCena_za_energie_czynna());
        wrappedPgePrices.setCena_za_energie_czynna_G12dzien(wrappedPgePrices.getCena_za_energie_czynna_G12dzien());
        wrappedPgePrices.setCena_za_energie_czynna_G12noc(wrappedPgePrices.getCena_za_energie_czynna_G12noc());
        wrappedPgePrices.setCena_oplata_sieciowa(wrappedPgePrices.getCena_oplata_sieciowa());
        wrappedPgePrices.setCena_oplata_sieciowa_G12dzien(wrappedPgePrices.getCena_oplata_sieciowa_G12dzien());
        wrappedPgePrices.setCena_oplata_sieciowa_G12noc(wrappedPgePrices.getCena_oplata_sieciowa_G12noc());
        wrappedPgePrices.setCena_skladnik_jakosciowy(wrappedPgePrices.getCena_skladnik_jakosciowy());
        wrappedPgePrices.setCena_oplata_przejsciowa(wrappedPgePrices.getCena_oplata_przejsciowa());
        wrappedPgePrices.setCena_oplata_stala_za_przesyl(wrappedPgePrices.getCena_oplata_stala_za_przesyl());
        wrappedPgePrices.setCena_oplata_abonamentowa(wrappedPgePrices.getCena_oplata_abonamentowa());
    }

    @hrisey.Preferences
    private class WrappedPgePrices {
        private String cena_za_energie_czynna = "0.2553";
        private String cena_za_energie_czynna_G12dzien = "0.2861";
        private String cena_za_energie_czynna_G12noc = "0.1917";
        private String cena_oplata_sieciowa = "0.2170";
        private String cena_oplata_sieciowa_G12dzien = "0.2417";
        private String cena_oplata_sieciowa_G12noc = "0.0836";
        private String cena_skladnik_jakosciowy = "0.0115";
        private String cena_oplata_przejsciowa = "1.04";
        private String cena_oplata_stala_za_przesyl = "1.95";
        private String cena_oplata_abonamentowa = "5.20";
    }
}