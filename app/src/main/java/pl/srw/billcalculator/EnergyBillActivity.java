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

import pl.srw.billcalculator.util.Dates;
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

    private String dateFrom;
    private String dateTo;
    private int readingFrom;
    private int readingTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_bill);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        dateFrom = intent.getStringExtra(MainActivity.DATE_FROM);
        dateTo = intent.getStringExtra(MainActivity.DATE_TO);
        readingFrom = intent.getIntExtra(MainActivity.READING_FROM, 0);
        readingTo = intent.getIntExtra(MainActivity.READING_TO, 0);

        setPrices();

        setZaOkres();
        setRozliczenieTable();
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

    private void setZaOkres() {
        setTV(R.id.textView_za_okres, getString(R.string.za_okres, dateFrom, dateTo));
    }

    private void setRozliczenieTable() {
        TableLayout rozliczenieTable = (TableLayout) findViewById(R.id.table_rozliczenie);
        int zuzycie = countZuzycie();
        int iloscMc = Dates.countMonth(dateFrom, dateTo);

        setRow(rozliczenieTable, R.id.row_za_energie_czynna, R.string.strefa_calodobowa, R.string.za_energie_czynna, zuzycie, R.string.kWh, cenaZaEnergieCzynna);
        setRow(rozliczenieTable, R.id.row_skladnik_jakosciowy, R.string.strefa_calodobowa, R.string.skladnik_jakosciowy, zuzycie, R.string.kWh, cenaSkladnikJakosciowy);
        setRow(rozliczenieTable, R.id.row_oplata_sieciowa, R.string.strefa_calodobowa, R.string.oplata_sieciowa, zuzycie, R.string.kWh, cenaOplataSieciowa);
        setRow(rozliczenieTable, R.id.row_oplata_przejsciowa, R.string.strefa_pusta, R.string.oplata_przejsciowa, iloscMc, R.string.m_c, cenaOplataPrzejsciowa);
        setRow(rozliczenieTable, R.id.row_oplata_stala_za_przesyl, R.string.strefa_pusta, R.string.oplata_stala_za_przesyl, iloscMc, R.string.m_c, cenaOplStalaZaPrzesyl);
        setRow(rozliczenieTable, R.id.row_oplata_abonamentowa, R.string.strefa_pusta, R.string.oplata_abonamentowa, iloscMc, R.string.m_c, cenaOplataAbonamentowa);
        setPodsumowanieRozliczenia(rozliczenieTable);
    }

    private void setRow(View componentsTable, int rowId, int strefaId, int opisId, int ilosc, int jmId, BigDecimal cena) {
        View row = componentsTable.findViewById(rowId);
        setTVInRow(row, R.id.textView_strefa, strefaId);
        setTVInRow(row, R.id.textView_opis, opisId);
        setTVInRow(row, R.id.textView_Jm, jmId);
        if (jmId == R.string.kWh) {
            setTVInRow(row, R.id.textView_wskazanie_biezace, Integer.toString(readingTo));
            setTVInRow(row, R.id.textView_wskazanie_przeprzednie, Integer.toString(readingFrom));
            setTVInRow(row, R.id.textView_ilosc, Integer.toString(ilosc));
        } else {
            setTVInRow(row, R.id.textView_ilosc_mc, Display.withScale(new BigDecimal(ilosc), 2));
        }
        setTVInRow(row, R.id.textView_naleznosc, getNaleznosc(cena, ilosc));
        setTVInRow(row, R.id.textView_cena, Display.withScale(cena, PRICE_SCALE));
    }

    private String getNaleznosc(BigDecimal cena, int ilosc) {
        BigDecimal naleznosc = cena.multiply(new BigDecimal(ilosc));
        sumNaleznoscNetto = sumNaleznoscNetto.add(naleznosc);
        return Display.toPay(naleznosc);
    }

    private int countZuzycie() {
        return readingTo - readingFrom;
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
        BigDecimal akcyza = AKCYZA.multiply(new BigDecimal(countZuzycie()));
        setTV(R.id.textView_akcyza, getString(R.string.akcyza, countZuzycie(), Display.toPay(akcyza)));
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
