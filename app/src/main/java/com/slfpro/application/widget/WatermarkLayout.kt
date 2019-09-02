package com.slfpro.application.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.slfpro.application.R
import kotlinx.android.synthetic.main.layout_watermark.view.*

/**
 * 水印view
 */
class WatermarkLayout : ConstraintLayout {

    private var mContext: Context

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
            : super(context, attributeSet, defStyleAttr) {
        mContext = context
        inflate(mContext, R.layout.layout_watermark, this)
    }

    fun getTvName(): TextView {
        return tvName
    }

    fun getTvTime(): TextView {
        return tvTime
    }

    fun getTvCoordinate(): TextView {
        return tvCoordinate
    }

}