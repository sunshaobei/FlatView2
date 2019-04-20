package sunsh.flatadapter.base

/**
 * Created by sunsh on 18/5/30.
 */
interface ItemViewDelegate<T> {

    fun getItemViewLayoutId(): Int

    fun isForViewType(item: T, position: Int): Boolean

    fun convert(holder: FlatViewHolder, t: T, position: Int)

}
