package pl.srw.billcalculator.settings.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.type.Provider;

public class ProviderSettingsHelpActivity extends Activity {

    private static final String EXTRA_PROVIDER = "ProviderType";

    @BindView(R.id.iv_example) ImageView ivExample;

    public static Intent createIntent(final Context context, Provider provider) {
        Intent i = new Intent(context, ProviderSettingsHelpActivity.class);
        i.putExtra(EXTRA_PROVIDER, provider);
        return i;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Provider provider = (Provider) getIntent().getSerializableExtra(EXTRA_PROVIDER);

        setContentView(provider.helpLayoutRes);
        ButterKnife.bind(this);
        ivExample.setImageResource(provider.helpImageExampleRes);
    }

    @OnClick(R.id.b_close)
    public void closeDialog() {
        onBackPressed();
    }
}
