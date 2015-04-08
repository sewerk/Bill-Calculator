package pl.srw.billcalculator.form;

import pl.srw.billcalculator.form.fragment.InputFragment;
import pl.srw.billcalculator.form.fragment.LogoFragment;
import pl.srw.billcalculator.form.fragment.PgeInputFragment;
import pl.srw.billcalculator.form.fragment.PgeLogoFragment;
import pl.srw.billcalculator.form.fragment.PgnigInputFragment;
import pl.srw.billcalculator.form.fragment.PgnigLogoFragment;

/**
 * Created by Kamil Seweryn.
 */
public enum ProviderForm {

    PGE(new PgeLogoFragment(), new PgeInputFragment()),
    PGNIG(new PgnigLogoFragment(), new PgnigInputFragment());

    private final LogoFragment logoFragment;
    private final InputFragment inputFragment;

    ProviderForm(final LogoFragment logo, final InputFragment input) {
        this.logoFragment = logo;
        this.inputFragment = input;
    }

    public LogoFragment getLogoFragment() {
        return logoFragment;
    }

    public InputFragment getInputFragment() {
        return inputFragment;
    }
}
