package pl.srw.billcalculator.settings;

import javax.inject.Inject;

import pl.srw.billcalculator.type.Provider;
import pl.srw.mfvp.MvpPresenter;
import pl.srw.mfvp.di.scope.RetainActivityScope;

@RetainActivityScope
public class SettingsPresenter extends MvpPresenter<SettingsPresenter.SettingsView> {

    private final Provider[] providers;
    private boolean isOnTablet;

    @Inject
    SettingsPresenter(Provider[] providers) {
        this.providers = providers;
    }

    @Override
    protected void onFirstBind() {
        present(view -> {
            view.fillProviderList(providers);
            if (isOnTablet) {
                view.selectProvider(0);
                view.showSettingsFor(providers[0]);
            }
        });
    }

    @Override
    protected void onNewViewRestoreState() {
        present(view -> view.fillProviderList(providers));
    }

    public void setup(boolean isOnTablet) {
        this.isOnTablet = isOnTablet;
    }

    public void providerClicked(final int index) {
        final Provider provider = providers[index];
        present(view -> {
            if (isOnTablet) {
                view.showSettingsFor(provider);
                view.selectProvider(index);
            } else {
                view.showSettingsScreenFor(provider);
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
