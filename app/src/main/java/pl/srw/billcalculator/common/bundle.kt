package pl.srw.billcalculator.common

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

fun bundleOf(key: String, value: String): Bundle {
    val bundle = Bundle()
    bundle.putString(key, value)
    return bundle
}

fun bundleOf(key: String, value: Serializable): Bundle {
    val bundle = Bundle()
    bundle.putSerializable(key, value)
    return bundle
}

fun bundleOf(key: String, value: Parcelable): Bundle {
    val bundle = Bundle()
    bundle.putParcelable(key, value)
    return bundle
}

fun bundleOf(puts: Bundle.() -> Unit): Bundle {
    val bundle = Bundle()
    puts(bundle)
    return bundle
}
