package pl.srw.billcalculator.settings

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.clearInvocations
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import pl.srw.billcalculator.data.settings.ProviderSettingsElement
import pl.srw.billcalculator.data.settings.SettingsRepo
import pl.srw.billcalculator.type.Provider

class SettingsVMTest {

    @get:Rule val rule: TestRule = InstantTaskExecutorRule()

    val provider = Provider.PGNIG
    val settingsRepo: SettingsRepo = mock {
        on { globalList() }.thenReturn(listOf(ProviderSettingsElement(provider), ProviderSettingsElement(Provider.PGE)))
    }
    val idx = 0 // index in list returned by repo
    val sut: SettingsVM = SettingsVM(settingsRepo)

    @Test
    fun `provides items given by settings repo`() {
        assert(sut.items.size == settingsRepo.globalList().size)
    }

    @Test
    fun `provides items with provider title, summary and icon`() {
        assert(sut.items[idx].titleRes == provider.settingsTitleRes)
        assert(sut.items[idx].summaryRes == provider.settingsDescRes)
        assert(sut.items[idx].logoRes == provider.logoSmallRes)
    }

    @Test
    fun `signal open settings details for provider when settings row clicked for phone`() {
        sut.isOnTablet = false
        val observer: Observer<Provider?> = mock()
        sut.openProviderSettings.observeForever(observer)

        sut.onRowClicked(idx)

        verify(observer).onChanged(provider)
    }

    @Test
    fun `signal switch settings tab when settings row clicked for tablet`() {
        sut.isOnTablet = true
        val observer: Observer<Provider?> = mock()
        sut.switchProviderTab.observeForever(observer)
        clearInvocations(observer)

        sut.onRowClicked(idx)

        verify(observer).onChanged(provider)
    }

    @Test
    fun `signals switch to first settings tab when on tablet`() {
        val observer: Observer<Provider?> = mock()
        sut.switchProviderTab.observeForever(observer)

        sut.isOnTablet = true

        verify(observer).onChanged(provider)
    }

    @Test
    fun `changes selected index when settings row clicked`() {
        val picked = 1

        sut.onRowClicked(picked)

        assert(sut.selectedIndex == picked)
    }
}
