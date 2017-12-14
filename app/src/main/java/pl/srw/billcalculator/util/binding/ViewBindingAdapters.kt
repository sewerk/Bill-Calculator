package pl.srw.billcalculator.util.binding

import android.databinding.BindingAdapter
import android.databinding.adapters.ViewBindingAdapter
import android.view.View

private const val BIND_ON_CLICK = "onClick"

interface OnClick {
    fun invoke()
}

/**
 * A simplified alternative to the framework variants defined in [ViewBindingAdapter].
 *
 * Unlike the framework variant, an [View.OnClickListener] is not required and instead a no-args functional interface is used ([OnClick]).
 *
 * This results in simpler databinding expressions in xml.
 */
@BindingAdapter(value = [BIND_ON_CLICK])
fun View.setOnClick(onClick: OnClick?) {

    onClick?.run {
        setOnClickListener { invoke() }
    }
}