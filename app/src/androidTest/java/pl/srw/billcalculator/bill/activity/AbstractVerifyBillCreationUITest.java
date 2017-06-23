package pl.srw.billcalculator.bill.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.history.HistoryGenerator;
import pl.srw.billcalculator.tester.AppTester;
import pl.srw.billcalculator.tester.BillTester;
import pl.srw.billcalculator.tester.FormTester;
import pl.srw.billcalculator.tester.HistoryTester;
import pl.srw.billcalculator.tester.ProviderSettingsTester;
import pl.srw.billcalculator.type.Provider;

@RunWith(AndroidJUnit4.class)
public abstract class AbstractVerifyBillCreationUITest {

    @Rule
    public final ActivityTestRule<DrawerActivity> testRule = new ActivityTestRule<>(DrawerActivity.class);

    private AppTester tester = new AppTester();

    @After
    public void tearDown() throws Exception {
        HistoryGenerator.clear();
    }

    @Test
    public void checkCalculatedBillValuesAndPrices() throws Exception {
        FormTester formTester = tester.skipCheckPricesDialogIfVisible()
                .openForm(getProvider());

        ProviderSettingsTester<FormTester> settingsTester = formTester.openProviderSettings();
        changePrices(settingsTester);
        settingsTester.close();

        BillTester billTester = inputFormValuesAndCalculate(formTester);
        verifyCalculatedValues(billTester);
        billTester.close();

        revertPricesToDefault();

        verifyAndOpenBillFromHistory(tester.onHistory());
        verifyCalculatedValues(billTester);
    }

    protected abstract Provider getProvider();

    protected abstract void changePrices(ProviderSettingsTester<FormTester> settingsTester);

    protected abstract BillTester inputFormValuesAndCalculate(FormTester formTester);

    protected abstract void verifyCalculatedValues(BillTester billTester);

    private void revertPricesToDefault() {
        tester.openSettings()
                .pickProvider(getProvider())
                .restoreDefault()
                .close()
                .close();
    }

    protected abstract void verifyAndOpenBillFromHistory(HistoryTester historyTester);
}
