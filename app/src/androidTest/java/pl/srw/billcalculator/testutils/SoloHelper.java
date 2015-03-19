package pl.srw.billcalculator.testutils;

import android.app.Fragment;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.widget.EditText;

import com.robotium.solo.Solo;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.fragment.PgeLogoFragment;
import pl.srw.billcalculator.form.fragment.PgnigLogoFragment;

/**
 * Created by Kamil Seweryn.
 */
public final class SoloHelper {

    private SoloHelper() {
    }

    public static String getString(Solo solo, @StringRes final int strRes) {
        return solo.getCurrentActivity().getString(strRes);
    }

    public static EditText findET(Solo solo, @IdRes final int edId) {
        return (EditText) solo.getView(edId);
    }

    public static void switchBill(final Solo solo) {
        solo.clickOnView(solo.getView(R.id.iv_bill_type_switch));
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    public static boolean isPgnigForm(final Solo solo) {
        solo.waitForFragmentById(R.id.fl_logo_section);
        final Fragment fragment = solo.getCurrentActivity().getFragmentManager().findFragmentById(R.id.fl_logo_section);
        return fragment instanceof PgnigLogoFragment;
    }

    public static boolean isPgeForm(final Solo solo) {
        solo.waitForFragmentById(R.id.fl_logo_section);
        final Fragment fragment = solo.getCurrentActivity().getFragmentManager().findFragmentById(R.id.fl_logo_section);
        return fragment instanceof PgeLogoFragment;
    }

    public static void redrawActivity(final Solo solo) {
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
    }

    public static int getDrawableFromRow(final Solo solo, final int index) {
        return (int) solo.getImage(index + 2).getTag();
    }
}
