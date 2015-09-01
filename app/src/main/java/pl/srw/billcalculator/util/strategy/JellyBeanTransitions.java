package pl.srw.billcalculator.util.strategy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.view.View;

/**
 * Created by kseweryn on 01.09.15.
 */
public class JellyBeanTransitions extends Transitions {

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startActivity(Activity activity, Intent intent, View view) {
        ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
        activity.startActivity(intent, opts.toBundle());
    }
}
