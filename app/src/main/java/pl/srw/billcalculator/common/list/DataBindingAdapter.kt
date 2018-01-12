package pl.srw.billcalculator.common.list

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class DataBindingAdapter <T, V : ViewDataBinding> constructor(var items: List<T> = emptyList())
    : RecyclerView.Adapter<DataBindingVH<V>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingVH<V> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = createBinding(inflater, parent)
        return DataBindingVH(binding)
    }

    override fun onBindViewHolder(holder: DataBindingVH<V>, position: Int) {
        bind(holder.binding, items[position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount() = items.size

    protected abstract fun createBinding(inflater: LayoutInflater, parent: ViewGroup): V

    protected abstract fun bind(binding: V, item: T)
}
