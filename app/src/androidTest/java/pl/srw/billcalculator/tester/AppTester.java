package pl.srw.billcalculator.tester;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;

import org.jetbrains.annotations.NotNull;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.type.Provider;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AppTester extends Tester {

    private HistoryTester historyTester = new HistoryTester(this);
    private FormTester formTester = new FormTester(this);
    private SettingsTester settingsTester = new SettingsTester(this);
    private AboutTester aboutTester = new AboutTester();

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
        clickDrawerMenu(R.string.settings_label);
        return settingsTester;
    }

    public HistoryTester onHistory() {
        return historyTester;
    }

    public void checkPricesDialogIsVisible() {
        onView(withText(R.string.check_price_info_message)).check(matches(isDisplayed()));
    }

    @NotNull
    public AboutTester openAbout() {
        openDrawer();
        clickDrawerMenu(R.string.about_label);
        return aboutTester;
    }

    public AppTester clickHelp() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        clickText(R.string.action_help);
        return this;
    }

    @NotNull
    public AppTester clickInCenter() {
        clickView(R.id.empty_history);
        return this;
    }
}
