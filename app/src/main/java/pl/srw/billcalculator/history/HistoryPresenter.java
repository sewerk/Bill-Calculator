package pl.srw.billcalculator.history;

import javax.inject.Inject;

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

    public void addButtonClicked() {
        present(new UIChange<HistoryView>() {
            @Override
            public void change(HistoryView view) {
                view.showMoreButtons();
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

    public void newPgeBillClicked() {
        // TODO
    }

    public void newPgnigButtonClicked() {
        // TODO
    }

    public void newTauronBillClicked() {
        // TODO
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

        void showMoreButtons();

        void openSettings();

        boolean isDrawerOpen();

        void closeDrawer();

        void showAbout();
    }
}
