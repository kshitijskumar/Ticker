package com.example.ticker.core.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ZoomCenterItemLayoutManager(private val context: Context) : LinearLayoutManager(context) {

    private val shrinkAmount = 0.8f
    private val shrinkDistance = 0.9f

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val scrolled = super.scrollVerticallyBy(dy, recycler, state)

        val midPoint = height / 2f
        val d0 = 0f
        val d1 = shrinkDistance * midPoint
        val s0 = 1f
        val s1 = 1f - shrinkAmount

        for (i in 0 until childCount) {
            getChildAt(i)?.let { childView ->
                val childMidPoint = (getDecoratedTop(childView) + getDecoratedBottom(childView)) / 2f
                val distance = kotlin.math.min(d1, kotlin.math.abs(midPoint - childMidPoint))
                val scale = s0 + (s1 - s0) * (distance - d0) / (d1 - d0)
                childView.scaleX = scale
                childView.scaleY = scale
            }
        }

        return scrolled
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        scrollVerticallyBy(0, recycler, state)
    }
}
