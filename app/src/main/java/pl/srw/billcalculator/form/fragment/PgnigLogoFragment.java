package pl.srw.billcalculator.form.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.srw.billcalculator.R;

/**
* Created by Kamil Seweryn.
*/
public class PgnigLogoFragment extends LogoFragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_logo_pgnig, container, false);
    }

    @Override
    protected boolean isEnergyForm() {
        return false;
    }
}
