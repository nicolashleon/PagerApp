package com.pager.teamapp.ui.adapters

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.NO_POSITION
import android.util.SparseArray
import android.view.ViewGroup
import com.pager.teamapp.ui.delegates.DelegateAdapter
import com.pager.teamapp.ui.models.DelegateUIModel
import java.util.*

/**
 * Code adapted from https://github.com/sockeqwe/AdapterDelegates
 * http://hannesdorfmann.com/android/adapter-delegates
 */
abstract class BaseAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected var delegateAdapters = SparseArray<DelegateAdapter<RecyclerView.ViewHolder, DelegateUIModel>>()
    protected var delegateUIModels: MutableList<DelegateUIModel> = ArrayList()

    val isEmpty: Boolean get() = delegateUIModels.isEmpty()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val delegateAdapter = delegateAdapters.get(viewType)
        return (delegateAdapter?.onCreateViewHolder(parent, viewType) as VH?)!!
    }

    override fun getItemViewType(position: Int): Int {
        return delegateUIModels[position].viewType
    }

    override fun getItemCount(): Int {
        return delegateUIModels.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val delegateAdapter = delegateAdapters.get(getItemViewType(position))
        delegateAdapter?.onBindViewHolder(holder, delegateUIModels[position])
    }

    fun addDelegate(delegateAdapter: DelegateAdapter<RecyclerView.ViewHolder, DelegateUIModel>, viewType: Int) {
        delegateAdapters.put(viewType, delegateAdapter)
    }

    fun <T : DelegateUIModel> getItem(pos: Int): T {
        return delegateUIModels[pos] as T
    }

    fun removeItemsAndNotify(uiModel: DelegateUIModel) {
        val posToRemove = delegateUIModels.indexOf(uiModel)
        if (posToRemove != NO_POSITION) {
            delegateUIModels.removeAt(posToRemove)
            notifyItemRemoved(posToRemove)
        }
    }

    fun clearItems() {
        delegateUIModels.clear()
    }

    fun addAllItemsAndNotify(uiModels: List<DelegateUIModel>) {
        val size = delegateUIModels.size
        delegateUIModels.addAll(uiModels)
        notifyItemRangeInserted(size, uiModels.size)
    }

    fun addItemAndNotify(uiModel: DelegateUIModel) {
        val size = delegateUIModels.size
        delegateUIModels.add(uiModel)
        notifyItemInserted(size)
    }

    fun clearItemsAndNotify() {
        delegateUIModels.clear()
        notifyDataSetChanged()
    }

}
