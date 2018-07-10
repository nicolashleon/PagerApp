package com.pager.teamapp.ui.delegates

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

import com.pager.teamapp.ui.models.DelegateUIModel


/**
 * Code adapted from https://github.com/sockeqwe/AdapterDelegates
 * http://hannesdorfmann.com/android/adapter-delegates
 */
interface DelegateAdapter<VH : RecyclerView.ViewHolder, UIModel : DelegateUIModel> {

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    fun onBindViewHolder(viewHolder: VH, uiModel: UIModel)

}
