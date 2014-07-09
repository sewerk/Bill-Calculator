package pl.srw.billcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class GasBillActivity extends Activity {

    private String dateFrom;
    private String dateTo;
    private int readingFrom;
    private int readingTo;

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

    private void setOdczytyTable() {

    }

    private void setRozliczenieTable() {

    }

    private void setOdczyt() {

    }

    private void setPodsumowanieTable() {

    }

    private void setWartoscFaktury() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
