package pl.srw.billcalculator.history;

import android.view.View;

import java.util.Arrays;

import javax.inject.Inject;

import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.data.ApplicationRepo;
import pl.srw.billcalculator.data.bill.HistoryRepo;
import pl.srw.billcalculator.db.Bill;
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
    private final HistoryRepo historyRepo;
    private final SavedBillsRegistry savedBillsRegistry;
    private final BillSelection selection;

    @Inject
    HistoryPresenter(ApplicationRepo applicationRepo, HistoryRepo historyRepo, SavedBillsRegistry savedBillsRegistry, BillSelection selection) {
        this.applicationRepo = applicationRepo;
        this.historyRepo = historyRepo;
        this.savedBillsRegistry = savedBillsRegistry;
        this.selection = selection;
    }

    @Override
    protected void onFirstBind() {
        present(view -> {
            if (applicationRepo.isFirstLaunch()) {
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
        present(view -> {
            if (selection.isAnySelected()) {
                view.disableSwipeDelete();
            }
        });
    }

    @Override
    public void onHistoryChanged() {
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
        historyRepo.cacheBillForUndoDelete(bill);
        historyRepo.deleteBillWithPrices(bill);
        present(view -> view.showUndoDeleteMessage(position));
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
        if (!historyRepo.isUndoDeletePossible()) {
            Timber.d("Undo delete clicked twice");
            return;
        }
        Arrays.sort(positions);
        for (int position : positions) {
            selection.onInsert(position);
        }
        historyRepo.undoDelete();
        present(view -> view.scrollToPosition(positions[positions.length - 1]));
    }

    void deleteClicked() {
        Analytics.event(EventType.DELETE_BILL,
                "dismissed", "false",
                "selected", "" + selection.getItems().size());
        historyRepo.cacheBillsForUndoDelete(selection.getItems());
        historyRepo.deleteBillsWithPrices(selection.getItems());
        present(view -> {
            view.hideDeleteButton();
            view.enableSwipeDelete();
            view.showUndoDeleteMessage(selection.getPositionsReverseOrder()); // FIXME: return non ordered, sort when needed
        });
        selection.deselectAll();
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

        void showUndoDeleteMessage(int... position);

        void scrollToPosition(int position);

        void openBill(Bill bill, View viewClicked);

        void showDeleteButton();

        void hideDeleteButton();

        void enableSwipeDelete();

        void disableSwipeDelete();

        void showNewUIDialog();
    }
}
