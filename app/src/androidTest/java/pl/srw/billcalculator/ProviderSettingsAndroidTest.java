package pl.srw.billcalculator;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.tester.SettingsTester;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProviderSettingsAndroidTest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>(DrawerActivity.class);

    @Test
    public void providerSettingsTariffChangeCauseScreenChangeDisplayedValues() {
        final SettingsTester tester = new SettingsTester();
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
        final SettingsTester tester = new SettingsTester();
        tester
                .openSettings()
                .pickProvider(SettingsTester.PGNIG)
                .getPreferenceLineAt(0)
                .changeValueTo("1.234");

        tester.getPreferenceLineAt(0)
                .hasSummary("1.234");
    }

    @Test
    public void restoreSettingsSetDefaultValues() {
        final SettingsTester tester = new SettingsTester();
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
