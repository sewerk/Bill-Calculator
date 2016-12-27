package pl.srw.billcalculator.form.fragment;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.ProviderMapper;

import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class FormPresenterTest {

    @InjectMocks private FormPresenter sut;

    @Mock private FormPresenter.FormView view;
    @Mock private ProviderMapper providerMapper;
    @Mock private SharedPreferencesEnergyPrices prices;

    @Before
    public void setUp() throws Exception {
        sut = new FormPresenter(providerMapper);
        MockitoAnnotations.initMocks(this);
        when(providerMapper.getPrices(any(Provider.class))).thenReturn(prices);
    }

    @Test
    @Parameters(method = "paramsForSetFormValues")
    public void onFirstBind_setFormValues(Provider provider, @Nullable String tariff, @StringRes int readingUnitResId) throws Exception {
        // GIVEN
        sut.setup(provider);
        when(prices.getTariff()).thenReturn(tariff);

        // WHEN
        sut.onFirstBind();

        // THEN
        verify(view).setLogo(provider);
        verify(view).setupSettingsLink();
        verify(view, times(tariff != null ? 1 : 0)).setTariffText(tariff);
        verify(view).setReadingUnit(readingUnitResId);
        verify(view).setDates(anyString(), anyString());
    }

    private Object paramsForSetFormValues() {
        return new Object[] {
                new Object[] {Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G11, R.string.form_reading_unit_kWh},
                new Object[] {Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G12, R.string.form_reading_unit_kWh},
                new Object[] {Provider.PGNIG, null, R.string.form_reading_unit_m3},
                new Object[] {Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G11, R.string.form_reading_unit_kWh},
                new Object[] {Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G12, R.string.form_reading_unit_kWh},
        };
    }

    @Test
    @Parameters(method = "paramsForSetFormValues")
    public void onNewViewRestoreState_setFormValues(Provider provider, @Nullable String tariff, @StringRes int readingUnitResId) throws Exception {
        // GIVEN
        sut.setup(provider);
        when(prices.getTariff()).thenReturn(tariff);

        // WHEN
        sut.onNewViewRestoreState();

        // THEN
        verify(view).setLogo(provider);
        verify(view, never()).setupSettingsLink();
        verify(view, times(tariff != null ? 1 : 0)).setTariffText(tariff);
        verify(view).setReadingUnit(readingUnitResId);
        verify(view, never()).setDates(anyString(), anyString());
    }

    @Test
    @Parameters
    public void whenSettingsLinkClicked_showProviderSettings(Provider provider) throws Exception {
        // GIVEN
        sut.setup(provider);

        // WHEN
        sut.settingsLinkClicked();

        // THEN
        verify(view).showProviderSettings(provider);
    }

    private Object parametersForWhenSettingsLinkClicked_showProviderSettings() {
        return new Provider[] {Provider.PGE, Provider.PGNIG, Provider.TAURON};
    }

    @Test
    public void whenCloseButtonClicked_dismissForm() throws Exception {
        // WHEN
        sut.closeButtonClicked();

        // THEN
        verify(view).hideForm();
    }

    @Test
    public void onFirstBind_forPGNIG_hidesDoubleReadingsVisibility() throws Exception {
        // GIVEN
        sut.setup(Provider.PGNIG);

        // WHEN
        sut.onFirstBind();

        // THEN
        verify(view).setDoubleReadingsVisibility(View.GONE);
    }

    @Test
    public void onNewViewRestoreState_forPGNIG_hidesDoubleReadingsVisibility() throws Exception {
        // GIVEN
        sut.setup(Provider.PGNIG);

        // WHEN
        sut.onNewViewRestoreState();

        // THEN
        verify(view).setDoubleReadingsVisibility(View.GONE);
    }

    @Test
    @Parameters(method = "paramsForReadinngVisibilityTest")
    public void onFirstBind_hidesReadingsVisibility(Provider provider, String tariff, int singleVisibility, int doubleVisibility) throws Exception {
        // GIVEN
        sut.setup(provider);
        when(prices.getTariff()).thenReturn(tariff);

        // WHEN
        sut.onFirstBind();

        // THEN
        verify(view).setSingleReadingsVisibility(singleVisibility);
        verify(view).setDoubleReadingsVisibility(doubleVisibility);
    }

    private Object[] paramsForReadinngVisibilityTest() {
        return new Object[] {
                new Object[] {Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G11, View.VISIBLE, View.GONE},
                new Object[] {Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G11, View.VISIBLE, View.GONE},
                new Object[] {Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G12, View.GONE, View.VISIBLE},
                new Object[] {Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G12, View.GONE, View.VISIBLE},
        };
    }

    @Test
    @Parameters(method = "paramsForReadinngVisibilityTest")
    public void onNewViewRestoreState_hidesReadingsVisibility(Provider provider, String tariff, int singleVisibility, int doubleVisibility) throws Exception {
        // GIVEN
        sut.setup(provider);
        when(prices.getTariff()).thenReturn(tariff);

        // WHEN
        sut.onNewViewRestoreState();

        // THEN
        verify(view).setSingleReadingsVisibility(singleVisibility);
        verify(view).setDoubleReadingsVisibility(doubleVisibility);
    }
}