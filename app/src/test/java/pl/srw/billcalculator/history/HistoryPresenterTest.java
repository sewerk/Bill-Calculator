package pl.srw.billcalculator.history;

import android.view.View;

import org.greenrobot.greendao.query.LazyList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import pl.srw.billcalculator.Whitebox;
import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.history.list.item.HistoryViewItem;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.settings.global.SettingsRepo;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.BillSelection;
import pl.srw.billcalculator.wrapper.HistoryRepo;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class HistoryPresenterTest {

    @InjectMocks private HistoryPresenter sut;
    
    @Mock HistoryPresenter.HistoryView view;
    @Mock SettingsRepo settings;
    @Mock HistoryRepo history;
    @Mock SavedBillsRegistry savedBillsRegistry;
    @Mock LazyList<History> listData;
    @Mock BillSelection selection;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(sut, "view", view);
        when(history.getAll()).thenReturn(listData);
    }

    @Test
    public void onFirstBind_whenFirstLaunch_showsWelcomeDialog() throws Exception {
        // GIVEN
        when(settings.isFirstLaunch()).thenReturn(true);

        // WHEN
        sut.onFirstBind();

        // THEN
        verify(view).showWelcomeDialog();
    }

    @Test
    public void onFirstBind_whenNotFirstLaunch_dontShowsWelcomeDialog() throws Exception {
        // GIVEN
        when(settings.isFirstLaunch()).thenReturn(false);

        // WHEN
        sut.onFirstBind();

        // THEN
        verify(view, never()).showWelcomeDialog();
    }

    @Test
    public void onFirstBind_fetchAllHistory() throws Exception {
        // WHEN
        sut.onFirstBind();

        // THEN
        verify(history).getAll();
    }

    @Test
    public void onFirstBind_setsHistoryDataOnList() throws Exception {
        // WHEN
        sut.onFirstBind();

        // THEN
        verify(view).setListData(listData);
        verify(view).redrawList();
    }

    @Test
    public void onNewViewRestoreState_whenNeedRefresh_fetchAllHistory() throws Exception {
        // GIVEN
        Whitebox.setInternalState(sut, "needRefresh", true);

        // WHEN
        sut.onNewViewRestoreState();

        // THEN
        verify(history).getAll();
    }

    @Test
    public void onNewViewRestoreState_setsHistoryDataOnList() throws Exception {
        // GIVEN
        final LazyList<History> list = mock(LazyList.class);
        Whitebox.setInternalState(sut, "historyData", list);

        // WHEN
        sut.onNewViewRestoreState();

        // THEN
        verify(view).setListData(list);
        verify(view).redrawList();
    }

    @Test
    public void helpMenuClicked_showsHelp() throws Exception {
        // WHEN
        sut.helpMenuClicked();

        // THEN
        verify(view).showHelp();
    }

    @Test
    public void settingsClicked_opensSettings() throws Exception {
        // WHEN
        sut.settingsClicked();

        // THEN
        verify(view).openSettings();
    }

    @Test
    public void settingsClicked_closesDrawer() throws Exception {
        // WHEN
        sut.settingsClicked();

        // THEN
        verify(view).closeDrawer();
    }

    @Test
    public void handleBackPressed_whenDrawerOpened_closesDrawer() throws Exception {
        // GIVEN
        when(view.isDrawerOpen()).thenReturn(true);

        // WHEN
        sut.handleBackPressed();

        // THEN
        verify(view).closeDrawer();
    }

    @Test
    public void handleBackPressed_whenDrawerOpened_returnsTrue() throws Exception {
        // GIVEN
        when(view.isDrawerOpen()).thenReturn(true);

        // WHEN
        boolean result = sut.handleBackPressed();

        // THEN
        assertTrue(result);
    }

    @Test
    public void handleBackPressed_whenDrawerClosed_returnsFalse() throws Exception {
        // GIVEN
        when(view.isDrawerOpen()).thenReturn(false);

        // WHEN
        boolean result = sut.handleBackPressed();

        // THEN
        assertFalse(result);
    }

    @Test
    public void historyClicked_closesDrawer() throws Exception {
        // WHEN
        sut.historyClicked();

        // THEN
        verify(view).closeDrawer();
    }

    @Test
    @Parameters({"PGE", "PGNIG", "TAURON"})
    public void newBillClicked_showsForm(Provider provider) throws Exception {
        // WHEN
        sut.newBillClicked(provider);

        // THEN
        verify(view).showNewBillForm(provider);
    }

    @Test
    public void aboutClicked_showsAbout() throws Exception {
        // WHEN
        sut.aboutClicked();

        // THEN
        verify(view).showAbout();
    }

    @Test
    public void aboutClicked_closesDrawer() throws Exception {
        // WHEN
        sut.aboutClicked();

        // THEN
        verify(view).closeDrawer();
    }

    @Test
    public void onListItemDismissed_deleteBillWithPrices() throws Exception {
        // GIVEN
        Bill bill = mock(PgeG11Bill.class);
        final Long id = 1L;
        when(bill.getId()).thenReturn(id);
        final Long pricesId = 2L;
        when(bill.getPricesId()).thenReturn(pricesId);

        // WHEN
        sut.onListItemDismissed(0, bill);

        // THEN
        verify(history).deleteBillWithPrices(BillType.PGE_G11, id, pricesId);
    }

    @Test
    public void onListItemDismissed_fetchAllHistory() throws Exception {
        // GIVEN
        final Bill bill = mock(PgeG11Bill.class);

        // WHEN
        sut.onListItemDismissed(0, bill);

        // THEN
        verify(history).getAll();
    }

    @Test
    public void onListItemDismissed_viewRemovesItemFromList() throws Exception {
        // GIVEN
        final int position = 1;
        final Bill bill = mock(PgeG11Bill.class);

        // WHEN
        sut.onListItemDismissed(position, bill);

        // THEN
        verify(view).setListData(any(LazyList.class));
        verify(view).onItemRemoveFromList(eq(position));
    }

    @Test
    public void onListItemDismissed_showsUndoDeleteMessage() throws Exception {
        // GIVEN
        final Bill bill = mock(PgeG11Bill.class);
        final int position = 0;

        // WHEN
        sut.onListItemDismissed(position, bill);

        // THEN
        verify(view).showUndoDeleteMessage(position);
    }

    @Test
    public void undoDeleteClicked_viewAddsItemToList() throws Exception {
        // WHEN
        final int position = 0;
        sut.undoDeleteClicked(position);

        // THEN
        verify(view).setListData(any(LazyList.class));
        verify(view).onItemInsertedToList(eq(position));
    }

    @Test
    public void undoDeleteClicked_undosDeleteHistory() throws Exception {
        // WHEN
        sut.undoDeleteClicked(0);

        // THEN
        verify(history).undoDelete();
    }

    @Test
    public void undoDeleteClicked_fetchAllHistory() throws Exception {
        // WHEN
        sut.undoDeleteClicked(0);

        // THEN
        verify(history).getAll();
    }

    @Test
    public void onListItemClicked_whenNotInSelectMode_opensBill() throws Exception {
        // GIVEN
        Bill bill = mock(PgeG11Bill.class);
        View itemView = mock(View.class);
        HistoryViewItem item = given_historyViewItem(bill, itemView);

        // WHEN
        sut.onListItemClicked(item);

        // THEN
        verify(view).openBill(bill, itemView);
    }

    @Test
    public void onListItemClicked_whenNotInSelectMode_registerSavedBill() throws Exception {
        // GIVEN
        Bill bill = mock(PgeG11Bill.class);
        HistoryViewItem item = given_historyViewItem(bill);

        // WHEN
        sut.onListItemClicked(item);

        // THEN
        verify(savedBillsRegistry).register(bill);
    }

    @Test
    public void onListItemClicked_andItemSelected_deselectsViewItem() throws Exception {
        // GIVEN
        int position = 2;
        when(selection.isAnySelected()).thenReturn(true);
        when(selection.isSelected(position)).thenReturn(true);
        HistoryViewItem item = given_historyViewItem(position);

        // WHEN
        sut.onListItemClicked(item);

        // THEN
        verify(item).deselect();
    }

    @Test
    public void onListItemClicked_andItemSelected_deselectPosition() throws Exception {
        // GIVEN
        int position = 2;
        when(selection.isAnySelected()).thenReturn(true);
        when(selection.isSelected(position)).thenReturn(true);
        HistoryViewItem item = given_historyViewItem(position);

        // WHEN
        sut.onListItemClicked(item);

        // THEN
        verify(selection).deselect(position);
    }

    @Test
    public void onListItemClicked_whenInSelectMode_andItemNotSelected_selectsViewItem() throws Exception {
        // GIVEN
        when(selection.isAnySelected()).thenReturn(true);
        int position = 2;
        HistoryViewItem item = given_historyViewItem(position);

        // WHEN
        sut.onListItemClicked(item);

        // THEN
        verify(item).select();
    }

    @Test
    public void onListItemClicked_whenInSelectMode_andItemNotSelected_selectsPosition() throws Exception {
        // GIVEN
        when(selection.isAnySelected()).thenReturn(true);
        int position = 2;
        PgeG11Bill bill = mock(PgeG11Bill.class);
        HistoryViewItem item = given_historyViewItem(bill, mock(View.class), position);

        // WHEN
        sut.onListItemClicked(item);

        // THEN
        verify(selection).select(position, bill);
    }

    @Test
    public void onListItemClicked_onSingleSelectedItem_hidesDeleteButton() throws Exception {
        // GIVEN
        when(selection.isAnySelected()).thenReturn(true, false);
        int position = 2;
        when(selection.isSelected(position)).thenReturn(true);
        HistoryViewItem item = given_historyViewItem(position);

        // WHEN
        sut.onListItemClicked(item);

        // THEN
        verify(view).hideDeleteButton();
    }

    @Test
    public void onListItemLongClicked_whenNotInSelectMode_showsDeleteButton() throws Exception {
        // GIVEN
        HistoryViewItem item = given_historyViewItem(2);

        // WHEN
        sut.onListItemLongClicked(item);

        // THEN
        verify(view).showDeleteButton();
    }

    @Test
    public void onListItemLongClicked_whenNotInSelectMode_selectsViewItem() throws Exception {
        // GIVEN
        HistoryViewItem item = given_historyViewItem(2);

        // WHEN
        sut.onListItemLongClicked(item);

        // THEN
        verify(item).select();
    }

    @Test
    public void onListItemLongClicked_andItemSelected_deselectsViewItem() throws Exception {
        // GIVEN
        int position = 2;
        when(selection.isAnySelected()).thenReturn(true);
        when(selection.isSelected(position)).thenReturn(true);
        HistoryViewItem item = given_historyViewItem(position);

        // WHEN
        sut.onListItemLongClicked(item);

        // THEN
        verify(item).deselect();
    }

    @Test
    public void onListItemLongClicked_andItemSelected_deselectsPosition() throws Exception {
        // GIVEN
        int position = 2;
        when(selection.isAnySelected()).thenReturn(true);
        when(selection.isSelected(position)).thenReturn(true);
        HistoryViewItem item = given_historyViewItem(position);

        // WHEN
        sut.onListItemLongClicked(item);

        // THEN
        verify(selection).deselect(position);
    }

    @Test
    public void onListItemLongClicked_whenInSelectMode_andItemNotSelected_selectsViewItem() throws Exception {
        // GIVEN
        when(selection.isAnySelected()).thenReturn(true);
        HistoryViewItem item = given_historyViewItem(2);

        // WHEN
        sut.onListItemLongClicked(item);

        // THEN
        verify(item).select();
    }

    @Test
    public void onListItemLongClicked_whenItemNotSelected_selectsPosition() throws Exception {
        // GIVEN
        int position = 2;
        Bill bill = mock(Bill.class);
        HistoryViewItem item = given_historyViewItem(bill, mock(View.class), position);

        // WHEN
        sut.onListItemLongClicked(item);

        // THEN
        verify(selection).select(position, bill);
    }

    @Test
    public void onListItemLongClicked_onSingleSelectedItem_hidesDeleteButton() throws Exception {
        // GIVEN
        int position = 2;
        when(selection.isAnySelected()).thenReturn(true, false);
        when(selection.isSelected(position)).thenReturn(true);
        HistoryViewItem item = given_historyViewItem(position);

        // WHEN
        sut.onListItemLongClicked(item);

        // THEN
        verify(view).hideDeleteButton();
    }

    @Test
    public void deleteClicked_deletesSelectedBill() throws Exception {
        // GIVEN
        final Long id = 1L;
        final Long pricesId = 2L;
        Bill bill = mock(PgeG11Bill.class);
        when(bill.getId()).thenReturn(id);
        when(bill.getPricesId()).thenReturn(pricesId);

        when(selection.isAnySelected()).thenReturn(true);
        when(selection.getItems()).thenReturn(Arrays.asList(bill, bill));
        when(selection.getPositionsReverseOrder()).thenReturn(new int[]{3, 2});

        // WHEN
        sut.deleteClicked();

        // THEN
        verify(history, times(2)).deleteBillWithPrices(BillType.PGE_G11, id, pricesId);
    }

    @Test
    public void deleteClicked_removesViewsFromList() throws Exception {
        // GIVEN
        int position1 = 2;
        int position2 = 5;
        when(selection.isAnySelected()).thenReturn(true);
        when(selection.getPositionsReverseOrder()).thenReturn(new int[]{position2, position1});

        // WHEN
        sut.deleteClicked();

        // THEN
        verify(view).setListData(any(LazyList.class));
        verify(view).onItemRemoveFromList(eq(position2));
        verify(view).onItemRemoveFromList(eq(position1));
    }

    @Test
    public void deleteClicked_clearsSelection() throws Exception {
        // GIVEN
        when(selection.isAnySelected()).thenReturn(true);
        when(selection.getPositionsReverseOrder()).thenReturn(new int[0]);

        // WHEN
        sut.deleteClicked();

        // THEN
        verify(selection).deselectAll();
    }

    @Test
    public void deleteClicked_hidesDeleteButton() throws Exception {
        // GIVEN
        when(selection.isAnySelected()).thenReturn(true);
        when(selection.getPositionsReverseOrder()).thenReturn(new int[0]);

        // WHEN
        sut.deleteClicked();

        // THEN
        verify(view).hideDeleteButton();
    }

    private HistoryViewItem given_historyViewItem(int position) {
        return given_historyViewItem(mock(PgeG11Bill.class), mock(View.class), position);
    }

    private HistoryViewItem given_historyViewItem(Bill bill) {
        return given_historyViewItem(bill, mock(View.class), 0);
    }

    private HistoryViewItem given_historyViewItem(Bill bill, View itemView) {
        return given_historyViewItem(bill, itemView, 0);
    }

    private HistoryViewItem given_historyViewItem(Bill bill, View itemView, int position) {
        HistoryViewItem item = mock(HistoryViewItem.class);
        when(item.getBill()).thenReturn(bill);
        when(item.getView()).thenReturn(itemView);
        when(item.getPositionOnList()).thenReturn(position);
        return item;
    }
}