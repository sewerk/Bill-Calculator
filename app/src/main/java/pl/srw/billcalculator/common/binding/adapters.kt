package pl.srw.billcalculator.common.binding

import android.databinding.BindingAdapter
import android.databinding.adapters.ViewBindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ListView
import pl.srw.billcalculator.common.list.DataBindingAdapter

interface OnClick {
    fun invoke()
}

interface OnClickPosition {
    fun invoke(position: Int)
}

/**
 * A simplified alternative to the framework variants defined in [ViewBindingAdapter].
 *
 * Unlike the framework variant, an [View.OnClickListener] is not required and instead a no-args functional interface is used ([OnClick]).
 *
 * This results in simpler databinding expressions in xml.
 */
@BindingAdapter("onClick")
fun View.setOnClick(onClick: OnClick?) {
    onClick?.run {
        setOnClickListener { invoke() }
    }
}

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("onItemClick")
fun ListView.setOnItemClick(onClick: OnClickPosition) {
    setOnItemClickListener { _, _, position, _ -> onClick.invoke(position) }
}

@BindingAdapter("items")
fun <T> RecyclerView.setItems(items: List<T>) {
    with(adapter as DataBindingAdapter<T, *>) {
        replaceData(items)
    }
}
