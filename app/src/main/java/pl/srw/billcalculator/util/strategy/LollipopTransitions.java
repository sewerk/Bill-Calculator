package pl.srw.billcalculator.util.strategy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.util.Pair;
import android.view.View;

import pl.srw.billcalculator.bill.activity.BillActivity;

/**
 * Created by kseweryn on 01.09.15.
 */
public class LollipopTransitions extends Transitions {

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startActivity(Activity activity, Intent intent, View view) {
        Pair<View, String> viewTransition = Pair.create(view, BillActivity.VIEW_TRANSITION_NAME);
        ActivityOptions opts = ActivityOptions.makeSceneTransitionAnimation(activity, viewTransition);
        activity.startActivity(intent, opts.toBundle());
    }
}
