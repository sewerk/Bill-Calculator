package pl.srw.billcalculator.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import timber.log.Timber

abstract class BackableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        Timber.i("Back pressed")
        super.onBackPressed()
    }
}
