package pl.srw.billcalculator.form.fragment;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.ProviderMapper;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
    @Parameters
    @TestCaseName("getFormLayout for {0} and tariff {1} should return form or form_g12")
    public void getFormLayout_forEnergy_returnProperForm(Provider provider, String tariff, int expected) throws Exception {
        // GIVEN
        sut.setup(provider);
        when(providerMapper.getPrices(any(Provider.class))).thenReturn(prices);
        when(prices.getTariff()).thenReturn(tariff);

        // WHEN
        final int result = sut.getFormLayout();

        // THEN
        assertEquals(expected, result);
    }

    private Object parametersForGetFormLayout_forEnergy_returnProperForm() {
        return new Object[]{
                new Object[]{Provider.PGNIG, null, R.layout.form},
                new Object[]{Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G11, R.layout.form},
                new Object[]{Provider.PGE, SharedPreferencesEnergyPrices.TARIFF_G12, R.layout.form_g12},
                new Object[]{Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G11, R.layout.form},
                new Object[]{Provider.TAURON, SharedPreferencesEnergyPrices.TARIFF_G12, R.layout.form_g12},
        };
    }
}