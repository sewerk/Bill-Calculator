package pl.srw.billcalculator.form;

import pl.srw.billcalculator.form.fragment.InputFragment;
import pl.srw.billcalculator.form.fragment.LogoFragment;
import pl.srw.billcalculator.form.fragment.PgnigInputFragment;
import pl.srw.billcalculator.form.fragment.PgnigLogoFragment;

/**
 * Created by Kamil Seweryn.
 */
public class PgnigForm {

    private static PgnigLogoFragment pgnigLogoFragment;
    private static PgnigInputFragment pgnigInputFragment;

    public static LogoFragment getLogoSection() {
        if (pgnigLogoFragment == null)
            pgnigLogoFragment = new PgnigLogoFragment();
        return pgnigLogoFragment;
    }

    public static InputFragment getInputSection() {
        if (pgnigInputFragment == null)
            pgnigInputFragment = new PgnigInputFragment();
        return pgnigInputFragment;
    }

}
