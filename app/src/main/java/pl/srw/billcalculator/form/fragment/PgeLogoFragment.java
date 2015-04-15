package pl.srw.billcalculator.form.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.settings.Provider;
import pl.srw.billcalculator.settings.activity.ProviderSettingsActivity;

/**
* Created by Kamil Seweryn.
*/
public class PgeLogoFragment extends LogoFragment {

    @InjectView(R.id.textView_tariff_change) TextView tvTariffChange;
    @InjectView(R.id.textView_tariff) TextView tvTariff;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_logo_pge, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeLinkOnTariffChange();
    }

    @Override
    public void onStart() {
        super.onStart();
        setTariffLabel();
    }

    private void makeLinkOnTariffChange() {
        tvTariffChange.setText(Html.fromHtml(getString(R.string.tariff_change)));
    }

    private void setTariffLabel() {
        if (GeneralPreferences.isPgeTariffG12()) {
            tvTariff.setText(R.string.pge_tariff_G12_on_bill);
        } else {
            tvTariff.setText(R.string.pge_tariff_G11_on_bill);
        }
    }

    @OnClick(R.id.textView_tariff_change)
    public void moveToChangeTariff() {
        final Intent intent = ProviderSettingsActivity
                .createIntent(getActivity(), Provider.PGE);
        startActivity(intent);
    }

    @Override
    protected boolean isEnergyForm() {
        return true;
    }
}
