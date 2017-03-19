package pl.srw.billcalculator.util.strategy;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * No transition to start activity on lower Android API
 */
class NoTransitions extends Transitions {

    @Override
    public void startActivity(Activity activity, Intent intent, View fromView) {
        activity.startActivity(intent);
    }
}
