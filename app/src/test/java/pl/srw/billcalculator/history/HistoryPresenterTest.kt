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
    fun onFirstBind_setsHistoryDataOnList() {
        // GIVEN
        val list: LazyList<History> = mock()
        whenever(history.getAll()).thenReturn(list)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view).setListData(list)
        verify(view, never()).redrawList()
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
    fun onFirstBind_whenFirstLaunch_doesNotShowNewUIDialog() {
        // GIVEN
        whenever(settings.isFirstLaunch).thenReturn(true)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view, never()).showNewUIDialog()
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
    fun onFirstBind_whenNotFirstLaunch_andHelpWasNotShown_showsNewUIDialogAndMarkShown() {
        // GIVEN
        whenever(settings.isFirstLaunch).thenReturn(false)
        whenever(settings.wasHelpShown()).thenReturn(false)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view).showNewUIDialog()
        verify(settings).markHelpShown()
    }

    @Test
    fun onFirstBind_whenNotFirstLaunch_andHelpWasMarkShown_doesNotShowNewUIDialog() {
        // GIVEN
        whenever(settings.isFirstLaunch).thenReturn(false)
        whenever(settings.wasHelpShown()).thenReturn(true)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view, never()).showNewUIDialog()
    }

    @Test
    fun onFirstBind_fetchAllHistory() {
        // WHEN
        sut.onFirstBind()

        // THEN
        verify(history).getAll()
    }

    @Test
    fun onNewViewRestoreState_setsHistoryDataOnList() {
        // GIVEN
        val list: LazyList<History> = mock()
        sut.setState("historyData", list)

        // WHEN
        sut.onNewViewRestoreState()

        // THEN
        verify(view).setListData(list)
    }

    @Test
    fun onNewViewRestoreState_whenNeedRefresh_redrawsList() {
        // GIVEN
        val list: LazyList<History> = mock()
        whenever(history.getAll()).thenReturn(list)
        sut.setState("needRefresh", true)

        // WHEN
        sut.onNewViewRestoreState()

        // THEN
        verify(view).redrawList()
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
        val bill: PgeG11Bill = mock()

        // WHEN
        sut.onListItemDismissed(0, bill)

        // THEN
        verify(history).deleteBillWithPrices(bill)
    }

    @Test
    fun `on list item dismissed, cache item for undo posibility`() {
        // GIVEN
        val bill: PgeG11Bill = mock()

        // WHEN
        sut.onListItemDismissed(0, bill)

        // THEN
        verify(history).cacheBillForUndoDelete(bill)
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
    fun `undo delete clicked, add view items to list, in natural order`() {
        // GIVEN
        whenever(history.isUndoDeletePossible()).thenReturn(true)

        // WHEN
        sut.undoDeleteClicked(3, 2, 5)

        // THEN
        val inOrder = inOrder(view)
        inOrder.verify(view).setListData(any())
        inOrder.verify(view).onItemInsertedToList(2)
        inOrder.verify(view).onItemInsertedToList(3)
        inOrder.verify(view).onItemInsertedToList(5)
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
    fun `delete button clicked, caches bills for undo posibility`() {
        // GIVEN
        val items = listOf(mock<Bill>(), mock<Bill>())
        whenever(selection.items).thenReturn(items)
        whenever(selection.positionsReverseOrder).thenReturn(intArrayOf(1, 0))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(history).cacheBillsForUndoDelete(items)
    }

    @Test
    fun deleteClicked_deletesSelectedBills() {
        // GIVEN
        val bill: PgeG11Bill = mock()
        whenever(selection.items).thenReturn(Arrays.asList<Bill>(bill, bill))
        whenever(selection.positionsReverseOrder).thenReturn(intArrayOf(3, 2))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(history, times(2)).deleteBillWithPrices(bill)
    }

    @Test
    fun `delete button clicked, remove view items to list, in reverse order`() {
        // GIVEN
        whenever(selection.isAnySelected).thenReturn(true)
        whenever(selection.positionsReverseOrder).thenReturn(intArrayOf(5, 2))

        // WHEN
        sut.deleteClicked()

        // THEN
        val inOrder = inOrder(view)
        inOrder.verify(view).setListData(any())
        inOrder.verify(view).onItemRemoveFromList(5)
        inOrder.verify(view).onItemRemoveFromList(2)
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
    fun `delete button clicked, shows undo delete message`() {
        // GIVEN
        whenever(selection.positionsReverseOrder).thenReturn(intArrayOf(5, 3))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(view).showUndoDeleteMessage(5, 3)
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