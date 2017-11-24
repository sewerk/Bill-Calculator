package pl.srw.billcalculator.form

import android.arch.lifecycle.ViewModel
import pl.srw.billcalculator.util.Dates

class FormVM : ViewModel() {

    var fromDate = Dates.firstDayOfThisMonth()
    var toDate = Dates.lastDayOfThisMonth()
}