package pl.srw.billcalculator.form.tauron;

import pl.srw.billcalculator.form.DoubleReadingsFormPresenter;
import pl.srw.billcalculator.settings.GeneralPreferences;

/**
 * Created by kseweryn on 27.05.15.
 */
public class TauronFormPresenter extends DoubleReadingsFormPresenter {

    @Override
    public boolean isTariffG12() {
        return GeneralPreferences.isTauronTariffG12();
    }
}
