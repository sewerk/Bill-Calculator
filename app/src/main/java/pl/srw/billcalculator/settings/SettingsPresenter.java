package pl.srw.billcalculator.settings;

import javax.inject.Inject;

import pl.srw.billcalculator.type.Provider;
import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.mfvp.presenter.MvpPresenter;

@RetainActivityScope
public class SettingsPresenter extends MvpPresenter<SettingsPresenter.SettingsView> {

    private boolean isOnTablet;
    private Provider[] providers;

    @Inject
    protected SettingsPresenter(Provider[] providers) {
        this.providers = providers;
    }

    @Override
    protected void onFirstBind() {
        present(new UIChange<SettingsView>() {
            @Override
            public void change(SettingsView view) {
                view.fillProviderList(providers);
                if (isOnTablet) {
                    view.selectProvider(0);
                    view.showSettingsFor(providers[0]);
                }
            }
        });
    }

    @Override
    protected void onNewViewRestoreState() {
        present(new UIChange<SettingsView>() {
            @Override
            public void change(SettingsView view) {
                view.fillProviderList(providers);
            }
        });
    }

    public void setup(boolean isOnTablet) {
        this.isOnTablet = isOnTablet;
    }

    public void providerClicked(final int index) {
        final Provider provider = providers[index];
        present(new UIChange<SettingsView>() {
            @Override
            public void change(SettingsView view) {
                if (isOnTablet) {
                    view.showSettingsFor(provider);
                    view.selectProvider(index);
                } else {
                    view.showSettingsScreenFor(provider);
                }
            }
        });
    }

    public interface SettingsView {

        void selectProvider(int position);

        void fillProviderList(Provider[] providers);

        void showSettingsScreenFor(Provider provider);

        void showSettingsFor(Provider provider);
    }
}
