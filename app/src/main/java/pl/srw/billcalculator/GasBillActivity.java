package pl.srw.billcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class GasBillActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gas_bill);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
