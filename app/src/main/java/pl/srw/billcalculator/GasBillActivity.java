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

public class GasBillActivity extends Activity {

    private String dateFrom;
    private String dateTo;
    private int readingFrom;
    private int readingTo;

    private double oplataAbonamentowa;
    private double paliwoGazowe;
    private double dystrybucyjnaStala;
    private double dystrybucyjnaZmienna;

    private double sumWartoscNetto;
    private double wartoscBrutto;
    private double kwotaVat;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gas_bill);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        dateFrom = intent.getStringExtra(MainActivity.DATE_FROM);
        dateTo = intent.getStringExtra(MainActivity.DATE_TO);
        readingFrom = intent.getIntExtra(MainActivity.READING_FROM, 0);
        readingTo = intent.getIntExtra(MainActivity.READING_TO, 0);

        setOdczytyTable();
        setRozliczenieTable();
        setOdczyt();
        setPodsumowanieTable();
        setWartoscFaktury();
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setOdczytyTable() {
        TableLayout odczyty = (TableLayout) findViewById(R.id.table_odczyt);

        setTV(odczyty, R.id.textView_odczyt_poprzedni, getString(R.string.odczyt_na_dzien, dateFrom, readingFrom));
        setTV(odczyty, R.id.textView_odczyt_biezacy, getString(R.string.odczyt_na_dzien, dateTo, readingTo));
        int zuzycie = readingTo - readingFrom;
        setTV(odczyty, R.id.textView_zuzycie, getString(R.string.zuzycie, zuzycie));
    }

    private void setRozliczenieTable() {
        TableLayout rozliczenie = (TableLayout) findViewById(R.id.table_rozliczenie);
        int zuzycie = readingTo - readingFrom;

        setPrices();

        setRow(rozliczenie, R.id.row_abonamentowa, R.string.abonamentowa, 1, getString(R.string.mc), oplataAbonamentowa, "");
        setRow(rozliczenie, R.id.row_paliwo_gazowe, R.string.paliwo_gazowe, zuzycie, getString(R.string.m3), paliwoGazowe, "ZW");
        setRow(rozliczenie, R.id.row_dystrybucyjna_stala, R.string.dystrybucyjna_stala, 1, getString(R.string.mc), dystrybucyjnaStala, "");
        setRow(rozliczenie, R.id.row_dystrybucyjna_zmienna, R.string.dystrybucyjna_zmienna, zuzycie, getString(R.string.m3), dystrybucyjnaZmienna, "");

        setPodsumowanieRozliczenia(rozliczenie);
    }

    private void setPrices() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String oplataAbonamentowaString = sharedPreferences.getString(
                getString(R.string.preferences_abonamentowa), getString(R.string.price_abonamentowa));
        oplataAbonamentowa = Double.parseDouble(oplataAbonamentowaString);

        String oplataPaliwoGazoweString = sharedPreferences.getString(
                getString(R.string.preferences_paliwo_gazowe), getString(R.string.price_paliwo_gazowe));
        paliwoGazowe = Double.parseDouble(oplataPaliwoGazoweString);

        String dystrybucyjnaStalaString = sharedPreferences.getString(
                getString(R.string.preferences_dystrybucyjna_stala), getString(R.string.price_dystrybucyjna_stala));
        dystrybucyjnaStala = Double.parseDouble(dystrybucyjnaStalaString);

        String dystrybucyjnaZmiennaString = sharedPreferences.getString(
                getString(R.string.preferences_dystrybucyjna_zmienna), getString(R.string.price_dystrybucyjna_zmienna));
        dystrybucyjnaZmienna = Double.parseDouble(dystrybucyjnaZmiennaString);
    }

    private void setRow(TableLayout rozliczenie, int rowId, int oplataTextId, int ilosc, String jm, double cenaNetto, String wartoscAkcyzy) {
        View row = rozliczenie.findViewById(rowId);
        setTV(row, R.id.textView_oplata, getString(oplataTextId));
        setTV(row, R.id.textView_okres_od, dateFrom);
        setTV(row, R.id.textView_okres_do, dateTo);
        setTV(row, R.id.textView_ilosc, ilosc == 1 ? "1,000" : "" + ilosc);
        setTV(row, R.id.textView_Jm, jm);
        setTV(row, R.id.textView_cena_netto, getCena(cenaNetto));
        setTV(row, R.id.textView_wartosc_akcyzy, wartoscAkcyzy);
        setTV(row, R.id.textView_wartosc_netto, display00(countWartosc(ilosc, cenaNetto)));

    }

    private double countWartosc(int ilosc, double cenaNetto) {
        double wartosc = cenaNetto * ilosc;
        sumWartoscNetto += wartosc;
        return wartosc;
    }

    private void setPodsumowanieRozliczenia(View rozliczenie) {
        View podsumowanie = rozliczenie.findViewById(R.id.row_sum);

        setTV(podsumowanie, R.id.textView_wartosc_netto, display00(sumWartoscNetto));
        kwotaVat = sumWartoscNetto * 0.23;
        setTV(podsumowanie, R.id.textView_kwota_vat, display00(kwotaVat));
        wartoscBrutto = sumWartoscNetto + kwotaVat;
        setTV(podsumowanie, R.id.textView_wartosc_brutto, display00(wartoscBrutto));
    }

    private void setOdczyt() {
        setTV(R.id.textView_odczyt, getString(R.string.odczyt_OSD, dateTo, readingTo));
    }

    private void setPodsumowanieTable() {
        TableLayout podsumowanie = (TableLayout) findViewById(R.id.table_podsumowanie);

        setTV(podsumowanie, R.id.textView_wartosc_netto, display00(sumWartoscNetto));
        setTV(podsumowanie, R.id.textView_kwota_vat, display00(kwotaVat));
        setTV(podsumowanie, R.id.textView_wartosc_brutto, display00(wartoscBrutto));
    }

    private void setWartoscFaktury() {
        setTV(R.id.textView_wartosc_faktury, getString(R.string.wartosc_faktury, display00(wartoscBrutto)));
    }

    private void setTV(View parent, int tvId, String text) {
        TextView tv = (TextView) parent.findViewById(tvId);
        setTV(tv, text);
    }

    private void setTV(int tvId, String text) {
        TextView tv = (TextView) findViewById(tvId);
        setTV(tv, text);
    }

    private void setTV(TextView tv, String text) {
        tv.setText(text);
    }

    private String getCena(double cena) {
        return new DecimalFormat("0.0000").format(cena);
    }

    private String display00(double value) {
        return new DecimalFormat("0.00").format(value);
    }
}
