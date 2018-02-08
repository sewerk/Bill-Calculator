package pl.srw.billcalculator.data.settings

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.srw.billcalculator.type.Provider

class SettingsRepoTest {

    val sut = SettingsRepo()

    @Test
    fun `global settings list contains all Providers`() {
        assertEquals(sut.globalList().size, Provider.values().size)
    }
}
