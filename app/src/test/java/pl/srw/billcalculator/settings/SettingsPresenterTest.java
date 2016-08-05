package pl.srw.billcalculator.settings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.srw.billcalculator.type.Provider;

public class SettingsPresenterTest {

    @InjectMocks private SettingsPresenter sut;

    @Mock private SettingsPresenter.SettingsView view;
    private Provider[] providers = new Provider[2];

    @Before
    public void setUp() throws Exception {
        sut = new SettingsPresenter(providers);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onFirstBind_fillUpProviders() throws Exception {
        // WHEN
        sut.onFirstBind();

        // THEN
        verify(view).fillProviderList(providers);
    }

    @Test
    public void onFirstBind_selectsFirstProvider_whenOnTablet() throws Exception {
        // GIVEN
        sut.setup(true);

        // WHEN
        sut.onFirstBind();

        // THEN
        verify(view).selectProvider(0);
    }

    @Test
    public void onFirstBind_showFirstProviderSettings_whenOnTablet() throws Exception {
        // GIVEN
        sut.setup(true);
        providers[0] = Provider.PGE;

        // WHEN
        sut.onFirstBind();

        // THEN
        verify(view).showSettingsFor(providers[0]);
    }

    @Test
    public void onFirstBind_selectsFirstProvider_whenOnPhone() throws Exception {
        // GIVEN
        sut.setup(false);

        // WHEN
        sut.onFirstBind();

        // THEN
        verify(view, never()).selectProvider(anyInt());
    }

    @Test
    public void providerClicked_showProviderSettings_whenOnTablet() throws Exception {
        // GIVEN
        sut.setup(true);
        providers[0] = Provider.PGE;

        // WHEN
        sut.providerClicked(0);

        // THEN
        verify(view).showSettingsFor(providers[0]);
    }

    @Test
    public void providerClicked_showProviderSettingsScreen_whenOnPhone() throws Exception {
        // GIVEN
        sut.setup(false);
        providers[0] = Provider.PGE;

        // WHEN
        sut.providerClicked(0);

        // THEN
        verify(view).showSettingsScreenFor(providers[0]);
    }

    @Test
    public void providerClicked_selectProviderOnList_whenOnTablet() throws Exception {
        // GIVEN
        sut.setup(true);
        final int position = 1;

        // WHEN
        sut.providerClicked(position);

        // THEN
        verify(view).selectProvider(position);
    }
}