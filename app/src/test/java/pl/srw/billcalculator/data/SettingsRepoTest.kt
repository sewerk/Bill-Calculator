package pl.srw.billcalculator.data

import org.junit.Test
import pl.srw.billcalculator.data.settings.SettingsRepo
import pl.srw.billcalculator.type.Provider

class SettingsRepoTest {

    val sut = SettingsRepo()

    @Test
    fun `global settings list contains all Providers`() {
        assert(sut.globalList().size == Provider.values().size)
    }
}