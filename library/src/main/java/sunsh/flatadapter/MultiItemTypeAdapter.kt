package sunsh.flatadapter

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import sunsh.flatadapter.base.ItemViewDelegate
import sunsh.flatadapter.base.ItemViewDelegateManager
import sunsh.flatadapter.base.FlatViewHolder
import sunsh.flatadapter.base.FlatViewHolder.Companion


/**
 * Created by sunsh on 18/5/30.
 */
open class MultiItemTypeAdapter<T>(protected var mContext: Context, private var datas: List<T>) : RecyclerView.Adapter<FlatViewHolder>() {
    private var autoIncrementing: Int = 0
    private var decrementing: Int = 0

    private var mHeaderViews: SparseArrayCompat<View>? = null
    private var mFootViews: SparseArrayCompat<View>? = null

    protected var mItemViewDelegateManager: ItemViewDelegateManager<T?>
    protected var mOnItemClickListener: OnItemClickListener? = null
    protected var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var emptyView: View? = null
    private var loadingView: LoadingView? = null
    private var onLoadingListener: OnLoadingListener? = null
    private var loadingComplete = true
    private var enableLoading = false

    private val dataItemCount: Int
        get() = datas.size

    val headersCount: Int
        get() = if (mHeaderViews == null) 0 else mHeaderViews!!.size()

    val footersCount: Int
        get() = if (mFootViews == null) 0 else mFootViews!!.size()

    init {
        this.datas = datas
        mItemViewDelegateManager = ItemViewDelegateManager<T?>()
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderViewPos(position)) {
            return mHeaderViews!!.keyAt(position)
        }
        if (isFooterViewPos(position)) {
            return mFootViews!!.keyAt(position - headersCount - dataItemCount)
        }

