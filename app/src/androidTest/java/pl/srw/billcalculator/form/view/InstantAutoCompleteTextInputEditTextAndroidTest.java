package pl.srw.billcalculator.form.view;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import pl.srw.billcalculator.di.ApplicationModule;
import pl.srw.billcalculator.di.DaggerTestApplicationComponent;
import pl.srw.billcalculator.di.TestApplicationComponent;
import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices;
import pl.srw.billcalculator.tester.FormTester;
import pl.srw.billcalculator.testutils.HistoryGenerator;
import pl.srw.billcalculator.type.Provider;

@RunWith(AndroidJUnit4.class)
public class InstantAutoCompleteTextInputEditTextAndroidTest {

    @Rule
    public ActivityTestRule<DrawerActivity> testRule = new ActivityTestRule<>(DrawerActivity.class, false, false);

    @Inject HistoryGenerator historyGenerator;
    @Inject PgePrices pgePrices;

    private FormTester tester = new FormTester();

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        TestApplicationComponent component = DaggerTestApplicationComponent.builder()
                .applicationModule(new ApplicationModule(context))
                .build();
        component.inject(this);

        historyGenerator.clear();
        pgePrices.setTariff(SharedPreferencesEnergyPrices.TARIFF_G11);

        testRule.launchActivity(null);
    }

    @After
    public void tearDown() throws Exception {
        historyGenerator.clear();
    }

    @Test
    public void formOpened_whenEntryInHistoryExist_autoCompleteShowUp() {
        historyGenerator.generatePgeG11Bill(11);

        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGE);

        tester.autoCompleteContains("11");
    }

    @Test
    public void formOpened_whenDuplicatedEntriesInHistory_showsOneAutoCompletion() {
        historyGenerator.generatePgeG11Bill(25);
        historyGenerator.generatePgeG11Bill(25);

        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGE);

        tester.autoCompleteContains("25");
    }

    @Test
    public void formOpened_whenManyEntriesInHistory_showsOnlyHighestValue() {
        historyGenerator.generatePgeG11Bills(20);

        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGE);

        tester.autoCompleteContains("30");
    }

    @Test
    public void formOpened_whenTextPartiallyFilled_showsAllMatchingEntries() {
        historyGenerator.generatePgeG11Bill(19);
        historyGenerator.generatePgeG11Bill(49);
        historyGenerator.generatePgeG11Bill(115);
        historyGenerator.generatePgeG11Bill(199);
        historyGenerator.generatePgeG11Bill(230);

        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGE)
                .putIntoReadingFrom("1");

        tester.autoCompleteDisplaysInOrder("199", "115", "19");
    }
}
