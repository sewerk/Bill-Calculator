package pl.srw.billcalculator.testutils;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by Kamil Seweryn.
 */
public final class SoloHelper {

    private SoloHelper() {
    }

    public static boolean isVisible(Solo solo, @DrawableRes int drawable) {
        //TODO return solo.getCurrentActivity().getResources().getDrawable(drawable).isVisible();
        return false;
    }

    public static String getString(Solo solo, @StringRes final int strRes) {
        return solo.getCurrentActivity().getString(strRes);
    }

    public static EditText findET(Solo solo, @IdRes final int edId) {
        return (EditText) solo.getView(edId);
    }
}
