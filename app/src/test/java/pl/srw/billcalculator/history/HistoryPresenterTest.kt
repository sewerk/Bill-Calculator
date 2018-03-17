package pl.srw.billcalculator.history

import android.view.View
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import junitparams.JUnitParamsRunner
import org.greenrobot.greendao.query.LazyList
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import pl.srw.billcalculator.bill.SavedBillsRegistry
import pl.srw.billcalculator.data.ApplicationRepo
import pl.srw.billcalculator.data.bill.HistoryRepo
import pl.srw.billcalculator.db.Bill
import pl.srw.billcalculator.db.History
import pl.srw.billcalculator.db.PgeG11Bill
import pl.srw.billcalculator.history.list.item.HistoryViewItem
import pl.srw.billcalculator.setState
import pl.srw.billcalculator.util.BillSelection
import java.util.Arrays

@RunWith(JUnitParamsRunner::class)
class HistoryPresenterTest {

    val listData: LazyList<History> = mock()
    val view: HistoryPresenter.HistoryView = mock()
    val applicationRepo: ApplicationRepo = mock()
    val history: HistoryRepo = mock {
        on { getAll() } doReturn listData
    }
    val savedBillsRegistry: SavedBillsRegistry = mock()
    val selection: BillSelection = mock()

    val sut = HistoryPresenter(applicationRepo, history, savedBillsRegistry, selection)

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
        whenever(applicationRepo.isFirstLaunch).thenReturn(true)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view).showWelcomeDialog()
    }

    @Test
    fun onFirstBind_whenFirstLaunch_doesNotShowNewUIDialog() {
        // GIVEN
        whenever(applicationRepo.isFirstLaunch).thenReturn(true)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view, never()).showNewUIDialog()
    }

    @Test
    fun onFirstBind_whenFirstLaunch_marksHelpShown() {
        // GIVEN
        whenever(applicationRepo.isFirstLaunch).thenReturn(true)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(applicationRepo).markHelpShown()
    }

    @Test
    fun onFirstBind_whenNotFirstLaunch_dontShowsWelcomeDialog() {
        // GIVEN
        whenever(applicationRepo.isFirstLaunch).thenReturn(false)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view, never()).showWelcomeDialog()
    }

    @Test
    fun onFirstBind_whenNotFirstLaunch_andHelpWasNotShown_showsNewUIDialogAndMarkShown() {
        // GIVEN
        whenever(applicationRepo.isFirstLaunch).thenReturn(false)
        whenever(applicationRepo.wasHelpShown()).thenReturn(false)

        // WHEN
        sut.onFirstBind()

        // THEN
        verify(view).showNewUIDialog()
        verify(applicationRepo).markHelpShown()
    }

    @Test
    fun onFirstBind_whenNotFirstLaunch_andHelpWasMarkShown_doesNotShowNewUIDialog() {
        // GIVEN
        whenever(applicationRepo.isFirstLaunch).thenReturn(false)
        whenever(applicationRepo.wasHelpShown()).thenReturn(true)

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
    fun `sets list data, on new restore state`() {
        // GIVEN
        val list: LazyList<History> = mock()
        sut.setState("historyData", list)

        // WHEN
        sut.onNewViewRestoreState()

        // THEN
        verify(view).setListData(list)
    }

    @Test
    fun `redraw list, on new view restore state, when history changed`() {
        // GIVEN
        val list: LazyList<History> = mock()
        whenever(history.getAll()).thenReturn(list)
        sut.onHistoryChanged()

        // WHEN
        sut.onNewViewRestoreState()

        // THEN
        verify(view).redrawList()
    }

    @Test
    fun `closes old and featches new list, on new view restore state, when history changed`() {
        // GIVEN
        val list: LazyList<History> = mock()
        sut.setState("historyData", list)
        sut.onHistoryChanged()

        // WHEN
        sut.onNewViewRestoreState()

        // THEN
        verify(list).close()
        verify(history).getAll()
    }

    @Test
    fun `disables swipe delete, on new view restore state, when any item selected`() {
        whenever(selection.isAnySelected()).thenReturn(true)

        sut.onNewViewRestoreState()

        verify(view).disableSwipeDelete()
    }

    @Test
    fun `unselects bills, when history changes`() {
        sut.onHistoryChanged()

        verify(selection).deselectAll()
    }

    @Test
    fun `hides delete button, when history changes`() {
        sut.onHistoryChanged()

        verify(view).hideDeleteButton()
    }

    @Test
    fun `enables swipe delete, when history changes`() {
        sut.onHistoryChanged()

        verify(view).enableSwipeDelete()
    }

    @Test
    fun helpMenuClicked_showsHelp() {
        // WHEN
        sut.helpMenuClicked()

        // THEN
        verify(view).showHelp()
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
    fun `update selection, when undo clicked`() {
        whenever(history.isUndoDeletePossible()).thenReturn(true)
        val lowestPosition = 1

        sut.undoDeleteClicked(4, lowestPosition, 2)

        verify(selection).onInsert(lowestPosition)
    }

    @Test
    fun `updates selection, after undo clicked, before list data changed`() {
        whenever(history.isUndoDeletePossible()).thenReturn(true)
        val lowestPosition = 1

        sut.undoDeleteClicked(lowestPosition)

        val inOrder = inOrder(selection, view)
        inOrder.verify(selection).onInsert(lowestPosition)
        inOrder.verify(view).setListData(any())
        inOrder.verify(view).onItemInsertedToList(lowestPosition)
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
        whenever(selection.isAnySelected()).thenReturn(true)
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
        whenever(selection.isAnySelected()).thenReturn(true)
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
        whenever(selection.isAnySelected()).thenReturn(true)
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
        whenever(selection.isAnySelected()).thenReturn(true)
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
        whenever(selection.isAnySelected()).thenReturn(true, false)
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
        whenever(selection.isAnySelected()).thenReturn(true, false)
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
        whenever(selection.isAnySelected()).thenReturn(true)
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
        whenever(selection.isAnySelected()).thenReturn(true)
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
        whenever(selection.isAnySelected()).thenReturn(true)
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
        whenever(selection.isAnySelected()).thenReturn(true, false)
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
        whenever(selection.isAnySelected()).thenReturn(true, false)
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
        whenever(selection.getItems()).thenReturn(items)
        whenever(selection.getPositionsReverseOrder()).thenReturn(intArrayOf(1, 0))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(history).cacheBillsForUndoDelete(items)
    }

    @Test
    fun deleteClicked_deletesSelectedBills() {
        // GIVEN
        val bill: PgeG11Bill = mock()
        val bills = Arrays.asList<Bill>(bill, bill)
        whenever(selection.getItems()).thenReturn(bills)
        whenever(selection.getPositionsReverseOrder()).thenReturn(intArrayOf(3, 2))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(history).deleteBillsWithPrices(bills)
    }

    @Test
    fun `delete button clicked, remove view items to list, in reverse order`() {
        // GIVEN
        whenever(selection.isAnySelected()).thenReturn(true)
        whenever(selection.getPositionsReverseOrder()).thenReturn(intArrayOf(5, 2))

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
        whenever(selection.getPositionsReverseOrder()).thenReturn(intArrayOf(1))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(view).enableSwipeDelete()
    }

    @Test
    fun `delete button clicked, shows undo delete message`() {
        // GIVEN
        whenever(selection.getPositionsReverseOrder()).thenReturn(intArrayOf(5, 3))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(view).showUndoDeleteMessage(5, 3)
    }

    @Test
    fun deleteClicked_clearsSelection() {
        // GIVEN
        whenever(selection.isAnySelected()).thenReturn(true)
        whenever(selection.getPositionsReverseOrder()).thenReturn(IntArray(0))

        // WHEN
        sut.deleteClicked()

        // THEN
        verify(selection).deselectAll()
    }

    @Test
    fun deleteClicked_hidesDeleteButton() {
        // GIVEN
        whenever(selection.isAnySelected()).thenReturn(true)
        whenever(selection.getPositionsReverseOrder()).thenReturn(IntArray(0))

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
