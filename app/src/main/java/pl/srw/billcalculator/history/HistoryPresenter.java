package pl.srw.billcalculator.history;

import android.view.View;

import org.greenrobot.greendao.query.LazyList;

import java.util.Arrays;

import javax.inject.Inject;

import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.data.ApplicationRepo;
import pl.srw.billcalculator.data.bill.HistoryRepo;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.form.fragment.FormPresenter;
import pl.srw.billcalculator.history.list.item.HistoryItemClickListener;
import pl.srw.billcalculator.history.list.item.HistoryItemDismissHandling;
import pl.srw.billcalculator.history.list.item.HistoryViewItem;
import pl.srw.billcalculator.util.BillSelection;
import pl.srw.billcalculator.util.analytics.Analytics;
import pl.srw.billcalculator.util.analytics.EventType;
import pl.srw.mfvp.MvpPresenter;
import pl.srw.mfvp.di.scope.RetainActivityScope;
import timber.log.Timber;

@RetainActivityScope
public class HistoryPresenter extends MvpPresenter<HistoryPresenter.HistoryView>
        implements HistoryItemDismissHandling, HistoryItemClickListener, FormPresenter.HistoryChangeListener {

    private final ApplicationRepo applicationRepo;
    private final HistoryRepo history;
    private final SavedBillsRegistry savedBillsRegistry;
    private final BillSelection selection;
    private LazyList<History> historyData; // FIXME: remove state from presenter?
    private boolean needRefresh;

    @Inject
    HistoryPresenter(ApplicationRepo applicationRepo, HistoryRepo history, SavedBillsRegistry savedBillsRegistry, BillSelection selection) {
        this.applicationRepo = applicationRepo;
        this.history = history;
        this.savedBillsRegistry = savedBillsRegistry;
        this.selection = selection;
    }

    @Override
    protected void onFirstBind() {
        loadHistoryData();
        Timber.i("history size = %d", historyData.size());

        present(view -> {
            view.setListData(historyData);

            if (applicationRepo.isFirstLaunch()) {
                if (historyData.size() > 0) {
                    Timber.e(new IllegalStateException("Db exist with clean SharedPrefs"));
                }
                view.showWelcomeDialog();
                applicationRepo.markHelpShown();
            } else if (!applicationRepo.wasHelpShown()) {
                view.showNewUIDialog();
                applicationRepo.markHelpShown();
            }
        });
    }

    @Override
    protected void onNewViewRestoreState() {
        if (needRefresh) { // FIXME: when BillActivity will be closed quickly then DrawerActivity.onStart will not be called - will not refresh
            loadHistoryData();
        }
        present(view -> {
            view.setListData(historyData);
            if (needRefresh) {
                view.redrawList();
            }
            if (selection.isAnySelected()) {
                view.disableSwipeDelete();
            }
        });
        needRefresh = false;
    }

    @Override
    public void onHistoryChanged() {
        needRefresh = true;
        selection.deselectAll();
        present(view -> {
            view.hideDeleteButton();
            view.enableSwipeDelete();
        });
    }

    @Override
    public void onListItemDismissed(final int position, Bill bill) {
        Analytics.event(EventType.DELETE_BILL, "dismissed", "true");
        Timber.d("bill id=%d", bill.getId());
        history.cacheBillForUndoDelete(bill);
        history.deleteBillWithPrices(bill);
        loadHistoryData();
        present(view -> {
            view.setListData(historyData);
            view.onItemRemoveFromList(position);
            view.showUndoDeleteMessage(position);
        });
    }

    @Override
    public void onListItemClicked(final HistoryViewItem item) {
        if (selection.isAnySelected()) {
            handleSelection(item);
        } else {
            openBill(item);
        }
    }

    @Override
    public void onListItemLongClicked(HistoryViewItem item) {
        if (!selection.isAnySelected()) {
            present(view -> {
                view.showDeleteButton();
                view.disableSwipeDelete();
            });
        }
        handleSelection(item);
    }

    void helpMenuClicked() {
        present(HistoryView::showHelp);
    }

    void undoDeleteClicked(final int... positions) {
        if (!history.isUndoDeletePossible()) {
            Timber.d("Undo delete clicked twice");
            return;
        }
        history.undoDelete();
        loadHistoryData();
        present(view -> {
            Arrays.sort(positions);
            selection.onInsert(positions[0]);
            view.setListData(historyData);
            for (int position : positions) {
                view.onItemInsertedToList(position);
            }
        });
    }

    void deleteClicked() {
        Analytics.event(EventType.DELETE_BILL,
                "dismissed", "false",
                "selected", "" + selection.getItems().size());
        history.cacheBillsForUndoDelete(selection.getItems());
        history.deleteBillsWithPrices(selection.getItems());
        loadHistoryData();
        present(view -> {
            view.setListData(historyData);
            for (int position : selection.getPositionsReverseOrder()) {
                view.onItemRemoveFromList(position);
            }
            view.hideDeleteButton();
            view.enableSwipeDelete();
            view.showUndoDeleteMessage(selection.getPositionsReverseOrder()); // FIXME: return non ordered, sort when needed
        });
        selection.deselectAll();
    }

    private void loadHistoryData() {
        if (historyData != null)
            historyData.close();

        historyData = history.getAll();
    }

    private void handleSelection(HistoryViewItem item) {
        int position = item.getPositionOnList();

        if (selection.isSelected(position)) {
            selection.deselect(position);
            item.deselect();
        } else {
            selection.select(position, item.getBill());
            item.select();
        }
        if (!selection.isAnySelected()) {
            present(view -> {
                view.hideDeleteButton();
                view.enableSwipeDelete();
            });
        }
    }

    private void openBill(final HistoryViewItem item) {
        Timber.i("Open bill");
        Timber.d("bill id=%d", item.getBill().getId());
        present(view -> view.openBill(item.getBill(), item.getView()));
        savedBillsRegistry.register(item.getBill());
    }

    public interface HistoryView {

        void showHelp();

        void showWelcomeDialog();

        void setListData(LazyList<History> data);

        void showUndoDeleteMessage(int... position);

        void redrawList();

        void onItemRemoveFromList(int position);

        void onItemInsertedToList(int position);

        void openBill(Bill bill, View viewClicked);

        void showDeleteButton();

        void hideDeleteButton();

        void enableSwipeDelete();

        void disableSwipeDelete();

        void showNewUIDialog();
    }
}
