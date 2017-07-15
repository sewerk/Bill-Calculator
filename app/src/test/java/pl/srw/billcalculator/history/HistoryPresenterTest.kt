package pl.srw.billcalculator.history

import android.view.View
import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.greenrobot.greendao.query.LazyList
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import pl.srw.billcalculator.bill.SavedBillsRegistry
import pl.srw.billcalculator.db.Bill
import pl.srw.billcalculator.db.History
import pl.srw.billcalculator.db.PgeG11Bill
import pl.srw.billcalculator.history.list.item.HistoryViewItem
import pl.srw.billcalculator.persistence.type.BillType
import pl.srw.billcalculator.setState
import pl.srw.billcalculator.settings.global.SettingsRepo
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.BillSelection
import pl.srw.billcalculator.wrapper.HistoryRepo
import java.util.*

@RunWith(JUnitParamsRunner::class)
class HistoryPresenterTest {

    val listData: LazyList<History> = mock()
    val view: HistoryPresenter.HistoryView = mock()
    val settings: SettingsRepo = mock()
    val history: HistoryRepo = mock {
        on { getAll() } doReturn listData
    }
    val savedBillsRegistry: SavedBillsRegistry = mock()
    val selection: BillSelection = mock()

    val sut = HistoryPresenter(settings, history, savedBillsRegistry, selection)

    @Before
    fun setUp() {
        sut.setState("view", view)
    }

