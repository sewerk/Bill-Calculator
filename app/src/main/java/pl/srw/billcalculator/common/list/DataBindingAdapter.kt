package pl.srw.billcalculator.common.list

import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import timber.log.Timber

abstract class DataBindingAdapter <T, V : ViewDataBinding> : RecyclerView.Adapter<DataBindingVH<V>>() {

    var items: List<T> = emptyList()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingVH<V> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = createBinding(inflater, parent)
        return DataBindingVH(binding)
    }

    override fun onBindViewHolder(holder: DataBindingVH<V>, position: Int) {
        bind(holder.binding, items[position])
        holder.binding.executePendingBindings()
    }

    fun replaceData(update: List<T>) {
        Timber.d("Replacing data")
        if (update.isEmpty()) throw IllegalArgumentException("New list cannot be empty")
        else if (items.isEmpty() && update.isNotEmpty()) {
            items = update.copy()
            notifyDataSetChanged()
        }
        else calculateDiffAsnc(update)
    }

    override fun getItemCount() = items.size

    protected abstract fun createBinding(inflater: LayoutInflater, parent: ViewGroup): V

    protected abstract fun bind(binding: V, item: T)

    protected abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    protected abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    private fun calculateDiffAsnc(update: List<T>) {
        async(UI) {
            val diffResult = bg { calculateDiff(items, update) }
            diffResult.await().run {
                items = update.copy()
                dispatchUpdatesTo(this@DataBindingAdapter)
            }
        }
    }

    @SuppressWarnings("ReturnCount")
    private fun calculateDiff(oldItems: List<T>, update: List<T>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return update.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldItems[oldItemPosition]
                val newItem = update[newItemPosition]
                return areItemsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldItems[oldItemPosition]
                val newItem = update[newItemPosition]
                return areContentsTheSame(oldItem, newItem)
            }
        })
    }
}

private fun <E> List<E>.copy(): List<E> = this.toList()
