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
import java.math.RoundingMode;

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
    private BigDecimal cenaZaEnergieCzynnaDzien;
    private BigDecimal cenaZaEnergieCzynnaNoc;
    private BigDecimal cenaOplataSieciowaDzien;
    private BigDecimal cenaOplataSieciowaNoc;

    private String dateFrom;
    private String dateTo;
    private int readingFrom;
    private int readingTo;
    private int readingDayFrom;
    private int readingDayTo;
    private int readingNightFrom;
    private int readingNightTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_bill);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        readExtra(getIntent());
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

    private void readExtra(Intent intent) {
        dateFrom = intent.getStringExtra(MainActivity.DATE_FROM);
        dateTo = intent.getStringExtra(MainActivity.DATE_TO);

        readingFrom = intent.getIntExtra(MainActivity.READING_FROM, -1);
        readingTo = intent.getIntExtra(MainActivity.READING_TO, -1);

        readingDayFrom = intent.getIntExtra(MainActivity.READING_DAY_FROM, -1);
        readingDayTo = intent.getIntExtra(MainActivity.READING_DAY_TO, -1);
        readingNightFrom = intent.getIntExtra(MainActivity.READING_NIGHT_FROM, -1);
        readingNightTo = intent.getIntExtra(MainActivity.READING_NIGHT_TO, -1);
    }

    private void setPrices() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (isTaryfaDwustrefowa()) {
            cenaZaEnergieCzynnaDzien = getPriceFrom(sharedPreferences,
                    R.string.preferences_pge_za_energie_czynna_G12dzien, R.string.price_za_energie_czynna_G12dzien);
            cenaZaEnergieCzynnaNoc = getPriceFrom(sharedPreferences,
                    R.string.preferences_pge_za_energie_czynna_G12noc, R.string.price_za_energie_czynna_G12noc);
            cenaOplataSieciowaDzien = getPriceFrom(sharedPreferences,
                    R.string.preferences_pge_oplata_sieciowa_G12dzien, R.string.price_oplata_sieciowa_G12dzien);
            cenaOplataSieciowaNoc = getPriceFrom(sharedPreferences,
                    R.string.preferences_pge_oplata_sieciowa_G12noc, R.string.price_oplata_sieciowa_G12noc);
        } else {
            cenaZaEnergieCzynna = getPriceFrom(sharedPreferences,
                    R.string.preferences_pge_za_energie_czynna, R.string.price_za_energie_czynna);
            cenaOplataSieciowa = getPriceFrom(sharedPreferences,
                    R.string.preferences_pge_oplata_sieciowa, R.string.price_oplata_sieciowa);
        }
        cenaSkladnikJakosciowy = getPriceFrom(sharedPreferences,
                R.string.preferences_pge_skladnik_jakosciowy, R.string.price_skladnik_jakosciowy);

        cenaOplataPrzejsciowa = getPriceFrom(sharedPreferences,
                R.string.preferences_pge_oplata_przejsciowa, R.string.price_oplata_przejsciowa);
        cenaOplStalaZaPrzesyl = getPriceFrom(sharedPreferences,
                R.string.preferences_pge_oplata_stala_za_przesyl, R.string.price_oplata_stala_za_przesyl);
        cenaOplataAbonamentowa = getPriceFrom(sharedPreferences,
                R.string.preferences_pge_oplata_abonamentowa, R.string.price_oplata_abonamentowa);
    }

    private BigDecimal getPriceFrom(SharedPreferences sharedPreferences, int preferenceKey, int defaultValueKey) {
        String cenaZaEnergieCzynnaString = sharedPreferences.getString(getString(preferenceKey), getString(defaultValueKey));
        return new BigDecimal(cenaZaEnergieCzynnaString);
    }

    private void setZaOkres() {
        setTV(R.id.textView_za_okres, getString(R.string.za_okres, dateFrom, dateTo));
    }

    private void setRozliczenieTable() {
        TableLayout rozliczenieTable = (TableLayout) findViewById(R.id.table_rozliczenie);

        if(isTaryfaDwustrefowa()) {
            setTV(R.id.textView_taryfa, getString(R.string.pge_tariff_G12_on_bill));
            setG12Rows(rozliczenieTable, countDzienneZuzycie(), countNocneZuzycie());
        } else {
            setTV(R.id.textView_taryfa, getString(R.string.pge_tariff_G11_on_bill));
            setG11Rows(rozliczenieTable, countZuzycie());
        }

        int iloscMc = Dates.countMonth(dateFrom, dateTo);
        setRow(rozliczenieTable, R.id.row_oplata_przejsciowa, R.string.strefa_pusta, R.string.oplata_przejsciowa, iloscMc, R.string.m_c, cenaOplataPrzejsciowa);
        setRow(rozliczenieTable, R.id.row_oplata_stala_za_przesyl, R.string.strefa_pusta, R.string.oplata_stala_za_przesyl, iloscMc, R.string.m_c, cenaOplStalaZaPrzesyl);
        setRow(rozliczenieTable, R.id.row_oplata_abonamentowa, R.string.strefa_pusta, R.string.oplata_abonamentowa, iloscMc, R.string.m_c, cenaOplataAbonamentowa);

        setPodsumowanieRozliczenia(rozliczenieTable);
    }

    private boolean isTaryfaDwustrefowa() {
        return readingDayTo > 0;
    }

    private void setG11Rows(TableLayout rozliczenieTable, int zuzycie) {
        setRow(rozliczenieTable, R.id.row_za_energie_czynna, R.string.strefa_calodobowa, R.string.za_energie_czynna, zuzycie, R.string.kWh, cenaZaEnergieCzynna);
        setRow(rozliczenieTable, R.id.row_skladnik_jakosciowy, R.string.strefa_calodobowa, R.string.skladnik_jakosciowy, zuzycie, R.string.kWh, cenaSkladnikJakosciowy);
        setRow(rozliczenieTable, R.id.row_oplata_sieciowa, R.string.strefa_calodobowa, R.string.oplata_sieciowa, zuzycie, R.string.kWh, cenaOplataSieciowa);
    }

    private void setG12Rows(TableLayout rozliczenieTable, int dzienneZuzycie, int nocneZuzycie) {
        setRow(rozliczenieTable, R.id.row_za_energie_czynna, R.string.strefa_dzienna, R.string.za_energie_czynna, dzienneZuzycie, R.string.kWh, cenaZaEnergieCzynnaDzien);
        setRow(rozliczenieTable, R.id.row_skladnik_jakosciowy, R.string.strefa_dzienna, R.string.skladnik_jakosciowy, dzienneZuzycie, R.string.kWh, cenaSkladnikJakosciowy);
        setRow(rozliczenieTable, R.id.row_oplata_sieciowa, R.string.strefa_dzienna, R.string.oplata_sieciowa, dzienneZuzycie, R.string.kWh, cenaOplataSieciowaDzien);

        setRow(rozliczenieTable, R.id.row_za_energie_czynna2, R.string.strefa_nocna, R.string.za_energie_czynna, nocneZuzycie, R.string.kWh, cenaZaEnergieCzynnaNoc);
        setRow(rozliczenieTable, R.id.row_skladnik_jakosciowy2, R.string.strefa_nocna, R.string.skladnik_jakosciowy, nocneZuzycie, R.string.kWh, cenaSkladnikJakosciowy);
        setRow(rozliczenieTable, R.id.row_oplata_sieciowa2, R.string.strefa_nocna, R.string.oplata_sieciowa, nocneZuzycie, R.string.kWh, cenaOplataSieciowaNoc);
    }

    private void setRow(View componentsTable, int rowId, int strefaId, int opisId, int ilosc, int jmId, BigDecimal cena) {
        View row = componentsTable.findViewById(rowId);
        row.setVisibility(View.VISIBLE);

        setTVInRow(row, R.id.textView_strefa, strefaId);
        setTVInRow(row, R.id.textView_opis, opisId);
        setTVInRow(row, R.id.textView_Jm, jmId);
        if (jmId == R.string.kWh) {
            setReadingsInRow(row, strefaId);
            setTVInRow(row, R.id.textView_ilosc, Integer.toString(ilosc));
        } else {
            setTVInRow(row, R.id.textView_ilosc_mc, Display.withScale(new BigDecimal(ilosc), 2));
        }
        setTVInRow(row, R.id.textView_naleznosc, getNaleznosc(cena, ilosc));
        setTVInRow(row, R.id.textView_cena, Display.withScale(cena, PRICE_SCALE));
    }

    private void setReadingsInRow(View row, int strefaId) {
        if (strefaId == R.string.strefa_dzienna) {
            setTVInRow(row, R.id.textView_wskazanie_biezace, Integer.toString(readingDayTo));
            setTVInRow(row, R.id.textView_wskazanie_przeprzednie, Integer.toString(readingDayFrom));
        } else if (strefaId == R.string.strefa_nocna) {
            setTVInRow(row, R.id.textView_wskazanie_biezace, Integer.toString(readingNightTo));
            setTVInRow(row, R.id.textView_wskazanie_przeprzednie, Integer.toString(readingNightFrom));
        } else {
            setTVInRow(row, R.id.textView_wskazanie_biezace, Integer.toString(readingTo));
            setTVInRow(row, R.id.textView_wskazanie_przeprzednie, Integer.toString(readingFrom));
        }
    }

    private String getNaleznosc(BigDecimal cena, int ilosc) {
        BigDecimal naleznosc = cena.multiply(new BigDecimal(ilosc));
        sumNaleznoscNetto = sumNaleznoscNetto.add(naleznosc);
        return Display.toPay(naleznosc);
    }

    private int countZuzycie() {
        return readingTo - readingFrom;
    }

    private int countDzienneZuzycie() {
        return readingDayTo - readingDayFrom;
    }

    private int countNocneZuzycie() {
        return readingNightTo - readingNightFrom;
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
        int zuzycie = isTaryfaDwustrefowa()
                ? countDzienneZuzycie() + countNocneZuzycie()
                : countZuzycie();
        BigDecimal akcyza = AKCYZA.multiply(new BigDecimal(zuzycie));
        setTV(R.id.textView_akcyza, getString(R.string.akcyza, zuzycie, Display.toPay(akcyza)));
    }

    private void setPodsumowanieTable() {
        TableLayout podsumowanie = (TableLayout) findViewById(R.id.table_podsumowanie);
        setTVInRow(podsumowanie, R.id.textView_naleznosc_netto, Display.toPay(sumNaleznoscNetto));
        BigDecimal kwotaVat = sumNaleznoscNetto.multiply(VAT).setScale(2, RoundingMode.HALF_UP);
        setTVInRow(podsumowanie, R.id.textView_kwota_vat, Display.toPay(kwotaVat));
        naleznoscBrutto = sumNaleznoscNetto.add(kwotaVat);
        setTVInRow(podsumowanie, R.id.textView_naleznosc_brutto, Display.toPay(naleznoscBrutto));
    }

    private void setDoZaplaty() {
        setTV(R.id.textView_do_zaplaty, getString(R.string.do_zaplaty, Display.toPay(naleznoscBrutto)));
    }
}
