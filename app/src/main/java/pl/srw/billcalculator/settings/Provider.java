package pl.srw.billcalculator.settings;

import android.support.annotation.StringRes;

import pl.srw.billcalculator.R;

/**
 * Created by kseweryn on 15.04.15.
 */
public enum Provider {
    PGE(R.string.pge_prices, R.string.settings_pge_summary),
    PGNIG(R.string.pgnig_prices, R.string.settings_pgnig_summary),
    TAURON(R.string.tauron_prices, R.string.settings_tauron_summary);

    public final int titleRes;
    public final int descRes;

    Provider(@StringRes final int titleRes, @StringRes final int descRes) {
        this.titleRes = titleRes;
        this.descRes = descRes;
    }

}
