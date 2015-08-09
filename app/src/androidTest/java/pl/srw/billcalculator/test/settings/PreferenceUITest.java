package pl.srw.billcalculator.test.settings;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.settings.activity.SettingsActivity;
import pl.srw.billcalculator.testutils.PreferenceUtil;
import pl.srw.billcalculator.type.Provider;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static pl.srw.billcalculator.testutils.EspressoHelper.waitForUi;

/**
 * Created by kseweryn on 17.07.15.
 */
public class PreferenceUITest {

    @Rule
    public ActivityTestRule<SettingsActivity> activity = new ActivityTestRule<>(SettingsActivity.class, true);

    @Before
    public void setUp() throws Exception {
        GeneralPreferences.markFirstLaunch();
        PreferenceUtil.changeToG11Tariff(Provider.PGE);
        PreferenceUtil.changeToG12Tariff(Provider.TAURON);
    }

    @Test
    public void shouldChangeEnableOnTariffChange() {
        onView(withText(R.string.pge_prices)).perform(click());
        // change tariff to G12
        onData(withTitle(R.string.pge_tariff_in_preferences)).perform(click());
        onView(withText("Taryfa dwustrefowa (G12)")).perform(click());
        // verify G12 fields disabled
        onData(withTitle(R.string.pge_tariff_in_preferences)).onChildView(withId(android.R.id.summary)).check(matches(withText(endsWith("(G12)"))));
        onData(withTitle(R.string.za_energie_czynna)).check(matches(not(isEnabled())));
        onData(withTitle(R.string.oplata_sieciowa)).check(matches(not(isEnabled())));
        // verify G12 fields enabled
        onData(withTitle(R.string.za_energie_czynna_G12dzien)).check(matches(isEnabled()));
        onData(withTitle(R.string.za_energie_czynna_G12noc)).check(matches(isEnabled()));
        onData(withTitle(R.string.oplata_sieciowa_G12dzien)).check(matches(isEnabled()));
        onData(withTitle(R.string.oplata_sieciowa_G12noc)).check(matches(isEnabled()));

        // change tariff to G11
        onData(withTitle(R.string.pge_tariff_in_preferences)).perform(click());
        onView(withText("Taryfa całodobowa (G11)")).perform(click());
        // verify G11 fields enabled
        onData(withTitle(R.string.pge_tariff_in_preferences)).onChildView(withId(android.R.id.summary)).check(matches(withText(endsWith("(G11)"))));
        onData(withTitle(R.string.za_energie_czynna)).check(matches((isEnabled())));
        onData(withTitle(R.string.oplata_sieciowa)).check(matches((isEnabled())));
        // verify G11 fields disabled
        onData(withTitle(R.string.za_energie_czynna_G12dzien)).check(matches(not(isEnabled())));
        onData(withTitle(R.string.za_energie_czynna_G12noc)).check(matches(not(isEnabled())));
        onData(withTitle(R.string.oplata_sieciowa_G12dzien)).check(matches(not(isEnabled())));
        onData(withTitle(R.string.oplata_sieciowa_G12noc)).check(matches(not(isEnabled())));
    }

    @Test
    public void shouldFillZeroIfEmptyValue() {
        // given: preference edit dialog
        onView(withText(R.string.pgnig_prices)).perform(click());
        onData(withTitle(R.string.wspolczynnik_konwersji)).perform(click());
        // when: fill empty value
        onView(withId(android.R.id.edit)).perform(clearText());
        waitForUi();
        onView(withId(android.R.id.button1)).perform(click());
        // then: verify value is 0.00
        onData(withTitle(R.string.wspolczynnik_konwersji)).onChildView(withId(android.R.id.summary)).check(matches(withText(startsWith("0.00"))));
    }

    @Test
    public void shouldRestoreDefaultValuesOnButtonClicked() {
        // given: preference screen with G12 tariff set
        onView(withText(R.string.tauron_prices)).perform(click());
        onData(withTitle(R.string.tauron_tariff_in_preferences)).onChildView(withId(android.R.id.summary)).check(matches(withText(endsWith("(G12)"))));
        // when: button clicked
        onView(withId(R.id.action_default)).perform(click());
        onView(withText(R.string.restore_prices_confirm)).perform(click());
        // then: default value G11 is set
        onData(withTitle(R.string.tauron_tariff_in_preferences)).onChildView(withId(android.R.id.summary)).check(matches(withText(endsWith("(G11)"))));
    }
}
