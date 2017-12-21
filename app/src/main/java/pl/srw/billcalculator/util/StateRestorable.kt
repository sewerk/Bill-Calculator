package pl.srw.billcalculator.util

import android.os.Bundle

interface StateRestorable {
    fun writeTo(bundle: Bundle)
    fun readFrom(bundle: Bundle)
}
