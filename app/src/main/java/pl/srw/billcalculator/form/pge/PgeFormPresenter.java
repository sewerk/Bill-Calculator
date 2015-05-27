package pl.srw.billcalculator.form.pge;

import pl.srw.billcalculator.form.DoubleReadingsFormPresenter;
import pl.srw.billcalculator.settings.GeneralPreferences;

/**
 * Created by kseweryn on 30.04.15.
 */
public class PgeFormPresenter extends DoubleReadingsFormPresenter {

    @Override
    public boolean isTariffG12() {
        return GeneralPreferences.isPgeTariffG12();
    }

}
