package com.pager.teamapp.ui

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.pager.teamapp.R

class TeamItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect?.top = view?.resources?.getDimensionPixelOffset(R.dimen.half_margin) ?: 0
        outRect?.bottom = view?.resources?.getDimensionPixelOffset(R.dimen.half_margin) ?: 0
    }
}