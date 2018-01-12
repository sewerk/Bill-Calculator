package pl.srw.billcalculator.common.list

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class DataBindingVH <out T : ViewDataBinding> constructor(val binding: T) : RecyclerView.ViewHolder(binding.root)
