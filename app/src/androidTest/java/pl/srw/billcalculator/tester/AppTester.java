package pl.srw.billcalculator.tester;

import android.support.test.espresso.NoMatchingViewException;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.type.Provider;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AppTester extends Tester {

    private HistoryTester historyTester = new HistoryTester(this);
    private FormTester formTester = new FormTester(this);
    private SettingsTester settingsTester = new SettingsTester(this);

    public AppTester skipCheckPricesDialogIfVisible() {
        try {
            clickText(R.string.check_prices_info_cancel);
        } catch (NoMatchingViewException ex) {
            // ignore
        }
        return this;
    }

    public FormTester openForm(Provider provider) {
        clickView(R.id.fab);
        switch (provider) {
            case PGE:
                clickView(R.id.fab_new_pge);
                break;
            case PGNIG:
                clickView(R.id.fab_new_pgnig);
                break;
            case TAURON:
                clickView(R.id.fab_new_tauron);
                break;
        }
        return formTester;
    }

    public SettingsTester openSettings() {
        openDrawer();
        clickDrawerMenu("Settings");
        return settingsTester;
    }

    public HistoryTester onHistory() {
        return historyTester;
    }

    public void checkPricesDialogIsVisible() {
        onView(withText(R.string.check_price_info_message)).check(matches(isDisplayed()));
    }
}
