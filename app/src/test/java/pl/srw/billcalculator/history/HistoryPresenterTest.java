package pl.srw.billcalculator.history;

import android.view.View;

import org.greenrobot.greendao.query.LazyList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import pl.srw.billcalculator.Whitebox;
import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.settings.global.SettingsRepo;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.wrapper.HistoryRepo;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
    public void myBillsClicked_closesDrawer() throws Exception {
        // WHEN
        sut.myBillsClicked();

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
        verify(view).itemRemovedFromList(eq(position), any(LazyList.class));
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
        verify(view).itemAddedToList(eq(position), any(LazyList.class));
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
    public void onListItemClicked_opensBill() throws Exception {
        // GIVEN
        Bill bill = mock(PgeG11Bill.class);
        View itemView = mock(View.class);

        // WHEN
        sut.onListItemClicked(bill, itemView);

        // THEN
        verify(view).openBill(bill, itemView);
    }

    @Test
    public void onListItemClicked_registerSavedBill() throws Exception {
        // GIVEN
        Bill bill = mock(PgeG11Bill.class);

        // WHEN
        sut.onListItemClicked(bill, mock(View.class));

        // THEN
        verify(savedBillsRegistry).register(bill);
    }
}