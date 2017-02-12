package pl.srw.billcalculator;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import pl.srw.billcalculator.di.ApplicationModule;
import pl.srw.billcalculator.di.DaggerTestApplicationComponent;
import pl.srw.billcalculator.di.TestApplicationComponent;
import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.settings.global.SettingsRepo;
import pl.srw.billcalculator.tester.SettingsTester;

@RunWith(AndroidJUnit4.class)
public class ProviderSettingsAndroidTest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>(DrawerActivity.class, false, false);

    @Inject SettingsRepo settingsRepo;

    SettingsTester tester = new SettingsTester();

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        TestApplicationComponent component = DaggerTestApplicationComponent.builder()
                .applicationModule(new ApplicationModule(context))
                .build();
        component.inject(this);

        settingsRepo.markFirstLaunch();

        mActivityTestRule.launchActivity(null);
    }

    @Test
    public void providerSettingsTariffChangeCauseScreenChangeDisplayedValues() {
        tester
                .openSettings()
                .pickProvider(SettingsTester.PGE)
                .getPreferenceLineAt(0)
                .pickOption("Taryfa dwustrefowa (G12)");
        tester
                .getPreferenceLineAt(1)
                .hasTitle("za energię czynną (strefa dzienna)");
        tester
                .getPreferenceLineAt(0)
                .pickOption("Taryfa całodobowa (G11)");
        tester
                .getPreferenceLineAt(1)
                .hasTitle("za energię czynną");
    }

    @Test
    public void settingPreferenceValueUpdatedSummary() {
        tester
                .openSettings()
                .pickProvider(SettingsTester.PGNIG)
                .getPreferenceLineAt(0)
                .changeValueTo("1.234");

        tester.getPreferenceLineAt(0)
                .hasSummary("1.234");
    }

    @Test
    public void settingPreferenceEmptyValueFillZeroSummary() {
        tester
                .openSettings()
                .pickProvider(SettingsTester.PGNIG)
                .getPreferenceLineAt(0)
                .changeValueTo("");

        tester.getPreferenceLineAt(0)
                .hasSummary("0.00");
    }

    @Test
    public void restoreSettingsSetDefaultValues() {
        tester
                .openSettings()
                .pickProvider(SettingsTester.TAURON)
                .getPreferenceLineAt(0)
                .pickOption("Taryfa dwustrefowa (G12)");
        tester
                .restoreDefault()
                .getPreferenceLineAt(0)
                .hasSummary("Taryfa całodobowa (G11)");
    }
}