        if (!useItemViewDelegateManager()) return super.getItemViewType(position)
        return if (position < datas.size)
            mItemViewDelegateManager.getItemViewType(datas[position], position)
        else
            mItemViewDelegateManager.getItemViewType(null, position)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlatViewHolder {
        if (headersCount > 0 || footersCount > 0) {
            var o: Any? = null
            if (headersCount > 0)
                o = mHeaderViews!!.get(viewType)
            if (o == null && footersCount > 0)
                o = mFootViews!!.get(viewType)
            if (o != null) {
                return FlatViewHolder.createViewHolder(parent.context, (o as View?)!!)
            }
        }
        val itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType)
        val layoutId = itemViewDelegate!!.getItemViewLayoutId()
        val holder = FlatViewHolder.createViewHolder(mContext, parent, layoutId)
        onViewHolderCreated(holder, holder.convertView)
        setListener(parent, holder, viewType)
        return holder
    }

    fun onViewHolderCreated(holder: FlatViewHolder, itemView: View) {

    }


    fun convert(holder: FlatViewHolder, t: T?) {
        mItemViewDelegateManager.convert(holder, t, holder.adapterPosition)
    }


    protected fun isEnabled(viewType: Int): Boolean {
        return true
    }


    protected fun setListener(parent: ViewGroup, viewHolder: FlatViewHolder, viewType: Int) {
        if (!isEnabled(viewType)) return
        viewHolder.convertView.setOnClickListener { v ->
            if (mOnItemClickListener != null) {
                val position = viewHolder.adapterPosition
                if (position < datas.size)
                    mOnItemClickListener!!.onItemClick(v, viewHolder, position, position - headersCount)
            }
        }

        viewHolder.convertView.setOnLongClickListener {
            if (mOnItemLongClickListener != null) {
                val position = viewHolder.adapterPosition
                if (position < datas.size)
                      mOnItemLongClickListener !!. onItemLongClick( it, viewHolder, position, position-headersCount)
                else
                    false
            }else{
                false
            }
        }
    }


    override fun onBindViewHolder(holder: FlatViewHolder, position: Int) {
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            if (isFooterViewPos(position)) {
                val key = mFootViews!!.keyAt(position - headersCount - dataItemCount)
                val o = mFootViews!!.get(key)
                if (o != null && o == getLoadingView().view && loadingComplete && getLoadingView().loadingType == LoadingType.LOADING) {
                    getLoadingView().view.postDelayed({
                        loadingComplete = false
                        onLoadingListener!!.onLoading(headersCount + dataItemCount)
                    }, 20)
                }
            }
            return
        }
        if (position - headersCount < datas.size) {
            convert(holder, datas[position - headersCount])
        } else {
            convert(holder, null)
        }
    }


    override fun getItemCount(): Int {
        val itemCount = datas.size
        if (itemCount == 0) {
            if (emptyView != null) {
                //                if (!enableLoading || getLoadingView().getLoadingType().equals(LoadingType.ERROR)) {
                //                    addFootView(emptyView);
                //                } else {
                //                    removeFootView(emptyView);
                //                }
                addFootView(emptyView!!)
            }
        } else {
            if (emptyView != null) removeFootView(emptyView!!)
            if (enableLoading && getLoadingView().loadingType != LoadingType.ERROR) {
                addLoadingView()
            } else {
                removeLoadingView()
            }
        }


        return itemCount + headersCount + footersCount
    }


    @JvmOverloads
    fun setLoadingComplete(noMore: Boolean = false) {
        this.loadingComplete = true
        if (noMore) {
            getLoadingView().loadingType = LoadingType.NO_MORE
        }
    }


    fun addHeaderView(view: View) {
        if (mHeaderViews == null) mHeaderViews = SparseArrayCompat()
        if (mHeaderViews!!.containsValue(view)) return
        autoIncrementing++
        mHeaderViews!!.put(autoIncrementing + BASE_ITEM_TYPE_HEADER, view)
    }


    fun removeHeaderView(v: View) {
        if (mHeaderViews == null) return
        val i = mHeaderViews!!.indexOfValue(v)
        if (i >= 0) {
            mHeaderViews!!.removeAt(i)
        }
    }

    fun addLoadingView() {
        addFootView(getLoadingView().view)
    }

    fun removeLoadingView() {
        if (loadingView != null)
            removeFootView(loadingView!!.view)
    }

    fun addFootView(view: View) {
        if (mFootViews == null) mFootViews = SparseArrayCompat()
        if (mFootViews!!.containsValue(view)) return
        decrementing--
        mFootViews!!.put(decrementing + BASE_ITEM_TYPE_FOOTER, view)
    }

    fun removeFootView(view: View) {
        if (mFootViews == null) return
        val i = mFootViews!!.indexOfValue(view)
        if (i >= 0)
            mFootViews!!.removeAt(i)
    }

    fun getLoadingView(): LoadingView {
        if (loadingView == null) loadingView = LoadingView(mContext)
        return loadingView as LoadingView
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val gridLayoutManager = layoutManager as GridLayoutManager?
            val spanSizeLookup = gridLayoutManager!!.spanSizeLookup

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isHeaderViewPos(position) || isFooterViewPos(position)) {
                        layoutManager.spanCount
                    } else spanSizeLookup?.getSpanSize(position) ?: 1
                }
            }
            gridLayoutManager.spanCount = gridLayoutManager.spanCount
        }
    }


    private fun isHeaderViewPos(position: Int): Boolean {
        return mHeaderViews != null && position < headersCount
    }


    private fun isFooterViewPos(position: Int): Boolean {
        return mFootViews != null && position >= headersCount + dataItemCount
    }


    fun addItemViewDelegate(itemViewDelegate: ItemViewDelegate<T?>): MultiItemTypeAdapter<*> {
        mItemViewDelegateManager.addDelegate(itemViewDelegate)
        return this
    }

    fun addItemViewDelegate(viewType: Int, itemViewDelegate: ItemViewDelegate<T?>): MultiItemTypeAdapter<*> {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate)
        return this
    }

    protected fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.itemViewDelegateCount > 0
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, holder: FlatViewHolder, position: Int, dataPosition: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int, dataPosition: Int): Boolean
    }


    interface OnLoadingListener {
        fun onLoading(lastPosition: Int)
    }

    fun setOnLoadingListener(o: OnLoadingListener) {
        this.onLoadingListener = o
        setEnableLoading(true)
        getLoadingView().loadingType = LoadingType.LOADING
    }

    fun setEnableLoading(b: Boolean) {
        enableLoading = b
        //        notifyDataSetChanged();
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    fun setEmptyView(text: String, resId: Int, width: Int, height: Int) {
        emptyView = LayoutInflater.from(mContext).inflate(R.layout.rv_empty, null)
        val tv_empty = emptyView!!.findViewById<TextView>(R.id.tv_empty)
        val iv_empty = emptyView!!.findViewById<ImageView>(R.id.iv_empty)
        if (!TextUtils.isEmpty(text))
            tv_empty.text = text
        if (resId > 0)
            iv_empty.setImageResource(resId)
        var layoutParams: ViewGroup.LayoutParams? = emptyView!!.layoutParams
        if (layoutParams == null)
            layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        if (width > 0) layoutParams.width = width
        if (height > 0) layoutParams.height = height
        emptyView!!.layoutParams = layoutParams
    }

    fun setEmptyView(text: String, resid: Int) {
        setEmptyView(text, resid, 0, 0)
    }

    companion object {
        private val BASE_ITEM_TYPE_HEADER = 100000
        private val BASE_ITEM_TYPE_FOOTER = 200000
    }
}
