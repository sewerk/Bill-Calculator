package pl.srw.billcalculator.util.strategy;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by kseweryn on 01.09.15.
 */
public class DefaultTransitions extends Transitions {

    @Override
    public void startActivity(Activity activity, Intent intent, View fromView) {
        activity.startActivity(intent);
    }
}
