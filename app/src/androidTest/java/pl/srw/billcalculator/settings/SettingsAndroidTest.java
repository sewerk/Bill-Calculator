package pl.srw.billcalculator.settings;

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
import pl.srw.billcalculator.tester.AppTester;
import pl.srw.billcalculator.tester.SettingsTester;

@RunWith(AndroidJUnit4.class)
public class SettingsAndroidTest {

    @Rule
    public ActivityTestRule<DrawerActivity> testRule = new ActivityTestRule<>(DrawerActivity.class, false, false);

    @Inject SettingsRepo settingsRepo;

    AppTester tester = new AppTester();

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        TestApplicationComponent component = DaggerTestApplicationComponent.builder()
                .applicationModule(new ApplicationModule(context))
                .build();
        component.inject(this);

        settingsRepo.markFirstLaunch();

        testRule.launchActivity(null);
    }

    @Test
    public void providerSettingsTariffChangeCauseScreenChangeDisplayedValues() {
        tester.openSettings()
                .pickProvider(SettingsTester.PGE)

                .getPreferenceAtLine(0)
                .pickOption("Taryfa dwustrefowa (G12)")

                .getPreferenceAtLine(1)
                .hasTitle("za energię czynną (strefa dzienna)")

                .getPreferenceAtLine(0)
                .pickOption("Taryfa całodobowa (G11)")

                .getPreferenceAtLine(1)
                .hasTitle("za energię czynną");
    }

    @Test
    public void settingPreferenceValueUpdatedSummary() {
        tester.openSettings()
                .pickProvider(SettingsTester.PGNIG)
                .getPreferenceAtLine(0)
                .changeValueTo("1.234")

                .getPreferenceAtLine(0)
                .hasSummary("1.234");
    }

    @Test
    public void settingPreferenceEmptyValueFillZeroSummary() {
        tester.openSettings()
                .pickProvider(SettingsTester.PGNIG)
                .getPreferenceAtLine(0)
                .changeValueTo("")

                .getPreferenceAtLine(0)
                .hasSummary("0.00");
    }

    @Test
    public void restoreSettingsSetDefaultValues() {
        tester.openSettings()
                .pickProvider(SettingsTester.TAURON)
                .getPreferenceAtLine(0)
                .pickOption("Taryfa dwustrefowa (G12)")

                .restoreDefault()
                .getPreferenceAtLine(0)
                .hasSummary("Taryfa całodobowa (G11)");
    }
}
