package pl.srw.billcalculator.history

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
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
    fun `expand fabs when expanded but collpse in progress when base fab button clicked`() {
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
}