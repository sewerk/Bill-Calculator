package pl.srw.billcalculator.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.math.BigDecimal;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public enum PgePrices {

    INSTANCE;

    private BigDecimal cenaZaEnergieCzynna;
    private BigDecimal cenaSkladnikJakosciowy;
    private BigDecimal cenaOplataSieciowa;
    private BigDecimal cenaOplataPrzejsciowa;
    private BigDecimal cenaOplStalaZaPrzesyl;
    private BigDecimal cenaOplataAbonamentowa;

    private BigDecimal cenaZaEnergieCzynnaDzien;
    private BigDecimal cenaZaEnergieCzynnaNoc;
    private BigDecimal cenaOplataSieciowaDzien;
    private BigDecimal cenaOplataSieciowaNoc;

    public void read(final Context context) { //TODO rethink
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        cenaZaEnergieCzynnaDzien = getPriceFrom(context, sharedPreferences,
                R.string.preferences_pge_za_energie_czynna_G12dzien, R.string.price_za_energie_czynna_G12dzien);
        cenaZaEnergieCzynnaNoc = getPriceFrom(context, sharedPreferences,
                R.string.preferences_pge_za_energie_czynna_G12noc, R.string.price_za_energie_czynna_G12noc);
        cenaOplataSieciowaDzien = getPriceFrom(context, sharedPreferences,
                R.string.preferences_pge_oplata_sieciowa_G12dzien, R.string.price_oplata_sieciowa_G12dzien);
        cenaOplataSieciowaNoc = getPriceFrom(context, sharedPreferences,
                R.string.preferences_pge_oplata_sieciowa_G12noc, R.string.price_oplata_sieciowa_G12noc);
        cenaZaEnergieCzynna = getPriceFrom(context, sharedPreferences,
                R.string.preferences_pge_za_energie_czynna, R.string.price_za_energie_czynna);
        cenaOplataSieciowa = getPriceFrom(context, sharedPreferences,
                R.string.preferences_pge_oplata_sieciowa, R.string.price_oplata_sieciowa);

        cenaSkladnikJakosciowy = getPriceFrom(context, sharedPreferences,
                R.string.preferences_pge_skladnik_jakosciowy, R.string.price_skladnik_jakosciowy);

        cenaOplataPrzejsciowa = getPriceFrom(context, sharedPreferences,
                R.string.preferences_pge_oplata_przejsciowa, R.string.price_oplata_przejsciowa);
        cenaOplStalaZaPrzesyl = getPriceFrom(context, sharedPreferences,
                R.string.preferences_pge_oplata_stala_za_przesyl, R.string.price_oplata_stala_za_przesyl);
        cenaOplataAbonamentowa = getPriceFrom(context, sharedPreferences,
                R.string.preferences_pge_oplata_abonamentowa, R.string.price_oplata_abonamentowa);
    }

    private BigDecimal getPriceFrom(final Context context, SharedPreferences sharedPreferences, int preferenceKey, int defaultValueKey) {
        String cenaZaEnergieCzynnaString = sharedPreferences.getString(context.getString(preferenceKey), context.getString(defaultValueKey));
        return new BigDecimal(cenaZaEnergieCzynnaString);
    }

    public BigDecimal getCenaZaEnergieCzynna() {
        return cenaZaEnergieCzynna;
    }

    public BigDecimal getCenaSkladnikJakosciowy() {
        return cenaSkladnikJakosciowy;
    }

    public BigDecimal getCenaOplataSieciowa() {
        return cenaOplataSieciowa;
    }

    public BigDecimal getCenaOplataPrzejsciowa() {
        return cenaOplataPrzejsciowa;
    }

    public BigDecimal getCenaOplStalaZaPrzesyl() {
        return cenaOplStalaZaPrzesyl;
    }

    public BigDecimal getCenaOplataAbonamentowa() {
        return cenaOplataAbonamentowa;
    }

    public BigDecimal getCenaZaEnergieCzynnaDzien() {
        return cenaZaEnergieCzynnaDzien;
    }

    public BigDecimal getCenaZaEnergieCzynnaNoc() {
        return cenaZaEnergieCzynnaNoc;
    }

    public BigDecimal getCenaOplataSieciowaDzien() {
        return cenaOplataSieciowaDzien;
    }

    public BigDecimal getCenaOplataSieciowaNoc() {
        return cenaOplataSieciowaNoc;
    }
}
