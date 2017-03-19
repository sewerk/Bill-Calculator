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
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.settings.global.SettingsRepo;
import pl.srw.billcalculator.type.ContentType;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.wrapper.Analytics;
import pl.srw.billcalculator.wrapper.HistoryRepo;
import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.mfvp.presenter.MvpPresenter;

@RetainActivityScope
public class HistoryPresenter extends MvpPresenter<HistoryPresenter.HistoryView>
        implements HistoryItemDismissHandling, HistoryItemClickListener, FormPresenter.HistoryUpdating {

    private final SettingsRepo settings;
    private final HistoryRepo history;
    private final SavedBillsRegistry savedBillsRegistry;
    private LazyList<History> historyData; // TODO: remove state from presenter?
    private boolean needRefresh;

    @Inject
    public HistoryPresenter(SettingsRepo settings, HistoryRepo history, SavedBillsRegistry savedBillsRegistry) {
        this.settings = settings;
        this.history = history;
        this.savedBillsRegistry = savedBillsRegistry;
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
                view.setListData(historyData); // TODO: BUG: when data not change the lazyList is closed by this call
            }
        });
    }

    @Override
    public void onHistoryChanged() {
        needRefresh = true;
    }

    public void helpMenuClicked() {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.showHelp();
            }
        });
    }

    public void settingsClicked() {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.openSettings();
                view.closeDrawer();
            }
        });
    }

    public boolean handleBackPressed() {
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

    public void myBillsClicked() {
        //TODO remove this option from drawer?
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.closeDrawer();
            }
        });
    }

    public void newBillClicked(final Provider provider) {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.showNewBillForm(provider);
                view.closeDrawer();
            }
        });
    }

    public void aboutClicked() {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.showAbout();
                view.closeDrawer();
            }
        });
    }

    @Override
    public void onListItemDismissed(final int position, Bill bill) {
        history.deleteBillWithPrices(BillType.valueOf(bill), bill.getId(), bill.getPricesId());
        loadHistoryData();
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.itemRemovedFromList(position, historyData);
                view.showUndoDeleteMessage(position);
            }
        });
    }

    public void undoDeleteClicked(final int position) {
        history.undoDelete();
        loadHistoryData();
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.itemAddedToList(position, historyData);
            }
        });
    }

    @Override
    public void onListItemClicked(final Bill bill, final View viewClicked) {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.openBill(bill, viewClicked);
            }
        });
        savedBillsRegistry.register(bill);
    }

    private void loadHistoryData() {
        historyData = history.getAll();
        needRefresh = false;
    }

    public interface HistoryView {

        void showHelp();

        void openSettings();

        boolean isDrawerOpen();

        void closeDrawer();

        void showAbout();

        void showNewBillForm(Provider provider);

        void showWelcomeDialog();

        void setListData(LazyList<History> data);

        void showUndoDeleteMessage(int position);

        void itemRemovedFromList(int position, LazyList<History> newData);

        void itemAddedToList(int position, LazyList<History> newData);

        void openBill(Bill bill, View viewClicked);
    }
}
