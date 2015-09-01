package pl.srw.billcalculator.util.strategy;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by kseweryn on 01.09.15.
 */
public abstract class Transitions {

    private static Transitions strategy;

    protected Transitions() {
    }

    public abstract void startActivity(Activity activity, Intent intent, View fromView);

    public static Transitions getInstance() {
        if (strategy == null) strategy = initialize();
        return strategy;
    }

    @NonNull
    private static Transitions initialize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return new LollipopTransitions();
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            return new JellyBeanTransitions();
        else
            return new DefaultTransitions();
    }
}
