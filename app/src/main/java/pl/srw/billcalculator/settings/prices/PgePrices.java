package pl.srw.billcalculator.settings.prices;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.srw.billcalculator.pojo.IPgePrices;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
@Singleton
public class PgePrices extends SharedPreferencesEnergyPrices implements IPgePrices {

    public static final String KEY_TARIFF = "pge_tariff";
    private static final String CENA_ZA_ENERGIE_CZYNNA = "cena_za_energie_czynna";
    private static final String CENA_ZA_ENERGIE_CZYNNA_G_12_DZIEN = "cena_za_energie_czynna_G12dzien";
    private static final String CENA_ZA_ENERGIE_CZYNNA_G_12_NOC = "cena_za_energie_czynna_G12noc";
    private static final String CENA_OPLATA_SIECIOWA = "cena_oplata_sieciowa";
    private static final String CENA_OPLATA_SIECIOWA_G_12_DZIEN = "cena_oplata_sieciowa_G12dzien";
    private static final String CENA_OPLATA_SIECIOWA_G_12_NOC = "cena_oplata_sieciowa_G12noc";
    private static final String CENA_SKLADNIK_JAKOSCIOWY = "cena_skladnik_jakosciowy";
    private static final String CENA_OPLATA_PRZEJSCIOWA = "cena_oplata_przejsciowa";
    private static final String CENA_OPLATA_STALA_ZA_PRZESYL = "cena_oplata_stala_za_przesyl";
    private static final String CENA_OPLATA_ABONAMENTOWA = "cena_oplata_abonamentowa";
    private static final String CENA_OPLATA_OZE = "cena_oplata_oze";

    private final String pge_tariff = TARIFF_G11;
    private final String cena_za_energie_czynna = "0.2410";
    private final String cena_za_energie_czynna_G12dzien = "0.2760";
    private final String cena_za_energie_czynna_G12noc = "0.1770";
    private final String cena_oplata_sieciowa = "0.2075";
    private final String cena_oplata_sieciowa_G12dzien = "0.2360";
    private final String cena_oplata_sieciowa_G12noc = "0.0720";
    private final String cena_skladnik_jakosciowy = "0.0127";
    private final String cena_oplata_przejsciowa = "1.90";
    private final String cena_oplata_stala_za_przesyl = "1.96";
    private final String cena_oplata_abonamentowa = "5.10";
    private final String cena_oplata_oze = "3.70";

    @Inject
    public PgePrices(SharedPreferences prefs) {
        super(prefs);
    }

    public pl.srw.billcalculator.db.PgePrices convertToDb() {
        pl.srw.billcalculator.db.PgePrices dbPrices = new pl.srw.billcalculator.db.PgePrices();
        dbPrices.setOplataAbonamentowa(getOplataAbonamentowa());
        dbPrices.setOplataPrzejsciowa(getOplataPrzejsciowa());
        dbPrices.setOplataSieciowa(getOplataSieciowa());
        dbPrices.setOplataStalaZaPrzesyl(getOplataStalaZaPrzesyl());
        dbPrices.setSkladnikJakosciowy(getSkladnikJakosciowy());
        dbPrices.setZaEnergieCzynna(getZaEnergieCzynna());
        dbPrices.setOplataOze(getOplataOze());

        dbPrices.setOplataSieciowaDzien(getOplataSieciowaDzien());
        dbPrices.setOplataSieciowaNoc(getOplataSieciowaNoc());
        dbPrices.setZaEnergieCzynnaDzien(getZaEnergieCzynnaDzien());
        dbPrices.setZaEnergieCzynnaNoc(getZaEnergieCzynnaNoc());
        return dbPrices;
    }

    private void clear() {
        removeTariff();
        removeZaEnergieCzynna();
        removeZaEnergieCzynnaDzien();
        removeZaEnergieCzynnaNoc();
        removeOplataSieciowa();
        removeOplataSieciowaDzien();
        removeOplataSieciowaNoc();
        removeSkladnikJakosciowy();
        removeOplataPrzejsciowa();
        removeOplataStalaZaPrzesyl();
        removeOplataAbonamentowa();
        removeOplataOze();
    }

