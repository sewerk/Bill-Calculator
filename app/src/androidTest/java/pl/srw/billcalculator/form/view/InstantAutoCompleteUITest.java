package pl.srw.billcalculator.form.view;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import pl.srw.billcalculator.data.settings.prices.EnergyTariff;
import pl.srw.billcalculator.data.settings.prices.PricesRepo;
import pl.srw.billcalculator.di.TestDependencies;
import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.history.HistoryGenerator;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.tester.AppTester;
import pl.srw.billcalculator.tester.rule.ClosingActivityTestRule;
import pl.srw.billcalculator.type.Provider;

public class InstantAutoCompleteUITest {

    @Rule
    public ActivityTestRule<DrawerActivity> testRule = new ClosingActivityTestRule<>(DrawerActivity.class, false, false);

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Inject HistoryGenerator historyGenerator;
    @Inject PricesRepo pricesRepo;
    @Inject PgePrices pgePrices;

    private AppTester tester = new AppTester();

    @Before
    public void setUp() {
        TestDependencies.INSTANCE.inject(this);
        historyGenerator.clear();
        pricesRepo.updateTariff(Provider.PGE, EnergyTariff.G11);
        pgePrices.setTariff(EnergyTariff.G11);

        testRule.launchActivity(null);
    }

    @After
    public void tearDown() {
        historyGenerator.clear();
    }

    @Test
    public void formOpened_whenEntryInHistoryExist_autoCompleteShowUp() {
        historyGenerator.generatePgeG11Bill(11);

        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGE)

                .autoCompleteContains("11");
    }

    @Test
    public void formOpened_whenDuplicatedEntriesInHistory_showsOneAutoCompletion() {
        historyGenerator.generatePgeG11Bill(25);
        historyGenerator.generatePgeG11Bill(25);

        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGE)

                .autoCompleteContains("25");
    }

    @Test
    public void formOpened_whenManyEntriesInHistory_showsOnlyHighestValue() {
        historyGenerator.generatePgeG11Bills(20);

        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGE)

                .autoCompleteContains("30");
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
                .putIntoReadingFrom("1")

                .autoCompleteDisplaysInOrder("199", "115", "19");
    }
}
