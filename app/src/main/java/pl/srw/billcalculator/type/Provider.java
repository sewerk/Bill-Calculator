package pl.srw.billcalculator.type;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;

import static pl.srw.billcalculator.persistence.type.CurrentReadingType.PGE_DAY_TO;
import static pl.srw.billcalculator.persistence.type.CurrentReadingType.PGE_NIGHT_TO;
import static pl.srw.billcalculator.persistence.type.CurrentReadingType.PGE_TO;
import static pl.srw.billcalculator.persistence.type.CurrentReadingType.PGNIG_TO;
import static pl.srw.billcalculator.persistence.type.CurrentReadingType.TAURON_DAY_TO;
import static pl.srw.billcalculator.persistence.type.CurrentReadingType.TAURON_NIGHT_TO;
import static pl.srw.billcalculator.persistence.type.CurrentReadingType.TAURON_TO;

public enum Provider {
    PGE(R.string.settings_pge, R.string.settings_pge_summary, R.drawable.pge, R.drawable.pge_small,
            R.string.form_reading_unit_kWh, R.layout.settings_help_energy, R.drawable.pge_example, PGE_TO, PGE_DAY_TO, PGE_NIGHT_TO),
    PGNIG(R.string.settings_pgnig, R.string.settings_pgnig_summary, R.drawable.pgnig, R.drawable.pgnig_small,
            R.string.form_reading_unit_m3, R.layout.settings_help_gas, R.drawable.pgnig_example, PGNIG_TO),
    TAURON(R.string.settings_tauron, R.string.settings_tauron_summary, R.drawable.tauron, R.drawable.tauron_small,
            R.string.form_reading_unit_kWh, R.layout.settings_help_energy, R.drawable.tauron_example, TAURON_TO, TAURON_DAY_TO, TAURON_NIGHT_TO);

    public final int settingsTitleRes;
    public final int settingsDescRes;
    public final int logoRes;
    public final int logoSmallRes;
    public final int formReadingUnit;
    public final int helpLayoutRes;
    public final int helpImageExampleRes;
    public final CurrentReadingType singleReadingType;
    public final CurrentReadingType[] doubleReadingTypes;

    Provider(@StringRes final int settingsTitleRes, @StringRes final int descRes,
             @DrawableRes final int logoRes, @DrawableRes final int logoSmallRes,
             @StringRes int formReadingUnit, @LayoutRes int helpLayoutRes, @DrawableRes int helpImageExampleRes,
             CurrentReadingType singleReadingType, CurrentReadingType... doubleReadingTypes) {
        this.settingsTitleRes = settingsTitleRes;
        this.settingsDescRes = descRes;
        this.logoRes = logoRes;
        this.logoSmallRes = logoSmallRes;
        this.formReadingUnit = formReadingUnit;
        this.helpLayoutRes = helpLayoutRes;
        this.helpImageExampleRes = helpImageExampleRes;
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
