package pl.srw.billcalculator.type;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;

import static pl.srw.billcalculator.persistence.type.CurrentReadingType.*;

public enum Provider {
    PGE(R.string.pge_prices, R.string.settings_pge_summary, R.drawable.pge, R.drawable.pge_small,
            R.string.form_reading_unit_kWh, PGE_TO, PGE_DAY_TO, PGE_NIGHT_TO),
    PGNIG(R.string.pgnig_prices, R.string.settings_pgnig_summary, R.drawable.pgnig, R.drawable.pgnig_small,
            R.string.form_reading_unit_m3, PGNIG_TO),
    TAURON(R.string.tauron_prices, R.string.settings_tauron_summary, R.drawable.tauron, R.drawable.tauron_small,
            R.string.form_reading_unit_kWh, TAURON_TO, TAURON_DAY_TO, TAURON_NIGHT_TO);

    public final int titleRes;
    public final int settingsDescRes;
    public final int logoRes;
    public final int logoSmallRes;
    public final int formReadingUnit;
    public final CurrentReadingType singleReadingType;
    public final CurrentReadingType[] doubleReadingTypes;

    Provider(@StringRes final int titleRes, @StringRes final int descRes,
             @DrawableRes final int logoRes, @DrawableRes final int logoSmallRes,
             @StringRes int formReadingUnit,
             CurrentReadingType singleReadingType, CurrentReadingType... doubleReadingTypes) {
        this.titleRes = titleRes;
        this.settingsDescRes = descRes;
        this.logoRes = logoRes;
        this.logoSmallRes = logoSmallRes;
        this.formReadingUnit = formReadingUnit;
        this.singleReadingType = singleReadingType;
        this.doubleReadingTypes = doubleReadingTypes;
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

    public static Provider getByViewId(@IdRes int id) {
        if (id == R.id.fab_new_pge)
            return Provider.PGE;
        else if (id == R.id.fab_new_pgnig)
            return Provider.PGNIG;
        else if (id == R.id.fab_new_tauron)
            return Provider.TAURON;
        else
            throw new RuntimeException("Unhandled view Id=" + id);
    }

}
