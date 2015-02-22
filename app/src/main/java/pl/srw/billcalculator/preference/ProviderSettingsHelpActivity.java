package pl.srw.billcalculator.preference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public class ProviderSettingsHelpActivity extends Activity {

    public static final String EXTRA_LAYOUT_RESOURCE = "EXTRA_LAYOUT_RESOURCE";
    public static final int DEFAULT_LAYOUT_RESOURCE = R.layout.pge_settings_help;

    public static Intent createIntent(final Context context, final int layoutResource) {
        Intent i = new Intent(context, ProviderSettingsHelpActivity.class);
        i.putExtra(EXTRA_LAYOUT_RESOURCE, layoutResource);
        return i;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int layoutResource = getIntent().getIntExtra(EXTRA_LAYOUT_RESOURCE, DEFAULT_LAYOUT_RESOURCE);
        setContentView(layoutResource);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.b_close)
    public void closeDialog() {
        onBackPressed();
    }
}