    @Override
    public void setDefault() {
        clear();
        setTariff(getTariff());
        setZaEnergieCzynna(getZaEnergieCzynna());
        setZaEnergieCzynnaDzien(getZaEnergieCzynnaDzien());
        setZaEnergieCzynnaNoc(getZaEnergieCzynnaNoc());
        setOplataSieciowa(getOplataSieciowa());
        setOplataSieciowaDzien(getOplataSieciowaDzien());
        setOplataSieciowaNoc(getOplataSieciowaNoc());
        setSkladnikJakosciowy(getSkladnikJakosciowy());
        setOplataPrzejsciowa(getOplataPrzejsciowa());
        setOplataStalaZaPrzesyl(getOplataStalaZaPrzesyl());
        setOplataAbonamentowa(getOplataAbonamentowa());
        setOplataOze(getOplataOze());
    }

    @Override
    public void setDefaultIfNotSet() {
        if (!containsZaEnergieCzynna())
            setDefault();
        else if (!containsOplataOze())
            setOplataOze(getOplataOze());
    }

    @Override
    public @TariffOption String getTariff() {
        //noinspection WrongConstant
        return getPref(KEY_TARIFF, this.pge_tariff);
    }

    public void setTariff(@TariffOption String tariff) {
        setPref(KEY_TARIFF, tariff);
    }

    public boolean containsTariff() {
        return containsPref(KEY_TARIFF);
    }

    public void removeTariff() {
        removePref(KEY_TARIFF);
    }

    public String getZaEnergieCzynna() {
        return getPref(CENA_ZA_ENERGIE_CZYNNA, this.cena_za_energie_czynna);
    }

    public void setZaEnergieCzynna(String cenaZaEnergieCzynna) {
        setPref(CENA_ZA_ENERGIE_CZYNNA, cenaZaEnergieCzynna);
    }

    public boolean containsZaEnergieCzynna() {
        return containsPref(CENA_ZA_ENERGIE_CZYNNA);
    }

    public void removeZaEnergieCzynna() {
        removePref(CENA_ZA_ENERGIE_CZYNNA);
    }

    public String getZaEnergieCzynnaDzien() {
        return getPref(CENA_ZA_ENERGIE_CZYNNA_G_12_DZIEN, this.cena_za_energie_czynna_G12dzien);
    }

    public void setZaEnergieCzynnaDzien(String cenaZaEnergieCzynnaG12Dzien) {
        setPref(CENA_ZA_ENERGIE_CZYNNA_G_12_DZIEN, cenaZaEnergieCzynnaG12Dzien);
    }

    public boolean containsZaEnergieCzynnaDzien() {
        return containsPref(CENA_ZA_ENERGIE_CZYNNA_G_12_DZIEN);
    }

    public void removeZaEnergieCzynnaDzien() {
        removePref(CENA_ZA_ENERGIE_CZYNNA_G_12_DZIEN);
    }

    public String getZaEnergieCzynnaNoc() {
        return getPref(CENA_ZA_ENERGIE_CZYNNA_G_12_NOC, this.cena_za_energie_czynna_G12noc);
    }

    public void setZaEnergieCzynnaNoc(String cenaZaEnergieCzynnaG12Noc) {
        setPref(CENA_ZA_ENERGIE_CZYNNA_G_12_NOC, cenaZaEnergieCzynnaG12Noc);
    }

    public boolean containsZaEnergieCzynnaNoc() {
        return containsPref(CENA_ZA_ENERGIE_CZYNNA_G_12_NOC);
    }

    public void removeZaEnergieCzynnaNoc() {
        removePref(CENA_ZA_ENERGIE_CZYNNA_G_12_NOC);
    }

    public String getOplataSieciowa() {
        return getPref(CENA_OPLATA_SIECIOWA, this.cena_oplata_sieciowa);
    }

    public void setOplataSieciowa(String cenaOplataSieciowa) {
        setPref(CENA_OPLATA_SIECIOWA, cenaOplataSieciowa);
    }

    public boolean containsOplataSieciowa() {
        return containsPref(CENA_OPLATA_SIECIOWA);
    }

    public void removeOplataSieciowa() {
        removePref(CENA_OPLATA_SIECIOWA);
    }

    public String getOplataSieciowaDzien() {
        return getPref(CENA_OPLATA_SIECIOWA_G_12_DZIEN, this.cena_oplata_sieciowa_G12dzien);
    }

