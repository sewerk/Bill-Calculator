package pl.srw.billcalculator.history

import android.view.MenuItem
import pl.srw.billcalculator.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuClickHandlerExtension @Inject constructor(private val generator: HistoryGenerator) {

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_generate) {
            generator.generatePgeG11Bills(10)
            return true
        }
        return false
    }
}