package pl.srw.billcalculator.form.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.srw.billcalculator.form.MainActivity;
import pl.srw.billcalculator.R;

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

    public void switchProvider() {
//        ((MainActivity) getActivity()).replaceFormFragments(isEnergyForm());
    }

    protected abstract boolean isEnergyForm();
}
