package sunsh.flatadapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class FlatView : RecyclerView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(context,attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context,attrs)
    }

    private fun init(context: Context,attrs:AttributeSet?) {
        if (attrs!=null){
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlatView)
            val orientation = typedArray.getInt(R.styleable.FlatView_orientation,0)
            val column = typedArray.getInt(R.styleable.FlatView_column,1)
            if (column==1){
                layoutManager = LinearLayoutManager(context,orientation,false)
            }else{
                layoutManager = GridLayoutManager(context,column,orientation,false)
            }
            typedArray.recycle()
        }
        initAdapter()
    }

    private fun initAdapter() {
        if (layoutManager==null){
            layoutManager = LinearLayoutManager(context)
        }

    }
}
