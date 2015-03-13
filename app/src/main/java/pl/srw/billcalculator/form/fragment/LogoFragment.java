package pl.srw.billcalculator.form.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.PgeForm;
import pl.srw.billcalculator.form.PgnigForm;

/**
 * Created by Kamil Seweryn.
 */
public abstract class LogoFragment extends Fragment {

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_bill_type_switch)
    public void switchProvider() {
        if (isEnergyForm())
            replaceForm(PgnigForm.getLogoSection(), PgnigForm.getInputSection());
        else
            replaceForm(PgeForm.getLogoSection(), PgeForm.getInputSection());
    }

    protected abstract boolean isEnergyForm();

    private void replaceForm(final LogoFragment logoSection, final InputFragment inputSection) {
        ((MainActivity) getActivity()).replaceFormFragments(logoSection, inputSection);
    }
}
