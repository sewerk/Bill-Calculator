package pl.srw.billcalculator.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import junit.framework.Assert;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public class PreferenceMigration {
    
    public static final String VERSION_PREF_KEY = "pref_version";
    public static final int CURRENT_VERSION = 2;
    
    public static void migrate(Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final int oldVersion = preferences.getInt(VERSION_PREF_KEY, 1);
        
        switch (oldVersion) {
            case 1: migrate_1_2(preferences, context);
            case CURRENT_VERSION:
                Assert.assertEquals(CURRENT_VERSION, preferences.getInt(VERSION_PREF_KEY, 1));
        }
    }

    private static void migrate_1_2(SharedPreferences preferences, Context context) {
        migratePgePrices(preferences, context);
        migratePgnigPrices(preferences, context);
        preferences.edit().putInt(VERSION_PREF_KEY, 2).commit();
    }

    private static void migratePgePrices(SharedPreferences preferences, Context context) {
        final PgePrices pgePrices = new PgePrices();

        String key = context.getString(R.string.preferences_pge_oplata_abonamentowa);
        if (preferences.contains(key)) {
            pgePrices.setOplataAbonamentowa(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pge_oplata_przejsciowa);
        if (preferences.contains(key)) {
            pgePrices.setOplataPrzejsciowa(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pge_oplata_sieciowa);
        if (preferences.contains(key)) {
            pgePrices.setOplataSieciowa(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pge_za_energie_czynna);
        if (preferences.contains(key)) {
            pgePrices.setZaEnergieCzynna(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pge_oplata_stala_za_przesyl);
        if (preferences.contains(key)) {
            pgePrices.setOplataStalaZaPrzesyl(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pge_skladnik_jakosciowy);
        if (preferences.contains(key)) {
            pgePrices.setSkladnikJakosciowy(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pge_za_energie_czynna_G12dzien);
        if (preferences.contains(key)) {
            pgePrices.setZaEnergieCzynnaDzien(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pge_za_energie_czynna_G12noc);
        if (preferences.contains(key)) {
            pgePrices.setZaEnergieCzynnaNoc(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pge_oplata_sieciowa_G12dzien);
        if (preferences.contains(key)) {
            pgePrices.setOplataSieciowaDzien(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pge_oplata_sieciowa_G12noc);
        if (preferences.contains(key)) {
            pgePrices.setOplataSieciowaNoc(preferences.getString(key, ""));
        }
    }

    private static void migratePgnigPrices(SharedPreferences preferences, Context context) {
        final PgnigPrices pgnigPrices = new PgnigPrices();

        String key = context.getString(R.string.preferences_pgnig_dystrybucyjna_stala);
        if (preferences.contains(key)) {
            pgnigPrices.setDystrybucyjnaStala(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pgnig_dystrybucyjna_zmienna);
        if (preferences.contains(key)) {
            pgnigPrices.setDystrybucyjnaZmienna(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pgnig_abonamentowa);
        if (preferences.contains(key)) {
            pgnigPrices.setOplataAbonamentowa(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pgnig_paliwo_gazowe);
        if (preferences.contains(key)) {
            pgnigPrices.setPaliwoGazowe(preferences.getString(key, ""));
        }
        key = context.getString(R.string.preferences_pgnig_wsp_konwersji);
        if (preferences.contains(key)) {
            pgnigPrices.setWspolczynnikKonwersji(preferences.getString(key, ""));
        }
    }

}
