package pl.srw.billcalculator.history

import android.view.MenuItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuClickHandlerExtension @Inject constructor() {

    @Suppress("UNUSED_PARAMETER")
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        // does nothing
        return false
    }
}
