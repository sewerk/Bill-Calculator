package pl.srw.billcalculator.util;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by kseweryn on 18.06.15.
 */
public final class Animations {

    private static final long DURATION = 1000;
    private static final ObjectAnimator SHAKE = ObjectAnimator.ofFloat(null, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0).setDuration(DURATION);

    private Animations() {
    }

    public static void shake(View target) {
        SHAKE.setTarget(target);
        SHAKE.start();
    }

}
