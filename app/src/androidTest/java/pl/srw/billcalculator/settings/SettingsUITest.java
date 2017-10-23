package pl.srw.billcalculator.settings;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import pl.srw.billcalculator.di.TestDependencies;
import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.settings.global.SettingsRepo;
import pl.srw.billcalculator.tester.AppTester;
import pl.srw.billcalculator.tester.rule.ClosingActivityTestRule;
import pl.srw.billcalculator.type.Provider;

public class SettingsUITest {

    @Rule
    public ActivityTestRule<DrawerActivity> testRule = new ClosingActivityTestRule<>(DrawerActivity.class, false, false);

    @Inject SettingsRepo settingsRepo;

    AppTester tester = new AppTester();

    @Before
    public void setUp() throws Exception {
        TestDependencies.inject(this);

        settingsRepo.markFirstLaunch();

        testRule.launchActivity(null);
    }

    @Test
    public void providerSettingsTariffChangeCauseScreenChangeDisplayedValues() {
        tester.openSettings()
                .pickProvider(Provider.PGE)

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
                .pickProvider(Provider.PGNIG)
                .getPreferenceAtLine(0)
                .changeValueTo("1.234")

                .getPreferenceAtLine(0)
                .hasSummary("1.234");
    }

    @Test
    public void settingPreferenceEmptyValueFillZeroSummary() {
        tester.openSettings()
                .pickProvider(Provider.PGNIG)
                .getPreferenceAtLine(0)
                .changeValueTo("")

                .getPreferenceAtLine(0)
                .hasSummary("0.00");
    }

    @Test
    public void restoreSettingsSetDefaultValues() {
        tester.openSettings()
                .pickProvider(Provider.TAURON)
                .getPreferenceAtLine(0)
                .pickOption("Taryfa dwustrefowa (G12)")

                .restoreDefault()
                .getPreferenceAtLine(0)
                .hasSummary("Taryfa całodobowa (G11)");
    }

    @Test
    public void opensPgeSettingsHelp() {
        tester.openSettings()
                .pickProvider(Provider.PGE)
                .openHelp()
                .clickOk();
    }

    @Test
    public void opensPgnigSettingsHelp() {
        tester.openSettings()
                .pickProvider(Provider.PGNIG)
                .openHelp()
                .clickOk();
    }

    @Test
    public void opensTauronSettingsHelp() {
        tester.openSettings()
                .pickProvider(Provider.TAURON)
                .openHelp()
                .clickOk();
    }
}
