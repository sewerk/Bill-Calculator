package pl.srw.billcalculator.util.binding

import android.databinding.BindingAdapter
import android.databinding.adapters.ViewBindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ListView
import pl.srw.billcalculator.common.list.DataBindingAdapter

private const val BIND_ON_CLICK = "onClick"
private const val BIND_ON_ITEM_CLICK = "onItemClick"
private const val BIND_LIST_ITEMS = "items"

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
@BindingAdapter(BIND_ON_CLICK)
fun View.setOnClick(onClick: OnClick?) {
    onClick?.run {
        setOnClickListener { invoke() }
    }
}

@BindingAdapter(BIND_ON_ITEM_CLICK)
fun ListView.setOnItemClick(onClick: OnClickPosition) {
    setOnItemClickListener { _, _, position, _ -> onClick.invoke(position) }
}

@BindingAdapter(BIND_LIST_ITEMS)
fun <T> RecyclerView.setItems(items: List<T>) {
    with(adapter as DataBindingAdapter<T, *>) {
        replaceData(items)
    }
}
