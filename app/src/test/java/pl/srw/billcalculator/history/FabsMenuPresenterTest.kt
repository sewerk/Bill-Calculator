package pl.srw.billcalculator.history

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.verify
import pl.srw.billcalculator.type.Provider

class FabsMenuPresenterTest {

    val view: FabsMenuPresenter.View = mock()

    val sut = FabsMenuPresenter(view)

    @Test
    fun `expand fabs when collapsed and base fab button clicked`() {
        whenever(view.isExpanded()).thenReturn(false)

        sut.baseFabClicked()

        verify(view).expand()
        verify(view).showDim()
    }

    @Test
    fun `expand fabs when expanded but collapse in progress when base fab button clicked`() {
        whenever(view.isExpanded()).thenReturn(true)
        whenever(view.isCollapsingInProgress()).thenReturn(true)

        sut.baseFabClicked()

        verify(view).expand()
        verify(view).showDim()
    }

    @Test
    fun `collapse fabs when expanded and base fab clicked`() {
        whenever(view.isExpanded()).thenReturn(true)

        sut.baseFabClicked()

        verify(view).collapse()
        verify(view).hideDim()
    }

    @Test
    fun `open form when fab provider clicked`() {
        val provider = Provider.PGE

        sut.fabClicked(provider)

        verify(view).openForm(provider)
    }

    @Test
    fun `collapse fabs when fab provider clicked`() {
        val provider = Provider.PGNIG

        sut.fabClicked(provider)

        verify(view).collapse()
        verify(view).hideDim()
    }

    @Test
    fun `collapse fabs when click outside fabs`() {
        sut.outsideClicked()

        verify(view).collapse()
        verify(view).hideDim()
    }

    @Test
    fun `handle back pressed with collapsing if fabs expanded`() {
        whenever(view.isExpanded()).thenReturn(true)

        val result = sut.handleBackPressed()

        verify(view).collapse()
        verify(view).hideDim()
        assertTrue(result)
    }

    @Test
    fun `dont handle back pressed when fabs not expanded`() {
        val result = sut.handleBackPressed()

        verify(view, never()).collapse()
        verify(view, never()).hideDim()
        assertFalse(result)
    }

    @Test
    fun `dont handle back pressed when fabs expanded but collapse is in progres`() {
        whenever(view.isExpanded()).thenReturn(true)
        whenever(view.isCollapsingInProgress()).thenReturn(true)

        val result = sut.handleBackPressed()

        verify(view, never()).collapse()
        verify(view, never()).hideDim()
        assertFalse(result)
    }
}
