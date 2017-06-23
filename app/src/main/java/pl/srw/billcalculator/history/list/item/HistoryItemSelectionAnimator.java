package pl.srw.billcalculator.history.list.item;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import pl.srw.billcalculator.R;

/**
 * Animates history item selection
 */
class HistoryItemSelectionAnimator {

    private final AnimatorSet changeLogoAnimator;

    HistoryItemSelectionAnimator(Context context, ImageView imageView) {
        final Animator outAnimator = AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out);
        final ObjectAnimator changeAnimator = ObjectAnimator.ofInt(imageView, "imageResource", 0, 0).setDuration(0);
        final Animator inAnimator = AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in);
        changeLogoAnimator = new AnimatorSet();
        changeLogoAnimator.playSequentially(outAnimator, changeAnimator, inAnimator);
        changeLogoAnimator.setTarget(imageView);
    }

    void changeTo(@DrawableRes int drawable) {
        ObjectAnimator objectAnimator = (ObjectAnimator) changeLogoAnimator.getChildAnimations().get(1);
        objectAnimator.setIntValues(drawable, drawable);
        changeLogoAnimator.start();
    }
}
