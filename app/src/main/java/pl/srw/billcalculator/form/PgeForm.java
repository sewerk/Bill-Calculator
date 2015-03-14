package pl.srw.billcalculator.form;

import pl.srw.billcalculator.form.fragment.InputFragment;
import pl.srw.billcalculator.form.fragment.LogoFragment;
import pl.srw.billcalculator.form.fragment.PgeInputFragment;
import pl.srw.billcalculator.form.fragment.PgeLogoFragment;

/**
 * Created by Kamil Seweryn.
 */
public class PgeForm {

    private static PgeLogoFragment pgeLogoFragment;
    private static PgeInputFragment pgeInputFragment;

    public static LogoFragment getLogoSection() {
        if (pgeLogoFragment == null)
            pgeLogoFragment = new PgeLogoFragment();
        return pgeLogoFragment;
    }

    public static InputFragment getInputSection() {
        if (pgeInputFragment == null)
            pgeInputFragment = new PgeInputFragment();
        return pgeInputFragment;
    }

}
