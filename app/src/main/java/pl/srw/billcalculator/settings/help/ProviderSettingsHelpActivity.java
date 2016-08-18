package pl.srw.billcalculator.settings.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public class ProviderSettingsHelpActivity extends Activity {

    private static final String EXTRA_LAYOUT_RESOURCE = "EXTRA_LAYOUT_RESOURCE";
    private static final String EXTRA_IMAGE_RESOURCE = "EXTRA_IMAGE_RESOURCE";
    @InjectExtra(EXTRA_LAYOUT_RESOURCE) int layoutResource;
    @InjectExtra(EXTRA_IMAGE_RESOURCE) int imageResource;
    @BindView(R.id.iv_example) ImageView ivExample;

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
        Dart.inject(this);
        setContentView(layoutResource);

        ButterKnife.bind(this);
        ivExample.setImageResource(imageResource);
    }

    @OnClick(R.id.b_close)
    public void closeDialog() {
        onBackPressed();
    }
}