    @Test
    fun onFirstBind_whenFirstLaunch_showsWelcomeDialog() {
        // GIVEN
        whenever(settings.isFirstLaunch).thenReturn(true)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view).showWelcomeDialog()
    }

    @Test
    fun onFirstBind_whenFirstLaunch_doesNotShowHelp() {
        // GIVEN
        whenever(settings.isFirstLaunch).thenReturn(true)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view, never()).showHelp()
    }

    @Test
    fun onFirstBind_whenFirstLaunch_marksHelpShown() {
        // GIVEN
        whenever(settings.isFirstLaunch).thenReturn(true)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(settings).markHelpShown()
    }

    @Test
    fun onFirstBind_whenNotFirstLaunch_dontShowsWelcomeDialog() {
        // GIVEN
        whenever(settings.isFirstLaunch).thenReturn(false)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view, never()).showWelcomeDialog()
    }

    @Test
    fun onFirstBind_whenNotFirstLaunch_andHelpWasNotShown_showsHelpAndMarkShown() {
        // GIVEN
        whenever(settings.isFirstLaunch).thenReturn(false)
        whenever(settings.wasHelpShown()).thenReturn(false)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view).showHelp()
        verify(settings).markHelpShown()
    }

    @Test
    fun onFirstBind_whenNotFirstLaunch_andFirstLaunchOnFreshInstallation_neverShowHelp() {
        // GIVEN
        whenever(settings.isFirstLaunch).thenReturn(true, false)
        sut.onFirstBind()
        verify(settings).markHelpShown()

        // WHEN
        whenever(settings.wasHelpShown()).thenReturn(true)
        sut.onFirstBind()

        // THEN
        verify(view, never()).showHelp()
    }

    @Test
    fun onFirstBind_whenNotFirstLaunch_andHelpWasShown_doesNotShowHelp() {
        // GIVEN
        whenever(settings.isFirstLaunch).thenReturn(false)
        whenever(settings.wasHelpShown()).thenReturn(true)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view, never()).showHelp()
    }

    @Test
    fun onFirstBind_fetchAllHistory() {
        // WHEN
        sut.onFirstBind()

        // THEN
        verify(history).getAll()
    }

    @Test
    fun onNewViewRestoreState_whenNeedRefresh_closesOldListAndFetchNewOne() {
        // GIVEN
        val list: LazyList<History> = mock()
        sut.setState("historyData", list)
        sut.setState("needRefresh", true)

        // WHEN
        sut.onNewViewRestoreState()

        // THEN
        verify(list).close()
        verify(history).getAll()
    }

    @Test
    fun onNewViewRestoreState_whenNeedRefresh_setsHistoryDataOnList() {
        // GIVEN
        val list: LazyList<History> = mock()
        whenever(history.getAll()).thenReturn(list)
        sut.setState("needRefresh", true)

        // WHEN
        sut.onNewViewRestoreState()

        // THEN
        verify(view).setListData(list)
        verify(view).redrawList()
    }

    @Test
    fun onCreate_setsHistoryDataOnList() {
        // GIVEN
        val list: LazyList<History> = mock()
        sut.setState("historyData", list)

        // WHEN
        sut.onCreate()

        // THEN
        verify(view).setListData(list)
        verify(view, never()).redrawList()
    }

    @Test
    fun helpMenuClicked_showsHelp() {
        // WHEN
        sut.helpMenuClicked()

        // THEN
        verify(view).showHelp()
    }

    @Test
    fun settingsClicked_opensSettings() {
        // WHEN
        sut.settingsClicked()

        // THEN
        verify(view).openSettings()
    }

    @Test
    fun settingsClicked_closesDrawer() {
        // WHEN
        sut.settingsClicked()

        // THEN
        verify(view).closeDrawer()
    }

    @Test
    fun handleBackPressed_whenDrawerOpened_closesDrawer() {
        // GIVEN
        whenever(view.isDrawerOpen).thenReturn(true)

        // WHEN
        sut.handleBackPressed()

        // THEN
        verify(view).closeDrawer()
    }

    @Test
    fun handleBackPressed_whenDrawerOpened_returnsTrue() {
        // GIVEN
        whenever(view.isDrawerOpen).thenReturn(true)

        // WHEN
        val result = sut.handleBackPressed()

        // THEN
        assertTrue(result)
    }

    @Test
    fun handleBackPressed_whenDrawerClosed_returnsFalse() {
        // GIVEN
        whenever(view.isDrawerOpen).thenReturn(false)

        // WHEN
        val result = sut.handleBackPressed()

        // THEN
        assertFalse(result)
    }

    @Test
    fun historyClicked_closesDrawer() {
        // WHEN
        sut.historyClicked()

        // THEN
        verify(view).closeDrawer()
    }

    @Test
    @Parameters("PGE", "PGNIG", "TAURON")
    fun newBillClicked_showsForm(provider: Provider) {
        // WHEN
        sut.newBillClicked(provider)

        // THEN
        verify(view).showNewBillForm(provider)
    }

    @Test
    fun aboutClicked_showsAbout() {
        // WHEN
        sut.aboutClicked()

        // THEN
        verify(view).showAbout()
    }

    @Test
    fun aboutClicked_closesDrawer() {
        // WHEN
        sut.aboutClicked()

        // THEN
        verify(view).closeDrawer()
    }

    @Test
    fun onListItemDismissed_deleteBillWithPrices() {
        // GIVEN
        val id = 1L
        val pricesId = 2L
        val bill: PgeG11Bill = mock {
            on { getId() } doReturn id
            on { getPricesId() } doReturn pricesId
        }

        // WHEN
        sut.onListItemDismissed(0, bill)

        // THEN
        verify(history).deleteBillWithPrices(BillType.PGE_G11, id, pricesId)
    }

    @Test
    fun `on list item dismissed, cache item for undo posibility`() {
        // GIVEN
        val id = 1L
        val pricesId = 2L
        val bill: PgeG11Bill = mock {
            on { getId() } doReturn id
            on { getPricesId() } doReturn pricesId
        }

        // WHEN
        sut.onListItemDismissed(0, bill)

        // THEN
        verify(history).cacheBillForUndoDelete(BillType.PGE_G11, id, pricesId)
    }

    @Test
    fun onListItemDismissed_fetchAllHistory() {
        // GIVEN
        val bill: PgeG11Bill = mock()

        // WHEN
        sut.onListItemDismissed(0, bill)

        // THEN
        verify(history).getAll()
    }

    @Test
    fun onListItemDismissed_viewRemovesItemFromList() {
        // GIVEN
        val position = 1
        val bill: PgeG11Bill = mock()

        // WHEN
        sut.onListItemDismissed(position, bill)

        // THEN
        verify(view).setListData(any())
        verify(view).onItemRemoveFromList(eq(position))
    }

    @Test
    fun onListItemDismissed_showsUndoDeleteMessage() {
        // GIVEN
        val bill: PgeG11Bill = mock()
        val position = 0

        // WHEN
        sut.onListItemDismissed(position, bill)

        // THEN
        verify(view).showUndoDeleteMessage(position)
    }

    @Test
    fun undoDeleteClicked_viewAddsItemToList() {
        // GIVEN
        whenever(history.isUndoDeletePossible()).thenReturn(true)

        // WHEN
        val position = 0
        sut.undoDeleteClicked(position)

        // THEN
        verify(view).setListData(any())
        verify(view).onItemInsertedToList(eq(position))
    }

    @Test
    fun undoDeleteClicked_undosDeleteHistory() {
        // GIVEN
        whenever(history.isUndoDeletePossible()).thenReturn(true)

        // WHEN
        sut.undoDeleteClicked(0)

        // THEN
        verify(history).undoDelete()
    }

    @Test
    fun undoDeleteClicked_whenAlreadyUndo_doesNotUndoAnyMore() {
        // GIVEN
        whenever(history.isUndoDeletePossible()).thenReturn(false)

        // WHEN
        sut.undoDeleteClicked(0)

        // THEN
        verify(history).isUndoDeletePossible()
        verify(history, never()).undoDelete()
    }

    @Test
    fun undoDeleteClicked_fetchAllHistory() {
        // GIVEN
        whenever(history.isUndoDeletePossible()).thenReturn(true)

        // WHEN
        sut.undoDeleteClicked(0)

        // THEN
        verify(history).getAll()
    }

    @Test
    fun onListItemClicked_whenNotInSelectMode_opensBill() {
        // GIVEN
        val bill: PgeG11Bill = mock()
        val itemView: View = mock()
        val item = given_historyViewItem(bill, itemView)

        // WHEN
        sut.onListItemClicked(item)

        // THEN
        verify(view).openBill(bill, itemView)
    }

    @Test
    fun onListItemClicked_whenNotInSelectMode_registerSavedBill() {
        // GIVEN
        val bill: PgeG11Bill = mock()
        val item = given_historyViewItem(bill)

        // WHEN
        sut.onListItemClicked(item)

        // THEN
        verify(savedBillsRegistry).register(bill)
    }

    @Test
    fun onListItemClicked_andItemSelected_deselectsViewItem() {
        // GIVEN
        val position = 2
        whenever(selection.isAnySelected).thenReturn(true)
        whenever(selection.isSelected(position)).thenReturn(true)
        val item = given_historyViewItem(position = position)

        // WHEN
        sut.onListItemClicked(item)

        // THEN
        verify(item).deselect()
    }

    @Test
    fun onListItemClicked_andItemSelected_deselectPosition() {
        // GIVEN
        val position = 2
        whenever(selection.isAnySelected).thenReturn(true)
        whenever(selection.isSelected(position)).thenReturn(true)
        val item = given_historyViewItem(position = position)

        // WHEN
        sut.onListItemClicked(item)

        // THEN
        verify(selection).deselect(position)
    }

    @Test
    fun onListItemClicked_whenInSelectMode_andItemNotSelected_selectsViewItem() {
        // GIVEN
        whenever(selection.isAnySelected).thenReturn(true)
        val position = 2
        val item = given_historyViewItem(position = position)

        // WHEN
        sut.onListItemClicked(item)

        // THEN
        verify(item).select()
    }

    @Test
    fun onListItemClicked_whenInSelectMode_andItemNotSelected_selectsPosition() {
        // GIVEN
        whenever(selection.isAnySelected).thenReturn(true)
        val position = 2
        val bill: PgeG11Bill = mock()
        val item = given_historyViewItem(bill, mock<View>(), position)

        // WHEN
        sut.onListItemClicked(item)

        // THEN
        verify(selection).select(position, bill)
    }

    @Test
    fun onListItemClicked_onSingleSelectedItem_hidesDeleteButton() {
        // GIVEN
        whenever(selection.isAnySelected).thenReturn(true, false)
        val position = 2
        whenever(selection.isSelected(position)).thenReturn(true)
        val item = given_historyViewItem(position = position)

        // WHEN
        sut.onListItemClicked(item)

        // THEN
        verify(view).hideDeleteButton()
    }

    @Test
    fun onListItemClicked_onSingleSelectedItem_enablesSwipeDelete() {
        // GIVEN
        whenever(selection.isAnySelected).thenReturn(true, false)
        val position = 2
        whenever(selection.isSelected(position)).thenReturn(true)
        val item = given_historyViewItem(position = position)

        // WHEN
        sut.onListItemClicked(item)

        // THEN
        verify(view).enableSwipeDelete()
    }

    @Test
    fun onListItemLongClicked_whenNotInSelectMode_showsDeleteButton() {
        // GIVEN
        val item = given_historyViewItem(position = 2)

        // WHEN
        sut.onListItemLongClicked(item)

        // THEN
        verify(view).showDeleteButton()
    }

    @Test
    fun onListItemLongClicked_whenNotInSelectMode_disablesSwipeDelete() {
        // GIVEN
        val item = given_historyViewItem(position = 2)

        // WHEN
        sut.onListItemLongClicked(item)

        // THEN
        verify(view).disableSwipeDelete()
    }

    @Test
    fun onListItemLongClicked_whenNotInSelectMode_selectsViewItem() {
        // GIVEN
        val item = given_historyViewItem(position = 2)

        // WHEN
        sut.onListItemLongClicked(item)

        // THEN
        verify(item).select()
    }

    @Test
    fun onListItemLongClicked_andItemSelected_deselectsViewItem() {
        // GIVEN
        val position = 2
        whenever(selection.isAnySelected).thenReturn(true)
        whenever(selection.isSelected(position)).thenReturn(true)
        val item = given_historyViewItem(position = position)

        // WHEN
        sut.onListItemLongClicked(item)

        // THEN
        verify(item).deselect()
    }

    @Test
    fun onListItemLongClicked_andItemSelected_deselectsPosition() {
        // GIVEN
        val position = 2
        whenever(selection.isAnySelected).thenReturn(true)
        whenever(selection.isSelected(position)).thenReturn(true)
        val item = given_historyViewItem(position = position)

        // WHEN
        sut.onListItemLongClicked(item)

        // THEN
        verify(selection).deselect(position)
    }

    @Test
    fun onListItemLongClicked_whenInSelectMode_andItemNotSelected_selectsViewItem() {
        // GIVEN
        whenever(selection.isAnySelected).thenReturn(true)
        val item = given_historyViewItem(position = 2)

        // WHEN
        sut.onListItemLongClicked(item)

        // THEN
        verify(item).select()
    }

    @Test
    fun onListItemLongClicked_whenItemNotSelected_selectsPosition() {
        // GIVEN
        val position = 2
        val bill: Bill = mock()
        val item = given_historyViewItem(bill, mock<View>(), position)

        // WHEN
        sut.onListItemLongClicked(item)

        // THEN
        verify(selection).select(position, bill)
    }

    @Test
    fun onListItemLongClicked_onSingleSelectedItem_hidesDeleteButton() {
        // GIVEN
        val position = 2
        whenever(selection.isAnySelected).thenReturn(true, false)
        whenever(selection.isSelected(position)).thenReturn(true)
        val item = given_historyViewItem(position = position)

        // WHEN
        sut.onListItemLongClicked(item)

        // THEN
        verify(view).hideDeleteButton()
    }

    @Test
    fun onListItemLongClicked_onSingleSelectedItem_enablesSwipeDelete() {
        // GIVEN
        val position = 2
        whenever(selection.isAnySelected).thenReturn(true, false)
        whenever(selection.isSelected(position)).thenReturn(true)
        val item = given_historyViewItem(position = position)

        // WHEN
        sut.onListItemLongClicked(item)

        // THEN
        verify(view).enableSwipeDelete()
    }

    @Test
    fun deleteClicked_deletesSelectedBill() {
        // GIVEN
        val id = 1L
        val pricesId = 2L
        val bill: PgeG11Bill = mock {
            on { getId() } doReturn id
            on { getPricesId() } doReturn pricesId
        }

        whenever(selection.isAnySelected).thenReturn(true)
        whenever(selection.items).thenReturn(Arrays.asList<Bill>(bill, bill))
        whenever(selection.positionsReverseOrder).thenReturn(intArrayOf(3, 2))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(history, times(2)).deleteBillWithPrices(BillType.PGE_G11, id, pricesId)
    }

    @Test
    fun deleteClicked_removesViewsFromList() {
        // GIVEN
        val position1 = 2
        val position2 = 5
        whenever(selection.isAnySelected).thenReturn(true)
        whenever(selection.positionsReverseOrder).thenReturn(intArrayOf(position2, position1))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(view).setListData(any())
        verify(view).onItemRemoveFromList(eq(position2))
        verify(view).onItemRemoveFromList(eq(position1))
    }

    @Test
    fun `delete button clicked, enables swipe on items`() {
        // GIVEN
        whenever(selection.positionsReverseOrder).thenReturn(intArrayOf(1))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(view).enableSwipeDelete()
    }

    @Test
    fun deleteClicked_clearsSelection() {
        // GIVEN
        whenever(selection.isAnySelected).thenReturn(true)
        whenever(selection.positionsReverseOrder).thenReturn(IntArray(0))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(selection).deselectAll()
    }

    @Test
    fun deleteClicked_hidesDeleteButton() {
        // GIVEN
        whenever(selection.isAnySelected).thenReturn(true)
        whenever(selection.positionsReverseOrder).thenReturn(IntArray(0))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(view).hideDeleteButton()
    }

    private fun given_historyViewItem(bill: Bill = mock<PgeG11Bill>(), itemView: View = mock<View>(), position: Int = 0): HistoryViewItem {
        val item = mock<HistoryViewItem>()
        whenever(item.bill).thenReturn(bill)
        whenever(item.view).thenReturn(itemView)
        whenever(item.positionOnList).thenReturn(position)
        return item
    }
}