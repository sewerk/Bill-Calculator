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
public class GasBillActivity extends Activity {

    public static final int PRICE_SCALE = 5;
    public static final BigDecimal VAT = new BigDecimal("0.23");
    private String dateFrom;
    private String dateTo;
    private int readingFrom;
    private int readingTo;

    private BigDecimal wspKonwersji;
    private BigDecimal oplataAbonamentowa;
    private BigDecimal paliwoGazowe;
    private BigDecimal dystrybucyjnaStala;
    private BigDecimal dystrybucyjnaZmienna;

    private BigDecimal sumWartoscNetto = BigDecimal.ZERO;
    private BigDecimal wartoscBrutto;
    private BigDecimal kwotaVat;

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

        setPrices();

        setOdczytyTable();
        setRozliczenieTable();
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

    private void setPrices() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String oplataAbonamentowaString = sharedPreferences.getString(
                getString(R.string.preferences_pgnig_abonamentowa), getString(R.string.price_abonamentowa));
        oplataAbonamentowa = new BigDecimal(oplataAbonamentowaString);

        String oplataPaliwoGazoweString = sharedPreferences.getString(
                getString(R.string.preferences_pgnig_paliwo_gazowe), getString(R.string.price_paliwo_gazowe));
        paliwoGazowe = new BigDecimal(oplataPaliwoGazoweString);

        String dystrybucyjnaStalaString = sharedPreferences.getString(
                getString(R.string.preferences_pgnig_dystrybucyjna_stala), getString(R.string.price_dystrybucyjna_stala));
        dystrybucyjnaStala = new BigDecimal(dystrybucyjnaStalaString);

        String dystrybucyjnaZmiennaString = sharedPreferences.getString(
                getString(R.string.preferences_pgnig_dystrybucyjna_zmienna), getString(R.string.price_dystrybucyjna_zmienna));
        dystrybucyjnaZmienna = new BigDecimal(dystrybucyjnaZmiennaString);

        String wspKonwersjiString = sharedPreferences.getString(
                getString(R.string.preferences_pgnig_wsp_konwersji), getString(R.string.price_wsp_konwersji));
        wspKonwersji = new BigDecimal(wspKonwersjiString);
    }

    private void setOdczytyTable() {
        TableLayout odczyty = (TableLayout) findViewById(R.id.table_odczyt);

        setTV(odczyty, R.id.textView_odczyt_poprzedni_na_dzien, dateFrom);
        setTV(odczyty, R.id.textView_odczyt_poprzedni, getString(R.string.odczyt_na_dzien, readingFrom));
        setTV(odczyty, R.id.textView_odczyt_biezacy_na_dzien, dateTo);
        setTV(odczyty, R.id.textView_odczyt_biezacy, getString(R.string.odczyt_na_dzien, readingTo));
        int zuzycie = getZuzycie();
        setTV(odczyty, R.id.textView_zuzycie, getString(R.string.zuzycie, zuzycie));

        setTV(odczyty, R.id.textView_zuzycie_razem, getString(R.string.zuzycie_razem, zuzycie));
        setTV(odczyty, R.id.textView_wsp_konwersji, getString(R.string.wsp_konwersji, wspKonwersji));
        int zuzycieKWh = getZuzycieKWh(zuzycie);
        setTV(odczyty, R.id.textView_zuzycie_razem_kWh, getString(R.string.zuzycie_razem_kWh, zuzycieKWh));
    }

    private int getZuzycieKWh(int zuzycie) {
        return new BigDecimal(zuzycie).multiply(wspKonwersji).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private int getZuzycie() {
        return readingTo - readingFrom;
    }

    private void setRozliczenieTable() {
        TableLayout rozliczenie = (TableLayout) findViewById(R.id.table_rozliczenie);
        int zuzycie = getZuzycieKWh(getZuzycie());
        int iloscMc = Dates.countMonth(dateFrom, dateTo);

        setRow(rozliczenie, R.id.row_abonamentowa, R.string.abonamentowa, iloscMc, R.string.mc, oplataAbonamentowa, "");
        setRow(rozliczenie, R.id.row_paliwo_gazowe, R.string.paliwo_gazowe, zuzycie, R.string.kWh, paliwoGazowe, "ZW");
        setRow(rozliczenie, R.id.row_dystrybucyjna_stala, R.string.dystrybucyjna_stala, iloscMc, R.string.mc, dystrybucyjnaStala, "");
        setRow(rozliczenie, R.id.row_dystrybucyjna_zmienna, R.string.dystrybucyjna_zmienna, zuzycie, R.string.kWh, dystrybucyjnaZmienna, "");

        setPodsumowanieRozliczenia(rozliczenie);
    }

    private void setRow(TableLayout rozliczenie, int rowId, int oplataTextId, int ilosc, int jmId, BigDecimal cenaNetto, String wartoscAkcyzy) {
        View row = rozliczenie.findViewById(rowId);
        setTV(row, R.id.textView_oplata, getString(oplataTextId));
        setTV(row, R.id.textView_okres_od, dateFrom);
        setTV(row, R.id.textView_okres_do, dateTo);
        setTV(row, R.id.textView_Jm, getString(jmId));
        if (jmId == R.string.kWh) {
            setTV(row, R.id.textView_ilosc, "" + ilosc);
        } else {
            setTV(row, R.id.textView_ilosc, Display.withScale(new BigDecimal(ilosc), 3));
        }
        setTV(row, R.id.textView_cena_netto, Display.withScale(cenaNetto, PRICE_SCALE));
        setTV(row, R.id.textView_wartosc_akcyzy, wartoscAkcyzy);
        setTV(row, R.id.textView_wartosc_netto, getWartosc(ilosc, cenaNetto));

    }

    private String getWartosc(int ilosc, BigDecimal cenaNetto) {
        BigDecimal wartosc = cenaNetto.multiply(new BigDecimal(ilosc));
        sumWartoscNetto = sumWartoscNetto.add(wartosc);
        return Display.toPay(wartosc);
    }

    private void setPodsumowanieRozliczenia(View rozliczenie) {
        View podsumowanie = rozliczenie.findViewById(R.id.row_sum);

        setTV(podsumowanie, R.id.textView_wartosc_netto, Display.toPay(sumWartoscNetto));
        kwotaVat = sumWartoscNetto.multiply(VAT);
        setTV(podsumowanie, R.id.textView_kwota_vat, Display.toPay(kwotaVat));
        wartoscBrutto = sumWartoscNetto.add(kwotaVat);
        setTV(podsumowanie, R.id.textView_wartosc_brutto, Display.toPay(wartoscBrutto));
    }

    private void setPodsumowanieTable() {
        TableLayout podsumowanie = (TableLayout) findViewById(R.id.table_podsumowanie);

        setTV(podsumowanie, R.id.textView_wartosc_netto, Display.toPay(sumWartoscNetto));
        setTV(podsumowanie, R.id.textView_kwota_vat, Display.toPay(kwotaVat));
        setTV(podsumowanie, R.id.textView_wartosc_brutto, Display.toPay(wartoscBrutto));
    }

    private void setWartoscFaktury() {
        setTV(R.id.textView_wartosc_faktury, getString(R.string.wartosc_faktury, Display.toPay(wartoscBrutto)));
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

}
