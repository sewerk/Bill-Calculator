package pl.srw.billcalculator.history

import android.content.Context
import android.support.test.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.srw.billcalculator.di.ApplicationModule.SHARED_PREFERENCES_FILE
import pl.srw.billcalculator.di.TestDependencies
import pl.srw.billcalculator.persistence.Database
import pl.srw.billcalculator.tester.AppTester
import pl.srw.billcalculator.tester.rule.ClosingActivityTestRule
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.BillSelection
import javax.inject.Inject

class HistoryUITest {

    @Rule @JvmField
    var testRule = ClosingActivityTestRule(DrawerActivity::class.java, false, false)

    @Inject lateinit var historyGenerator: HistoryGenerator
    @Inject lateinit var selection: BillSelection

    val tester = AppTester()

    @Before
    fun setUp() {
        TestDependencies.inject(this)
        HistoryGenerator.clear()
    }

    @After
    fun tearDown() {
        selection.deselectAll()
        HistoryGenerator.clear()
    }

    @Test
    fun shouldShowCheckPricesDialogOnFirstStart() {
        // given: first start
        cleanFirstLaunch()

        // when: application start
        testRule.launchActivity(null)

        // then:
        tester.checkPricesDialogIsVisible()
    }

    @Test
    fun shouldRemoveBillWithPricesFromDb() {
        // given: one bill in history
        historyGenerator.generatePgeG11Bill(11)
        testRule.launchActivity(null)

        // when: deleting one bill
        val historyTester = tester.skipCheckPricesDialogIfVisible()
                .onHistory()
                .changeItemSelectionWithReadings("1", "11")
                .deleteSelected()

        // then:
        historyTester.checkEmptyHistoryIsShown()

        // and no bill and prices in database is available
        assertTrue(Database.getSession().pgeG11BillDao.loadAll().isEmpty())
        assertTrue(Database.getSession().pgePricesDao.loadAll().isEmpty())
    }

    @Test
    fun shouldShowUndoMessageAfterSwipeDelete() {
        // given: one bill in history
        historyGenerator.generatePgeG11Bill(11)
        testRule.launchActivity(null)

        // when: deleting one bill
        val historyTester = tester.skipCheckPricesDialogIfVisible()
                .onHistory()
                .deleteBillWithReadings("1", "11")

        // then:
        historyTester
                .checkUndoMessageIsShown()
                .checkEmptyHistoryIsShown()
    }

    @Test
    fun shouldShowUndoMessageAfterSelectDelete() {
        // given: one bill in history
        historyGenerator.generatePgeG11Bills(5)
        testRule.launchActivity(null)

        // when: deleting one bill
        val historyTester = tester.skipCheckPricesDialogIfVisible()
                .onHistory()
                .changeItemSelectionAtPosition(1)
                .changeItemSelectionAtPosition(3)
                .deleteSelected()

        // then:
        historyTester
                .checkUndoMessageIsShown()
    }

    @Test
    fun shouldRestoreBillAfterSwipeDeleteWhenUndoActionClicked() {
        // given: one bill in history
        historyGenerator.generatePgeG11Bill(11)
        testRule.launchActivity(null)

        // when: deleting one bill
        val historyTester = tester.skipCheckPricesDialogIfVisible()
                .onHistory()
                .deleteBillWithReadings("1", "11")
                .undoDelete()

        // then:
        historyTester.checkEmptyHistoryIsNotShown()
                .openBillWithReadings("1", "11")
    }

    @Test
    fun shouldRestoreBillAfterSelectDeleteWhenUndoActionClicked() {
        // given: one bill in history
        historyGenerator.generatePgeG11Bill(11)
        historyGenerator.generatePgeG11Bill(22)
        testRule.launchActivity(null)

        // when: deleting one bill
        val historyTester = tester.skipCheckPricesDialogIfVisible()
                .onHistory()
                .changeItemSelectionWithReadings("1", "11")
                .deleteSelected()
                .undoDelete()

        // then:
        historyTester.checkEmptyHistoryIsNotShown()
                .openBillWithReadings("1", "11")
    }

    @Test
    fun shouldUnselectAfterDeletion() {
        // given: list contain 5 entries
        historyGenerator.generatePgeG11Bills(5)
        testRule.launchActivity(null)

        // when: select second entry and delete
        val historyTester = tester.skipCheckPricesDialogIfVisible()
                .onHistory()
                .changeItemSelectionAtPosition(1)
                .deleteSelected()
                // and select second and third entry and delete
                .changeItemSelectionAtPosition(1)
                .changeItemSelectionAtPosition(2)
                .deleteSelected()

        // then:
        historyTester.checkNoSelection()
                .checkDeleteButtonHidden()
    }

    @Test
    fun shouldRestoreSelectionOnScreenRotation() {
        // given:
        historyGenerator.generatePgeG11Bills(3)
        testRule.launchActivity(null)

        // and one item is selected
        tester.skipCheckPricesDialogIfVisible()
                .onHistory()
                .changeItemSelectionAtPosition(1)
                .checkItemSelected(1)

        // when:
        tester.changeOrientation(testRule)

        // then: item is selected
        tester.onHistory()
                .checkItemSelected(1)
                .checkDeleteButtonShown()

                // when:
                .deleteSelected()
        tester.changeOrientation(testRule)

        // then:
        tester.onHistory()
                .checkNoSelection()
    }

    @Test
    fun shouldUnselectWhenNewBillCalculated() {
        // given:
        historyGenerator.generatePgeG11Bills(3)
        testRule.launchActivity(null)

        // and one item is selected
        val historyTester = tester.skipCheckPricesDialogIfVisible()
                .onHistory()
                .changeItemSelectionAtPosition(0)
                .changeItemSelectionAtPosition(1)

        // when:
        tester.openForm(Provider.PGNIG)
                .putIntoReadingFrom("12")
                .putIntoReadingTo("23")
                .calculate()

        // then:
        historyTester.checkNoSelection()
                .checkDeleteButtonHidden()
    }

    @Test
    fun shouldDisplayProperReadingsAfterScrolling() {
        val count = 10
        // given: one double-reading bill and more single-reading
        historyGenerator.generatePgeG12Bill(101, 201)
        historyGenerator.generatePgeG11Bills(count)
        testRule.launchActivity(null)

        // when:
        val historyTester = tester
                .skipCheckPricesDialogIfVisible()
                .onHistory()

        // then:
        historyTester.checkItemReadings(0, "91 - 101", "191 - 201")
        // and: double reading should be empty for position > 0
        for (i in count downTo 1) {
            val position = count - i + 1
            val firstLine = i.toString() + " - " + (i + 10)
            historyTester.checkItemReadings(position, firstLine, "")
        }
    }

    @Test
    fun shouldOpenAndCloseBillAfterScrolling() {
        // given:
        historyGenerator.generatePgeG11Bills(20)
        testRule.launchActivity(null)

        // when:
        tester
                .skipCheckPricesDialogIfVisible()
                .onHistory()
                .openBillAtPosition(16)
                .close()
                .onHistory()
                .openBillAtPosition(1)
                .close()

        // then: no crash
    }

    @Test
    fun shouldGoThroughHelpWithoutProblem() {
        testRule.launchActivity(null)

        tester.skipCheckPricesDialogIfVisible()
                .clickHelp()
                .clickInCenter()
                .clickInCenter()
                .clickInCenter()
                .clickInCenter()
    }

    private fun cleanFirstLaunch() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .edit().remove("first_launch").apply()
    }
}
