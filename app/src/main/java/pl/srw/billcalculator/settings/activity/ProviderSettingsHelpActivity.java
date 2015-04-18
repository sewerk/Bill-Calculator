package pl.srw.billcalculator.settings.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public class ProviderSettingsHelpActivity extends Activity {

    private static final String EXTRA_LAYOUT_RESOURCE = "EXTRA_LAYOUT_RESOURCE";
    private static final String EXTRA_IMAGE_RESOURCE = "EXTRA_IMAGE_RESOURCE";
    @InjectView(R.id.iv_example) ImageView ivExample;

    public static Intent createIntent(final Context context,
            @LayoutRes final int layoutResource, @DrawableRes final int imageResource) {
        Intent i = new Intent(context, ProviderSettingsHelpActivity.class);
        i.putExtra(EXTRA_LAYOUT_RESOURCE, layoutResource);
        i.putExtra(EXTRA_IMAGE_RESOURCE, imageResource);
        return i;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int layoutResource = getIntent().getIntExtra(EXTRA_LAYOUT_RESOURCE, -1);
        setContentView(layoutResource);

        ButterKnife.inject(this);

        final int imageResource = getIntent().getIntExtra(EXTRA_IMAGE_RESOURCE, -1);
        ivExample.setImageResource(imageResource);
    }

    @OnClick(R.id.b_close)
    public void closeDialog() {
        onBackPressed();
    }
}
