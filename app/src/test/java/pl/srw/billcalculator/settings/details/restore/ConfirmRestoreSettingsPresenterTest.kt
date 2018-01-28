package pl.srw.billcalculator.settings.details.restore

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import org.junit.Before
import org.junit.Test
import pl.srw.billcalculator.data.settings.prices.PricesRepo
import pl.srw.billcalculator.type.Provider

class ConfirmRestoreSettingsPresenterTest {

    val provider = Provider.PGNIG
    val pricesRepo: PricesRepo = mock()
    val sut = ConfirmRestoreSettingsPresenter(pricesRepo)

    @Before
    fun setUp() {
        sut.setup(provider)
    }

    @Test
    fun `set default prices on confirm restore`() {
        sut.onConfirmClicked()

        verify(pricesRepo).setDefaultPricesFor(provider)
    }

    @Test
    fun `don't change prices on restore canceled`() {
        sut.onCancelClicked()

        verifyZeroInteractions(pricesRepo)
    }
}
