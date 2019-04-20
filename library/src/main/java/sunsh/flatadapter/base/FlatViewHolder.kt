package sunsh.flatadapter.base

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.util.Linkify
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Checkable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import com.android.databinding.library.R

/**
 * Created by sunsh on 18/5/30.
 */
class FlatViewHolder(private val mContext: Context, val convertView: View) : RecyclerView.ViewHolder(convertView) {
    private val mViews: SparseArray<View>
    private var binding: ViewDataBinding? = null


    init {
        if (convertView.getTag(R.id.dataBinding)!=null){
            binding = DataBindingUtil.bind(convertView)
        }
        mViews = SparseArray()
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    fun <T : View> getView(viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }


    /****以下为辅助方法 */

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    fun setText(viewId: Int, text: String): FlatViewHolder {
        val tv = getView<TextView>(viewId)
        tv.text = text
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): FlatViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageResource(resId)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap): FlatViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable): FlatViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): FlatViewHolder {
        val view = getView<View>(viewId)
        view.setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(viewId: Int, backgroundRes: Int): FlatViewHolder {
        val view = getView<View>(viewId)
        view.setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): FlatViewHolder {
        val view = getView<TextView>(viewId)
        view.setTextColor(textColor)
        return this
    }

    fun setTextColorRes(viewId: Int, textColorRes: Int): FlatViewHolder {
        val view = getView<TextView>(viewId)
        view.setTextColor(mContext.resources.getColor(textColorRes))
        return this
    }

    @SuppressLint("NewApi")
    fun setAlpha(viewId: Int, value: Float): FlatViewHolder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId).alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId).startAnimation(alpha)
        }
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): FlatViewHolder {
        val view = getView<View>(viewId)
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun linkify(viewId: Int): FlatViewHolder {
        val view = getView<TextView>(viewId)
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    fun setTypeface(typeface: Typeface, vararg viewIds: Int): FlatViewHolder {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): FlatViewHolder {
        val view = getView<ProgressBar>(viewId)
        view.progress = progress
        return this
    }

    fun setProgress(viewId: Int, progress: Int, max: Int): FlatViewHolder {
        val view = getView<ProgressBar>(viewId)
        view.max = max
        view.progress = progress
        return this
    }

    fun setMax(viewId: Int, max: Int): FlatViewHolder {
        val view = getView<ProgressBar>(viewId)
        view.max = max
        return this
    }

    fun setRating(viewId: Int, rating: Float): FlatViewHolder {
        val view = getView<RatingBar>(viewId)
        view.rating = rating
        return this
    }

    fun setRating(viewId: Int, rating: Float, max: Int): FlatViewHolder {
        val view = getView<RatingBar>(viewId)
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any): FlatViewHolder {
        val view = getView<View>(viewId)
        view.tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any): FlatViewHolder {
        val view = getView<View>(viewId)
        view.setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): FlatViewHolder {
        val view = getView<View>(viewId) as Checkable
        view.isChecked = checked
        return this
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(viewId: Int,
                           listener: View.OnClickListener): FlatViewHolder {
        val view = getView<View>(viewId)
        view.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(viewId: Int,
                           listener: View.OnTouchListener): FlatViewHolder {
        val view = getView<View>(viewId)
        view.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(viewId: Int,
                               listener: View.OnLongClickListener): FlatViewHolder {
        val view = getView<View>(viewId)
        view.setOnLongClickListener(listener)
        return this
    }

    fun <T : ViewDataBinding> getBinding(): T {
        return binding as T
    }

    companion object {


        fun createViewHolder(context: Context, itemView: View): FlatViewHolder {
            return FlatViewHolder(context, itemView)
        }

        fun createViewHolder(context: Context,
                             parent: ViewGroup, layoutId: Int): FlatViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                    false)
            return FlatViewHolder(context, itemView)
        }
    }


}
