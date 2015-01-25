package pl.srw.billcalculator.test;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ApplicationTestCase;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.pojo.PgePrices;
import pl.srw.billcalculator.pojo.PgnigPrices;

/**
 * Created by Kamil Seweryn.
 */
public class BillCalculatorTest extends ApplicationTestCase {

    private SharedPreferences preferences;

    public BillCalculatorTest() {
        super(BillCalculator.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        preferences.edit().clear().commit();
    }

    public void testPgePricesPreferenceMigration() {
        // prepare
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(mContext.getString(R.string.preferences_pge_za_energie_czynna_G12dzien), "1.01");
        editor.putString(mContext.getString(R.string.preferences_pge_za_energie_czynna_G12noc), "2.02");
        editor.putString(mContext.getString(R.string.preferences_pge_skladnik_jakosciowy), "3.03");
        editor.putString(mContext.getString(R.string.preferences_pge_oplata_sieciowa_G12dzien), "4.04");
        editor.putString(mContext.getString(R.string.preferences_pge_oplata_sieciowa_G12noc), "5.05");
        editor.putString(mContext.getString(R.string.preferences_pge_oplata_przejsciowa), "6.06");
        editor.putString(mContext.getString(R.string.preferences_pge_oplata_stala_za_przesyl), "7.07");
        editor.putString(mContext.getString(R.string.preferences_pge_oplata_abonamentowa), "8.08");
        editor.putString(mContext.getString(R.string.preferences_pge_za_energie_czynna), "9.09");
        editor.putString(mContext.getString(R.string.preferences_pge_oplata_sieciowa), "9.01");
        editor.commit();

        createApplication();

        // test
        final PgePrices pgePrices = new PgePrices(preferences);
        assertEquals("1.01", pgePrices.getZaEnergieCzynnaDzien());
        assertEquals("2.02", pgePrices.getZaEnergieCzynnaNoc());
        assertEquals("3.03", pgePrices.getSkladnikJakosciowy());
        assertEquals("4.04", pgePrices.getOplataSieciowaDzien());
        assertEquals("5.05", pgePrices.getOplataSieciowaNoc());
        assertEquals("6.06", pgePrices.getOplataPrzejsciowa());
        assertEquals("7.07", pgePrices.getOplataStalaZaPrzesyl());
        assertEquals("8.08", pgePrices.getOplataAbonamentowa());
        assertEquals("9.09", pgePrices.getZaEnergieCzynna());
        assertEquals("9.01", pgePrices.getOplataSieciowa());
    }

    public void testPgnigPricesPreferencesMigration() {
        // prepare
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(mContext.getString(R.string.preferences_pgnig_dystrybucyjna_stala), "1.09");
        editor.putString(mContext.getString(R.string.preferences_pgnig_dystrybucyjna_zmienna), "1.08");
        editor.putString(mContext.getString(R.string.preferences_pgnig_abonamentowa), "1.07");
        editor.putString(mContext.getString(R.string.preferences_pgnig_paliwo_gazowe), "1.06");
        editor.putString(mContext.getString(R.string.preferences_pgnig_wsp_konwersji), "1.05");
        editor.commit();
        
        createApplication();

        // test
        final PgnigPrices pgnigPrices = new PgnigPrices(preferences);
        assertEquals("1.09", pgnigPrices.getDystrybucyjnaStala());
        assertEquals("1.08", pgnigPrices.getDystrybucyjnaZmienna());
        assertEquals("1.07", pgnigPrices.getOplataAbonamentowa());
        assertEquals("1.06", pgnigPrices.getPaliwoGazowe());
        assertEquals("1.05", pgnigPrices.getWspolczynnikKonwersji());
    }
}
