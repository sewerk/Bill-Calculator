package pl.srw.billcalculator.history

import android.view.MenuItem
import pl.srw.billcalculator.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuClickHandlerExtension @Inject constructor() {

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        // does nothing
        return false
    }
}