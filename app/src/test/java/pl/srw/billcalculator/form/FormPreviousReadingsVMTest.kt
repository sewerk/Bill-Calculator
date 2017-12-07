package pl.srw.billcalculator.form

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import pl.srw.billcalculator.RxJavaBaseTest
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.wrapper.PricesRepo
import pl.srw.billcalculator.wrapper.ReadingsRepo

@RunWith(JUnitParamsRunner::class)
class FormPreviousReadingsVMTest : RxJavaBaseTest() {

    @get:Rule val rule: TestRule = InstantTaskExecutorRule()

    val readings = intArrayOf(1, 2, 3)
    val readingsRepo: ReadingsRepo = mock {
        on { getPreviousReadingsFor(any()) } doReturn Single.just(readings)
    }
    val tariffLiveData = MutableLiveData<String>()
    val pricesRepo: PricesRepo = mock {
        on { tariffPge } doReturn tariffLiveData
        on { tariffTauron } doReturn tariffLiveData
    }
    val testObserver: Observer<IntArray> = mock()
    
    lateinit var sut: FormPreviousReadingsVM

    @Test
    @Parameters("PGNIG", "PGE", "TAURON")
    fun `fetches single previous readings for PGNIG or G11 tariff`(provider: Provider) {
        setTariff(SharedPreferencesEnergyPrices.TARIFF_G11)

        init(provider)

        sut.singlePrevReadings.observeForever(testObserver)
        verify(testObserver).onChanged(readings)
    }

    @Test
    @Parameters("PGNIG", "PGE", "TAURON")
    fun `does not fetch double previous readings for PGNIG or G11 tariff`(provider: Provider) {
        setTariff(SharedPreferencesEnergyPrices.TARIFF_G11)

        init(provider)

        sut.dayPrevReadings.observeForever(testObserver)
        sut.nightPrevReadings.observeForever(testObserver)
        verify(testObserver, never()).onChanged(readings)
    }

    @Test
    @Parameters("PGE", "TAURON")
    fun `fetches double previous readings for G12 tariff`(provider: Provider) {
        setTariff(SharedPreferencesEnergyPrices.TARIFF_G12)

        init(provider)

        sut.dayPrevReadings.observeForever(testObserver)
        sut.nightPrevReadings.observeForever(testObserver)
        verify(testObserver, times(2)).onChanged(readings)
    }

    @Test
    @Parameters("PGE", "TAURON")
    fun `does not fetch single previous readings for G12 tariff`(provider: Provider) {
        setTariff(SharedPreferencesEnergyPrices.TARIFF_G12)

        init(provider)

        sut.singlePrevReadings.observeForever(testObserver)
        verify(testObserver, never()).onChanged(readings)
    }

    @Test
    @Parameters("PGE", "TAURON")
    fun `fetch previous readings when tariff changes`(provider: Provider) {
        setTariff(SharedPreferencesEnergyPrices.TARIFF_G11)
        init(provider)
        sut.dayPrevReadings.observeForever(testObserver)
        sut.nightPrevReadings.observeForever(testObserver)

        clearInvocations(testObserver, readingsRepo)
        setTariff(SharedPreferencesEnergyPrices.TARIFF_G12)

        verify(readingsRepo, times(2)).getPreviousReadingsFor(any())
        verify(testObserver, times(2)).onChanged(readings)
    }

    @Test
    @Parameters("PGE", "TAURON")
    fun `does not re-fetch previous readings but notifies cached value, when view re-attached`(provider: Provider) {
        setTariff(SharedPreferencesEnergyPrices.TARIFF_G11)
        init(provider)
        sut.singlePrevReadings.observeForever(testObserver)

        clearInvocations(readingsRepo)
        val afterReAttachObserver: Observer<IntArray> = mock()
        sut.singlePrevReadings.observeForever(afterReAttachObserver)

        verify(readingsRepo, never()).getPreviousReadingsFor(any())
        verify(afterReAttachObserver).onChanged(readings)
    }

    private fun setTariff(@SharedPreferencesEnergyPrices.TariffOption tariff: String) {
        tariffLiveData.value = tariff
        whenever(pricesRepo.getTariff(any())).thenReturn(tariff)
        waitToFinish() // tariff change after observing trigger prev reading fetch
    }

    private fun init(provider: Provider) {
        sut = FormPreviousReadingsVM(provider, readingsRepo, pricesRepo)
        waitToFinish() // constructor is triggering prev readings fetch
    }
}