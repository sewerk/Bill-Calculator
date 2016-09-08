package pl.srw.billcalculator.settings.restore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.srw.billcalculator.settings.prices.RestorablePrices;
import pl.srw.billcalculator.util.ProviderMapper;

import static org.mockito.Mockito.verify;

public class ConfirmRestoreSettingsPresenterTest {
    @InjectMocks private ConfirmRestoreSettingsPresenter sut;
    @Mock private RestorablePrices prices;
    @Mock private ConfirmRestoreSettingsPresenter.ConfirmRestoreSettingsView view;
    @Mock private ProviderMapper providerMapper;

    @Before
    public void setUp() throws Exception {
        sut = new ConfirmRestoreSettingsPresenter(providerMapper);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onConfirmedClicked_setDefaultPrices() throws Exception {
        // WHEN
        sut.onConfirmedClicked();

        // THEN
        verify(prices).setDefault();
    }

    @Test
    public void onConfirmedClicked_refreshView() throws Exception {
        // WHEN
        sut.onConfirmedClicked();

        // THEN
        verify(view).refresh();
    }
}