    public void setOplataSieciowaDzien(String cenaOplataSieciowaG12Dzien) {
        setPref(CENA_OPLATA_SIECIOWA_G_12_DZIEN, cenaOplataSieciowaG12Dzien);
    }

    public boolean containsOplataSieciowaDzien() {
        return containsPref(CENA_OPLATA_SIECIOWA_G_12_DZIEN);
    }

    public void removeOplataSieciowaDzien() {
        removePref(CENA_OPLATA_SIECIOWA_G_12_DZIEN);
    }

    public String getOplataSieciowaNoc() {
        return getPref(CENA_OPLATA_SIECIOWA_G_12_NOC, this.cena_oplata_sieciowa_G12noc);
    }

    public void setOplataSieciowaNoc(String cenaOplataSieciowaG12Noc) {
        setPref(CENA_OPLATA_SIECIOWA_G_12_NOC, cenaOplataSieciowaG12Noc);
    }

    public boolean containsOplataSieciowaNoc() {
        return containsPref(CENA_OPLATA_SIECIOWA_G_12_NOC);
    }

    public void removeOplataSieciowaNoc() {
        removePref(CENA_OPLATA_SIECIOWA_G_12_NOC);
    }

    public String getSkladnikJakosciowy() {
        return getPref(CENA_SKLADNIK_JAKOSCIOWY, this.cena_skladnik_jakosciowy);
    }

    public void setSkladnikJakosciowy(String cenaSkladnikJakosciowy) {
        setPref(CENA_SKLADNIK_JAKOSCIOWY, cenaSkladnikJakosciowy);
    }

    public boolean containsSkladnikJakosciowy() {
        return containsPref(CENA_SKLADNIK_JAKOSCIOWY);
    }

    public void removeSkladnikJakosciowy() {
        removePref(CENA_SKLADNIK_JAKOSCIOWY);
    }

    public String getOplataPrzejsciowa() {
        return getPref(CENA_OPLATA_PRZEJSCIOWA, this.cena_oplata_przejsciowa);
    }

    public void setOplataPrzejsciowa(String cenaOplataPrzejsciowa) {
        setPref(CENA_OPLATA_PRZEJSCIOWA, cenaOplataPrzejsciowa);
    }

    public boolean containsOplataPrzejsciowa() {
        return containsPref(CENA_OPLATA_PRZEJSCIOWA);
    }

    public void removeOplataPrzejsciowa() {
        removePref(CENA_OPLATA_PRZEJSCIOWA);
    }

    public String getOplataStalaZaPrzesyl() {
        return getPref(CENA_OPLATA_STALA_ZA_PRZESYL, this.cena_oplata_stala_za_przesyl);
    }

    public void setOplataStalaZaPrzesyl(String cenaOplataStalaZaPrzesyl) {
        setPref(CENA_OPLATA_STALA_ZA_PRZESYL, cenaOplataStalaZaPrzesyl);
    }

    public boolean containsOplataStalaZaPrzesyl() {
        return containsPref(CENA_OPLATA_STALA_ZA_PRZESYL);
    }

    public void removeOplataStalaZaPrzesyl() {
        removePref(CENA_OPLATA_STALA_ZA_PRZESYL);
    }

    public String getOplataAbonamentowa() {
        return getPref(CENA_OPLATA_ABONAMENTOWA, this.cena_oplata_abonamentowa);
    }

    public void setOplataAbonamentowa(String cenaOplataAbonamentowa) {
        setPref(CENA_OPLATA_ABONAMENTOWA, cenaOplataAbonamentowa);
    }

    public boolean containsOplataAbonamentowa() {
        return containsPref(CENA_OPLATA_ABONAMENTOWA);
    }

    public void removeOplataAbonamentowa() {
        removePref(CENA_OPLATA_ABONAMENTOWA);
    }

    public String getOplataOze() {
        return getPref(CENA_OPLATA_OZE, this.cena_oplata_oze);
    }

    public void setOplataOze(String cenaOplataOze) {
        setPref(CENA_OPLATA_OZE, cenaOplataOze);
    }

    public boolean containsOplataOze() {
        return containsPref(CENA_OPLATA_OZE);
    }

    public void removeOplataOze() {
        removePref(CENA_OPLATA_OZE);
    }
}
