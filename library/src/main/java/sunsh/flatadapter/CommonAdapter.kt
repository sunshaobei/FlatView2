package sunsh.flatadapter

import android.content.Context

import sunsh.flatadapter.base.ItemViewDelegate
import sunsh.flatadapter.base.FlatViewHolder

/**
 * Created by sunsh on 18/5/30.
 */
abstract class CommonAdapter<T>(var context: Context, protected var mLayoutId: Int, protected var list: List<T>) : MultiItemTypeAdapter<T>(context, list) {
    init {
        this.addItemViewDelegate(object : ItemViewDelegate<T?> {
            override fun getItemViewLayoutId(): Int {
                return mLayoutId
            }
            override fun isForViewType(item: T?, position: Int): Boolean {
                return list.isNotEmpty()
            }
            override fun convert(holder: FlatViewHolder, t: T?, position: Int) {
                this@CommonAdapter.convert(holder, t, position)
            }
        })
    }
    protected abstract fun convert(holder: FlatViewHolder, t: T?, position: Int)


}
