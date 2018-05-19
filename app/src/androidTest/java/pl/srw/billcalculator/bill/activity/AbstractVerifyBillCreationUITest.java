package pl.srw.billcalculator.bill.activity;

import android.Manifest;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import io.reactivex.plugins.RxJavaPlugins;
import pl.srw.billcalculator.di.TestDependencies;
import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.history.HistoryGenerator;
import pl.srw.billcalculator.tester.AppTester;
import pl.srw.billcalculator.tester.BillTester;
import pl.srw.billcalculator.tester.FormTester;
import pl.srw.billcalculator.tester.HistoryTester;
import pl.srw.billcalculator.tester.ProviderSettingsTester;
import pl.srw.billcalculator.tester.idling.RxEspressoScheduleHandler;
import pl.srw.billcalculator.tester.rule.PermissionsIntentsTestRule;
import pl.srw.billcalculator.type.Provider;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasFlag;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.intent.matcher.UriMatchers.hasHost;
import static android.support.test.espresso.intent.matcher.UriMatchers.hasPath;
import static android.support.test.espresso.intent.matcher.UriMatchers.hasScheme;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public abstract class AbstractVerifyBillCreationUITest {

    private final static RxEspressoScheduleHandler rxEspressoScheduleHandler = new RxEspressoScheduleHandler();

    @Inject HistoryGenerator historyGenerator;

    @Rule
    public final IntentsTestRule<DrawerActivity> testRule
            = new PermissionsIntentsTestRule<>(DrawerActivity.class, false, false,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    private AppTester tester = new AppTester();

    @Before
    public void setUp() {
        RxJavaPlugins.setScheduleHandler(rxEspressoScheduleHandler);
        Espresso.registerIdlingResources(rxEspressoScheduleHandler.getIdlingResource());
        TestDependencies.INSTANCE.inject(this);
        historyGenerator.clear();
        testRule.launchActivity(null);
    }

    @After
    public void tearDown() {
        historyGenerator.clear();
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
        verifyPrintBill(billTester);
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

    private void verifyPrintBill(BillTester billTester) {
        billTester.print();

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasFlag(Intent.FLAG_GRANT_READ_URI_PERMISSION),
                hasType(BillActivity.MIME_APPLICATION_PDF),
                hasData(allOf(
                        hasScheme("content"),
                        hasHost("pl.srw.billcalculator.fileprovider"),
                        hasPath(containsString("printout")),
                        hasPath(containsString(getProvider().toString()))
                ))
        ));
    }
}
