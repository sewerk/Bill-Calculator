package pl.srw.billcalculator.history;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import pl.srw.billcalculator.type.Provider;

import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class HistoryPresenterTest {

    @InjectMocks private HistoryPresenter sut;
    
    @Mock private HistoryPresenter.HistoryView view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void helpMenuShowsHelp() throws Exception {
        // WHEN
        sut.helpMenuClicked();

        // THEN
        verify(view).showHelp();
    }

    @Test
    public void settingsMenuOpensSettings() throws Exception {
        // WHEN
        sut.settingsClicked();

        // THEN
        verify(view).openSettings();
    }

    @Test
    public void backPressedClosesDrawerWhenOpen() throws Exception {
        // GIVEN
        when(view.isDrawerOpen()).thenReturn(true);

        // WHEN
        sut.handleBackPressed();

        // THEN
        verify(view).closeDrawer();
    }

    @Test
    @Parameters
    public void newBillClicked_showsForm(Provider provider) throws Exception {
        // WHEN
        sut.newBillClicked(provider);

        // THEN
        verify(view).showNewBillForm(provider);
    }

    private Object parametersForNewBillClicked_showsForm() {
        return new Provider[] { Provider.PGE, Provider.PGNIG, Provider.TAURON};
    }
}