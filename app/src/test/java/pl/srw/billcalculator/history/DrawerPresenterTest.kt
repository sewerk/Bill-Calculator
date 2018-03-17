package pl.srw.billcalculator.history

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import pl.srw.billcalculator.type.Provider

@RunWith(JUnitParamsRunner::class)
class DrawerPresenterTest {

    val view: DrawerPresenter.View = mock()
    val sut = DrawerPresenter(view)

    @Test
    fun settingsClicked_opensSettings() {
        // WHEN
        sut.settingsClicked()

        // THEN
        verify(view).openSettings()
    }

    @Test
    fun settingsClicked_closesDrawer() {
        // WHEN
        sut.settingsClicked()

        // THEN
        verify(view).closeDrawer()
    }

    @Test
    fun handleBackPressed_whenDrawerOpened_closesDrawer() {
        // GIVEN
        whenever(view.isDrawerOpen()).thenReturn(true)

        // WHEN
        sut.handleBackPressed()

        // THEN
        verify(view).closeDrawer()
    }

    @Test
    fun handleBackPressed_whenDrawerOpened_returnsTrue() {
        // GIVEN
        whenever(view.isDrawerOpen()).thenReturn(true)

        // WHEN
        val result = sut.handleBackPressed()

        // THEN
        assertTrue(result)
    }

    @Test
    fun handleBackPressed_whenDrawerClosed_returnsFalse() {
        // GIVEN
        whenever(view.isDrawerOpen()).thenReturn(false)

        // WHEN
        val result = sut.handleBackPressed()

        // THEN
        assertFalse(result)
    }

    @Test
    fun historyClicked_closesDrawer() {
        // WHEN
        sut.historyClicked()

        // THEN
        verify(view).closeDrawer()
    }

    @Test
    @Parameters("PGE", "PGNIG", "TAURON")
    fun newBillClicked_showsForm(provider: Provider) {
        // WHEN
        sut.newBillClicked(provider)

        // THEN
        verify(view).showNewBillForm(provider)
    }

    @Test
    fun aboutClicked_showsAbout() {
        // WHEN
        sut.aboutClicked()

        // THEN
        verify(view).showAbout()
    }

    @Test
    fun aboutClicked_closesDrawer() {
        // WHEN
        sut.aboutClicked()

        // THEN
        verify(view).closeDrawer()
    }
}