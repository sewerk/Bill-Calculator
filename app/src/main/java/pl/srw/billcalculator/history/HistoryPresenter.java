package pl.srw.billcalculator.history;

import javax.inject.Inject;

import pl.srw.billcalculator.type.Provider;
import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.mfvp.presenter.MvpPresenter;

@RetainActivityScope
public class HistoryPresenter extends MvpPresenter<HistoryPresenter.HistoryView> {

    @Inject
    public HistoryPresenter() {
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
        //TODO
    }

    public void newBillClicked(final Provider provider) {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView historyView) {
                historyView.showNewBillForm(provider);
            }
        });
    }

    public void aboutClicked() {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.showAbout();
            }
        });
    }

    public interface HistoryView {

        void showHelp();

        void openSettings();

        boolean isDrawerOpen();

        void closeDrawer();

        void showAbout();

        void showNewBillForm(Provider provider);
    }
}
