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

import java.text.DecimalFormat;

public class EnergyBillActivity extends Activity {

    private static final double AKCYZA = 0.02;
    private double sumNaleznoscNetto;
    private double naleznoscBrutto;
    private double cenaZaEnergieCzynna;
    private double cenaSkladnikJakosciowy;
    private double cenaOplataSieciowa;
    private double cenaOplataPrzejsciowa;
    private double cenaOplStalaZaPrzesyl;
    private double cenaOplataAbonamentowa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_bill);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
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

    private void setZaOkres(String from, String to) {
        setTV(R.id.textView_za_okres, getString(R.string.za_okres, from, to));
    }

    private void setRozliczenieTable(int wskazanieBiezace, int wskazaniePoprzednie) {
        TableLayout rozliczenieTable = (TableLayout) findViewById(R.id.table_rozliczenie);

        setPrices();

        setRow(rozliczenieTable, R.id.row_za_energie_czynna, R.string.strefa_calodobowa, R.string.za_energie_czynna, R.string.kWh, wskazanieBiezace, wskazaniePoprzednie, cenaZaEnergieCzynna);
        setRow(rozliczenieTable, R.id.row_skladnik_jakosciowy, R.string.strefa_calodobowa, R.string.skladnik_jakosciowy, R.string.kWh, wskazanieBiezace, wskazaniePoprzednie, cenaSkladnikJakosciowy);
        setRow(rozliczenieTable, R.id.row_oplata_sieciowa, R.string.strefa_calodobowa, R.string.oplata_sieciowa, R.string.kWh, wskazanieBiezace, wskazaniePoprzednie, cenaOplataSieciowa);
        setRow(rozliczenieTable, R.id.row_oplata_przejsciowa, R.string.strefa_pusta, R.string.oplata_przejsciowa, R.string.m_c, 0, 0, cenaOplataPrzejsciowa);
        setRow(rozliczenieTable, R.id.row_oplata_stala_za_przesyl, R.string.strefa_pusta, R.string.oplata_stala_za_przesyl, R.string.m_c, 0, 0, cenaOplStalaZaPrzesyl);
        setRow(rozliczenieTable, R.id.row_oplata_abonamentowa, R.string.strefa_pusta, R.string.oplata_abonamentowa, R.string.m_c, 0, 0, cenaOplataAbonamentowa);
        setPodsumowanieRozliczenia(rozliczenieTable, R.id.row_sum);
    }

    private void setPrices() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String cenaZaEnergieCzynnaString = sharedPreferences.getString(
                getString(R.string.preferences_za_energie_czynna), getString(R.string.price_za_energie_czynna));
        cenaZaEnergieCzynna = Double.parseDouble(cenaZaEnergieCzynnaString);

        String cenaSkladnikJakosciowyString = sharedPreferences.getString(
                getString(R.string.preferences_skladnik_jakosciowy), getString(R.string.price_skladnik_jakosciowy));
        cenaSkladnikJakosciowy = Double.parseDouble(cenaSkladnikJakosciowyString);

        String cenaOplataSieciowaString = sharedPreferences.getString(
                getString(R.string.preferences_oplata_sieciowa), getString(R.string.price_oplata_sieciowa));
        cenaOplataSieciowa = Double.parseDouble(cenaOplataSieciowaString);

        String cenaOplataPrzejsciowaString = sharedPreferences.getString(
                getString(R.string.preferences_oplata_przejsciowa), getString(R.string.price_oplata_przejsciowa));
        cenaOplataPrzejsciowa = Double.parseDouble(cenaOplataPrzejsciowaString);

        String cenaOplStalaZaPrzesylString = sharedPreferences.getString(
                getString(R.string.preferences_oplata_stala_za_przesyl), getString(R.string.price_oplata_stala_za_przesyl));
        cenaOplStalaZaPrzesyl = Double.parseDouble(cenaOplStalaZaPrzesylString);

        String cenaOplataAbonamentowaString = sharedPreferences.getString(
                getString(R.string.preferences_oplata_abonamentowa), getString(R.string.price_oplata_abonamentowa));
        cenaOplataAbonamentowa = Double.parseDouble(cenaOplataAbonamentowaString);
    }

    private void setRow(View componentsTable, int rowId, int strefaId, int opisId, int jmId, int biezace, int poprzednie, double cena) {
        View row = componentsTable.findViewById(rowId);
        setTVInRow(row, R.id.textView_strefa, strefaId);
        setTVInRow(row, R.id.textView_opis, opisId);
        setTVInRow(row, R.id.textView_Jm, jmId);
        setTVInRow(row, R.id.textView_wskazanie_biezace, getWskazanie(biezace));
        setTVInRow(row, R.id.textView_wskazanie_przeprzednie, getWskazanie(poprzednie));
        setTVInRow(row, R.id.textView_ilosc_mc, getIloscMc(biezace, poprzednie));
        setTVInRow(row, R.id.textView_ilosc, getIlosc(biezace, poprzednie));
        setTVInRow(row, R.id.textView_cena, getCena(cena));
        setTVInRow(row, R.id.textView_naleznosc, getNaleznosc(biezace, poprzednie, cena));
    }

    private String getCena(double cena) {
        return new DecimalFormat("0.0000").format(cena);
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

    private String getNaleznosc(int biezace, int poprzednie, double cena) {
        int ilosc = countIlosc(biezace, poprzednie);
        double naleznosc;
        if (ilosc > 0)
            naleznosc = ilosc * cena;
        else
            naleznosc = cena;
        sumNaleznoscNetto += naleznosc;
        return display00(naleznosc);
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

    private void setPodsumowanieRozliczenia(View table, int rowId) {
        View summary = table.findViewById(rowId);
        setTVInRow(summary, R.id.textView_naleznosc_ogolem, display00(sumNaleznoscNetto));
    }

    private void setAkcyza() {
        double akcyza = sumNaleznoscNetto * AKCYZA;
        setTV(R.id.textView_akcyza, getString(R.string.akcyza, display00(sumNaleznoscNetto), display00(akcyza)));
    }

    private void setPodsumowanieTable() {
        TableLayout podsumowanie = (TableLayout) findViewById(R.id.table_podsumowanie);
        setTVInRow(podsumowanie, R.id.textView_naleznosc_netto, display00(sumNaleznoscNetto));
        double kwotaVat = sumNaleznoscNetto * 0.23;
        setTVInRow(podsumowanie, R.id.textView_kwota_vat, display00(kwotaVat));
        naleznoscBrutto = sumNaleznoscNetto + kwotaVat;
        setTVInRow(podsumowanie, R.id.textView_naleznosc_brutto, display00(naleznoscBrutto));
    }

    private String display00(double value) {
        return new DecimalFormat("0.00").format(value);
    }

    private void setDoZaplaty() {
        setTV(R.id.textView_do_zaplaty, getString(R.string.do_zaplaty, display00(naleznoscBrutto)));
    }
}
