package pl.srw.billcalculator.history;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import pl.srw.billcalculator.di.DaggerTestApplicationComponent;
import pl.srw.billcalculator.di.TestApplicationComponent;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.tester.AppTester;
import pl.srw.billcalculator.tester.HistoryTester;
import pl.srw.billcalculator.testutils.HistoryGenerator;
import pl.srw.billcalculator.util.BillSelection;
import pl.srw.billcalculator.wrapper.Dependencies;

import static org.junit.Assert.assertTrue;
import static pl.srw.billcalculator.di.ApplicationModule.SHARED_PREFERENCES_FILE;

public class HistoryUITest {

    @Rule
    public ActivityTestRule<DrawerActivity> testRule = new ActivityTestRule<>(DrawerActivity.class, false, false);

    @Inject HistoryGenerator historyGenerator;

    @Inject BillSelection selection;

    private AppTester tester = new AppTester();

    @Before
    public void setUp() throws Exception {
        TestApplicationComponent component = DaggerTestApplicationComponent.builder()
                .applicationModule(Dependencies.getApplicationComponent().getApplicationModule())
                .build();
        Dependencies.setApplicationComponent(component);
        component.inject(this);
    }

    @After
    public void tearDown() throws Exception {
        selection.deselectAll();
        HistoryGenerator.clear();
    }

    @Test
    public void shouldShowCheckPricesDialogOnFirstStart() {
        // given: first start
        cleanFirstLaunch();

        // when: application start
        testRule.launchActivity(null);

        // then: check prices dialog show up
        tester.checkPricesDialogIsVisible();
    }

    @Test
    public void shouldRemoveBillWithPricesFromDb() {
        // given: one bill in history
        historyGenerator.generatePgeG11Bill(11);
        testRule.launchActivity(null);

        // when: deleting one bill
        HistoryTester historyTester = tester.skipCheckPricesDialogIfVisible()
                .onHistory()
                .changeItemSelectionWithReadings("1", "11")
                .deleteSelected();

        // then:
        historyTester.checkEmptyHistoryIsShown();

        // and no bill and prices in database is available
        assertTrue(Database.getSession().getPgeG11BillDao().loadAll().isEmpty());
        assertTrue(Database.getSession().getPgePricesDao().loadAll().isEmpty());
    }

    @Test
    public void shouldUnselectAfterDeletion() {
        // given: list contain 5 entries
        historyGenerator.generatePgeG11Bills(5);
        testRule.launchActivity(null);

        // when: select second entry and delete
        HistoryTester historyTester = tester.skipCheckPricesDialogIfVisible()
                .onHistory()
                .changeItemSelectionAtPosition(1)
                .deleteSelected()
                // and select second and third entry and delete
                .changeItemSelectionAtPosition(1)
                .changeItemSelectionAtPosition(2)
                .deleteSelected();

        // then:
        historyTester.checkNoSelection()
                .checkDeleteButtonHidden();
    }

    @Test
    public void shouldRestoreSelectionOnScreenRotation() throws InterruptedException {
        // given:
        historyGenerator.generatePgeG11Bills(3);
        testRule.launchActivity(null);

        // and one item is selected
        tester.skipCheckPricesDialogIfVisible()
                .onHistory()
                .changeItemSelectionAtPosition(1)
                .checkItemSelected(1);

        // when:
        tester.changeOrientation(testRule);

        // then: item is selected
        tester.onHistory()
                .checkItemSelected(1)
                .checkDeleteButtonShown()

        // when:
                .deleteSelected();
        tester.changeOrientation(testRule);

        // then:
        tester.onHistory()
                .checkNoSelection();
    }

    private void cleanFirstLaunch() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .edit().remove("first_launch").apply();
    }
}
