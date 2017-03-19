package pl.srw.billcalculator.util.strategy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

class LollipopTransitions extends Transitions {

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startActivity(Activity activity, Intent intent, View view) {
        ActivityOptionsCompat opts = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, view, Transitions.BILL_VIEW_TRANSITION_NAME);
        activity.startActivity(intent, opts.toBundle());
    }
}
