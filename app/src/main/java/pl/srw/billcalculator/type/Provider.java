package pl.srw.billcalculator.type;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import pl.srw.billcalculator.R;

/**
 * Created by kseweryn on 15.04.15.
 */
public enum Provider {
    PGE(R.string.pge_prices, R.string.settings_pge_summary, R.drawable.pge, R.drawable.pge_small),
    PGNIG(R.string.pgnig_prices, R.string.settings_pgnig_summary, R.drawable.pgnig, R.drawable.pgnig_small),
    TAURON(R.string.tauron_prices, R.string.settings_tauron_summary, R.drawable.tauron, R.drawable.tauron_small);

    public final int titleRes;
    public final int settingsDescRes;
    public final int logoRes;
    public final int logoSmallRes;


    Provider(@StringRes final int titleRes, @StringRes final int descRes,
             @DrawableRes final int logoRes, @DrawableRes final int logoSmallRes) {
        this.titleRes = titleRes;
        this.settingsDescRes = descRes;
        this.logoRes = logoRes;
        this.logoSmallRes = logoSmallRes;
    }

    public static Provider mapFrom(pl.srw.billcalculator.persistence.type.BillType persistenceType) {
        switch (persistenceType) {
            case PGNIG:
                return PGNIG;
            case PGE_G11:
            case PGE_G12:
                return PGE;
            case TAURON_G11:
            case TAURON_G12:
                return TAURON;
        }
        throw new EnumVariantNotHandledException(persistenceType);
    }
}