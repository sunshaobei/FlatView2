package sunsh.flatadapter.utils

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup

/**
 * Created by sunsh on 18/5/30.
 */
object WrapperUtils {
    interface SpanSizeCallback {
        fun getSpanSize(layoutManager: GridLayoutManager, oldLookup: GridLayoutManager.SpanSizeLookup, position: Int): Int
    }

    fun onAttachedToRecyclerView(innerAdapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView, callback: SpanSizeCallback) {
        innerAdapter.onAttachedToRecyclerView(recyclerView)

        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val gridLayoutManager = layoutManager as GridLayoutManager?
            val spanSizeLookup = gridLayoutManager!!.spanSizeLookup

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position)
                }
            }
            gridLayoutManager.spanCount = gridLayoutManager.spanCount
        }
    }

    fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val lp = holder.itemView.layoutParams

        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {

            lp.isFullSpan = true
        }
    }
}
