package com.example.finaldateset

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(
    private val topSpace: Int,
    private val bottomSpace: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position == 0) {
            outRect.top = topSpace
        } else {
            outRect.top = 0
        }

        if (position == itemCount - 1) {
            outRect.bottom = bottomSpace
        } else {
            outRect.bottom = 0
        }
    }
}
