package pl.srw.billcalculator.settings.prices;

import android.content.SharedPreferences;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class SharedPreferencesEnergyPrices extends SharedPreferencesPrices {

    protected static final String TARIFF_G11 = "G11";
    protected static final String TARIFF_G12 = "G12";

    @StringDef({TARIFF_G11, TARIFF_G12})
    @Retention(RetentionPolicy.SOURCE)
    protected @interface TariffOption {}

    protected SharedPreferencesEnergyPrices(SharedPreferences prefs) {
        super(prefs);
    }

    public abstract String getTariff();

    public boolean isTariffG12() {
        return TARIFF_G12.equals(getTariff());
    }
}
