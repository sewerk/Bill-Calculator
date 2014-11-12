package pl.srw.billcalculator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import pl.srw.billcalculator.util.Display;

/**
 * Created by Kamil Seweryn
 */
public class EnergyBillActivity extends Activity {

    public static final int PRICE_SCALE = 4;
    private static final BigDecimal AKCYZA = new BigDecimal("0.02");
    private static final BigDecimal VAT = new BigDecimal("0.23");
    private BigDecimal sumNaleznoscNetto = BigDecimal.ZERO;
    private BigDecimal naleznoscBrutto;

    private BigDecimal cenaZaEnergieCzynna;
    private BigDecimal cenaSkladnikJakosciowy;
    private BigDecimal cenaOplataSieciowa;
    private BigDecimal cenaOplataPrzejsciowa;
    private BigDecimal cenaOplStalaZaPrzesyl;
    private BigDecimal cenaOplataAbonamentowa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_bill);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        setPrices();

        setZaOkres(intent.getStringExtra(MainActivity.DATE_FROM), intent.getStringExtra(MainActivity.DATE_TO));
        setRozliczenieTable(intent.getIntExtra(MainActivity.READING_TO, 0), intent.getIntExtra(MainActivity.READING_FROM, 0));
        setAkcyza();
        setPodsumowanieTable();
        setDoZaplaty();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPrices() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String cenaZaEnergieCzynnaString = sharedPreferences.getString(
                getString(R.string.preferences_za_energie_czynna), getString(R.string.price_za_energie_czynna));
        cenaZaEnergieCzynna = new BigDecimal(cenaZaEnergieCzynnaString);

        String cenaSkladnikJakosciowyString = sharedPreferences.getString(
                getString(R.string.preferences_skladnik_jakosciowy), getString(R.string.price_skladnik_jakosciowy));
        cenaSkladnikJakosciowy = new BigDecimal(cenaSkladnikJakosciowyString);

        String cenaOplataSieciowaString = sharedPreferences.getString(
                getString(R.string.preferences_oplata_sieciowa), getString(R.string.price_oplata_sieciowa));
        cenaOplataSieciowa = new BigDecimal(cenaOplataSieciowaString);

        String cenaOplataPrzejsciowaString = sharedPreferences.getString(
                getString(R.string.preferences_oplata_przejsciowa), getString(R.string.price_oplata_przejsciowa));
        cenaOplataPrzejsciowa = new BigDecimal(cenaOplataPrzejsciowaString);

        String cenaOplStalaZaPrzesylString = sharedPreferences.getString(
                getString(R.string.preferences_oplata_stala_za_przesyl), getString(R.string.price_oplata_stala_za_przesyl));
        cenaOplStalaZaPrzesyl = new BigDecimal(cenaOplStalaZaPrzesylString);

        String cenaOplataAbonamentowaString = sharedPreferences.getString(
                getString(R.string.preferences_oplata_abonamentowa), getString(R.string.price_oplata_abonamentowa));
        cenaOplataAbonamentowa = new BigDecimal(cenaOplataAbonamentowaString);
    }

    private void setZaOkres(String from, String to) {
        setTV(R.id.textView_za_okres, getString(R.string.za_okres, from, to));
    }

    private void setRozliczenieTable(int wskazanieBiezace, int wskazaniePoprzednie) {
        TableLayout rozliczenieTable = (TableLayout) findViewById(R.id.table_rozliczenie);

        setRow(rozliczenieTable, R.id.row_za_energie_czynna, R.string.strefa_calodobowa, R.string.za_energie_czynna, R.string.kWh, wskazanieBiezace, wskazaniePoprzednie, cenaZaEnergieCzynna);
        setRow(rozliczenieTable, R.id.row_skladnik_jakosciowy, R.string.strefa_calodobowa, R.string.skladnik_jakosciowy, R.string.kWh, wskazanieBiezace, wskazaniePoprzednie, cenaSkladnikJakosciowy);
        setRow(rozliczenieTable, R.id.row_oplata_sieciowa, R.string.strefa_calodobowa, R.string.oplata_sieciowa, R.string.kWh, wskazanieBiezace, wskazaniePoprzednie, cenaOplataSieciowa);
        setRow(rozliczenieTable, R.id.row_oplata_przejsciowa, R.string.strefa_pusta, R.string.oplata_przejsciowa, R.string.m_c, 0, 0, cenaOplataPrzejsciowa);
        setRow(rozliczenieTable, R.id.row_oplata_stala_za_przesyl, R.string.strefa_pusta, R.string.oplata_stala_za_przesyl, R.string.m_c, 0, 0, cenaOplStalaZaPrzesyl);
        setRow(rozliczenieTable, R.id.row_oplata_abonamentowa, R.string.strefa_pusta, R.string.oplata_abonamentowa, R.string.m_c, 0, 0, cenaOplataAbonamentowa);
        setPodsumowanieRozliczenia(rozliczenieTable);
    }

    private void setRow(View componentsTable, int rowId, int strefaId, int opisId, int jmId, int biezace, int poprzednie, BigDecimal cena) {
        View row = componentsTable.findViewById(rowId);
        setTVInRow(row, R.id.textView_strefa, strefaId);
        setTVInRow(row, R.id.textView_opis, opisId);
        setTVInRow(row, R.id.textView_Jm, jmId);
        setTVInRow(row, R.id.textView_wskazanie_biezace, getWskazanie(biezace));
        setTVInRow(row, R.id.textView_wskazanie_przeprzednie, getWskazanie(poprzednie));
        setTVInRow(row, R.id.textView_ilosc_mc, getIloscMc(biezace, poprzednie));
        setTVInRow(row, R.id.textView_ilosc, getIlosc(biezace, poprzednie));
        setTVInRow(row, R.id.textView_cena, Display.price(cena, PRICE_SCALE));
        setTVInRow(row, R.id.textView_naleznosc, getNaleznosc(biezace, poprzednie, cena));
    }

    private String getWskazanie(int wskazanieVal) {
        if (wskazanieVal > 0)
            return Integer.toString(wskazanieVal);
        else
            return "";
    }

    private String getIloscMc(int biezace, int poprzednie) {
        if (countIlosc(biezace, poprzednie) > 0)
            return "0,00";
        else
            return "1,00";
    }

    private String getNaleznosc(int biezace, int poprzednie, BigDecimal cena) {
        int ilosc = countIlosc(biezace, poprzednie);
        BigDecimal naleznosc;
        if (ilosc > 0)
            naleznosc = cena.multiply(new BigDecimal(ilosc));
        else
            naleznosc = cena;
        sumNaleznoscNetto = sumNaleznoscNetto.add(naleznosc);
        return Display.toPay(naleznosc);
    }

    private String getIlosc(int biezace, int poprzednie) {
        int ilosc = countIlosc(biezace, poprzednie);
        if (ilosc > 0)
            return Integer.toString(ilosc);
        else
            return "";
    }

    private int countIlosc(int biezace, int poprzednie) {
        return biezace - poprzednie;
    }

    private void setTVInRow(View row, int tvId, int stringId) {
        TextView tv = (TextView) row.findViewById(tvId);
        setTV(tv, getString(stringId));
    }

    private void setTVInRow(View row, int tvId, String string) {
        TextView tv = (TextView) row.findViewById(tvId);
        setTV(tv, string);
    }

    private void setTV(TextView tv, String string) {
        tv.setText(string);
    }

    private void setTV(int tvId, String string) {
        TextView tv = (TextView) findViewById(tvId);
        tv.setText(string);
    }

    private void setPodsumowanieRozliczenia(View table) {
        View summary = table.findViewById(R.id.row_sum);
        setTVInRow(summary, R.id.textView_naleznosc_ogolem, Display.toPay(sumNaleznoscNetto));
    }

    private void setAkcyza() {
        BigDecimal akcyza = sumNaleznoscNetto.multiply(AKCYZA);
        setTV(R.id.textView_akcyza, getString(R.string.akcyza, Display.toPay(sumNaleznoscNetto), Display.toPay(akcyza)));
    }

    private void setPodsumowanieTable() {
        TableLayout podsumowanie = (TableLayout) findViewById(R.id.table_podsumowanie);
        setTVInRow(podsumowanie, R.id.textView_naleznosc_netto, Display.toPay(sumNaleznoscNetto));
        BigDecimal kwotaVat = sumNaleznoscNetto.multiply(VAT);
        setTVInRow(podsumowanie, R.id.textView_kwota_vat, Display.toPay(kwotaVat));
        naleznoscBrutto = sumNaleznoscNetto.add(kwotaVat);
        setTVInRow(podsumowanie, R.id.textView_naleznosc_brutto, Display.toPay(naleznoscBrutto));
    }

    private void setDoZaplaty() {
        setTV(R.id.textView_do_zaplaty, getString(R.string.do_zaplaty, Display.toPay(naleznoscBrutto)));
    }
}
