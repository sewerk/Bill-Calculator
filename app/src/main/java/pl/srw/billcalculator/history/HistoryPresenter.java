package pl.srw.billcalculator.history;

import android.view.View;

import org.greenrobot.greendao.query.LazyList;

import javax.inject.Inject;

import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.form.fragment.FormPresenter;
import pl.srw.billcalculator.history.list.item.HistoryItemClickListener;
import pl.srw.billcalculator.history.list.item.HistoryItemDismissHandling;
import pl.srw.billcalculator.history.list.item.HistoryViewItem;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.settings.global.SettingsRepo;
import pl.srw.billcalculator.type.ActionType;
import pl.srw.billcalculator.type.ContentType;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.BillSelection;
import pl.srw.billcalculator.wrapper.Analytics;
import pl.srw.billcalculator.wrapper.HistoryRepo;
import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.mfvp.presenter.MvpPresenter;
import timber.log.Timber;

@RetainActivityScope
public class HistoryPresenter extends MvpPresenter<HistoryPresenter.HistoryView>
        implements HistoryItemDismissHandling, HistoryItemClickListener, FormPresenter.HistoryUpdating {

    private final SettingsRepo settings;
    private final HistoryRepo history;
    private final SavedBillsRegistry savedBillsRegistry;
    private final BillSelection selection;
    private LazyList<History> historyData; // TODO: remove state from presenter?
    private boolean needRefresh;

    @Inject
    HistoryPresenter(SettingsRepo settings, HistoryRepo history, SavedBillsRegistry savedBillsRegistry, BillSelection selection) {
        this.settings = settings;
        this.history = history;
        this.savedBillsRegistry = savedBillsRegistry;
        this.selection = selection;
    }

    @Override
    protected void onFirstBind() {
        loadHistoryData();
        Analytics.logContent(ContentType.HISTORY, "history size", String.valueOf(historyData.size()));

        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                if (settings.isFirstLaunch()) {
                    view.showWelcomeDialog();
                }
                view.setListData(historyData);
                view.redrawList();
            }
        });
    }

    @Override
    protected void onNewViewRestoreState() {
        if (needRefresh) {
            loadHistoryData();
        }
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.setListData(historyData);
                view.redrawList();// TODO: this is unnecessary then coming back from other screen
            }
        });
    }

    @Override
    public void onHistoryChanged() {
        needRefresh = true;
    }

    @Override
    public void onListItemDismissed(final int position, Bill bill) {
        Analytics.logAction(ActionType.DELETE_BILL, "dismissed", "true");
        history.deleteBillWithPrices(BillType.valueOf(bill), bill.getId(), bill.getPricesId());
        loadHistoryData();
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.setListData(historyData);
                view.onItemRemoveFromList(position);
                view.showUndoDeleteMessage(position);
            }
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
            present(new UIChange<HistoryView>() {
                @Override
                public void change(HistoryView view) {
                    view.showDeleteButton();
                }
            });
        }
        handleSelection(item);
    }

    void helpMenuClicked() {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.showHelp();
            }
        });
    }

    void settingsClicked() {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.openSettings();
                view.closeDrawer();
            }
        });
    }

    boolean handleBackPressed() {
        final boolean[] handled = {false};
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                if (view.isDrawerOpen()) {
                    view.closeDrawer();
                    handled[0] = true;
                }
            }
        });
        return handled[0];
    }

    void historyClicked() {
        //TODO remove this option from drawer?
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.closeDrawer();
            }
        });
    }

    void newBillClicked(final Provider provider) {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.showNewBillForm(provider);
                view.closeDrawer();
            }
        });
    }

    void aboutClicked() {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.showAbout();
                view.closeDrawer();
            }
        });
    }

    void undoDeleteClicked(final int position) {
        Analytics.log("Undo clicked");
        if (!history.isUndoDeletePossible()) {
            Timber.d("Undo delete clicked twice");
            return;
        }
        history.undoDelete();
        loadHistoryData();
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.setListData(historyData);
                view.onItemInsertedToList(position);
            }
        });
    }

    void deleteClicked() {
        Analytics.logAction(ActionType.DELETE_BILL,
                "dismissed", "false",
                "selected", "" + selection.getItems().size());
        for (Bill bill : selection.getItems()) {
            history.deleteBillWithPrices(BillType.valueOf(bill), bill.getId(), bill.getPricesId());
        }
        loadHistoryData();
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.setListData(historyData);
                for (int position : selection.getPositionsReverseOrder()) {
                    view.onItemRemoveFromList(position);
                }
                view.hideDeleteButton();
            }
        });
        selection.deselectAll();
    }

    private void loadHistoryData() {
        if (historyData != null)
            historyData.close();

        historyData = history.getAll();
        needRefresh = false;
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
            present(new UIChange<HistoryView>() {
                @Override
                public void change(HistoryView view) {
                    view.hideDeleteButton();
                }
            });
        }
    }

    private void openBill(final HistoryViewItem item) {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.openBill(item.getBill(), item.getView());
            }
        });
        savedBillsRegistry.register(item.getBill());
    }

    interface HistoryView {

        void showHelp();

        void openSettings();

        boolean isDrawerOpen();

        void closeDrawer();

        void showAbout();

        void showNewBillForm(Provider provider);

        void showWelcomeDialog();

        void setListData(LazyList<History> data);

        void showUndoDeleteMessage(int position);

        void redrawList();

        void onItemRemoveFromList(int position);

        void onItemInsertedToList(int position);

        void openBill(Bill bill, View viewClicked);

        void showDeleteButton();

        void hideDeleteButton();
    }
}
