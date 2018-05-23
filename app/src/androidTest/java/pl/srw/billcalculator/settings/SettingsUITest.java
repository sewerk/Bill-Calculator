package pl.srw.billcalculator.settings;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import pl.srw.billcalculator.data.ApplicationRepo;
import pl.srw.billcalculator.di.TestDependencies;
import pl.srw.billcalculator.tester.ProviderSettingsTester;
import pl.srw.billcalculator.tester.SettingsTester;
import pl.srw.billcalculator.tester.rule.ClosingActivityTestRule;
import pl.srw.billcalculator.type.Provider;

public class SettingsUITest {

    @Rule
    public ActivityTestRule<SettingsActivity> testRule = new ClosingActivityTestRule<>(SettingsActivity.class, false, false);

    @Inject ApplicationRepo applicationRepo;

    SettingsTester tester = new SettingsTester();

    @Before
    public void setUp() throws Exception {
        TestDependencies.INSTANCE.inject(this);

        applicationRepo.markFirstLaunch();

        testRule.launchActivity(null);
    }

    @Test
    public void providerSettingsTariffChangeCauseScreenChangeDisplayedValues() {
        tester
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
        tester
                .pickProvider(Provider.PGNIG)
                .getPreferenceAtLine(0)
                .changeValueTo("1.234")

                .getPreferenceAtLine(0)
                .hasSummary("1.234");
    }

    @Test
    public void settingPreferenceEmptyValueFillZeroSummary() {
        tester
                .pickProvider(Provider.PGNIG)
                .getPreferenceAtLine(0)
                .changeValueTo("")

                .getPreferenceAtLine(0)
                .hasSummary("0.00");
    }

    @Test
    public void restoreSettingsSetDefaultValues() {
        tester
                .pickProvider(Provider.TAURON)
                .getPreferenceAtLine(0)
                .pickOption("Taryfa dwustrefowa (G12)")

                .restoreDefault()
                .getPreferenceAtLine(0)
                .hasSummary("Taryfa całodobowa (G11)");
    }

    @Test
    public void inputValueStaysUnchangedAfterScreenRotation() throws Exception {
        String value = "1.23456";
        ProviderSettingsTester<SettingsTester>.SettingsDetailsListItem.InputDialog inputDialog =
                tester
                        .pickProvider(Provider.PGNIG)
                        .getPreferenceAtLine(1)
                        .openInput()
                        .changeValue(value);

        tester.changeOrientation(testRule);

        inputDialog.hasValue(value);
    }

    @Test
    public void opensPgeSettingsHelp() {
        tester
                .pickProvider(Provider.PGE)
                .openHelp()
                .clickOk();
    }

    @Test
    public void opensPgnigSettingsHelp() {
        tester
                .pickProvider(Provider.PGNIG)
                .openHelp()
                .clickOk();
    }

    @Test
    public void opensTauronSettingsHelp() {
        tester
                .pickProvider(Provider.TAURON)
                .openHelp()
                .clickOk();
    }
}
