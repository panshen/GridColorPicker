package com.panshen.gridcolorpicker

import android.view.View
import android.view.ViewGroup

fun View.defaultWidthMeasureSpec(parentView: ViewGroup): Int {
    return when (layoutParams.width) {
        ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredWidth.toExactlyMeasureSpec()
        ViewGroup.LayoutParams.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT.toAtMostMeasureSpec()
        0 -> throw IllegalAccessException("Need special treatment for $this")
        else -> layoutParams.width.toExactlyMeasureSpec()
    }
}

fun View.defaultHeightMeasureSpec(parentView: ViewGroup): Int {
    return when (layoutParams.height) {
        ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredWidth.toExactlyMeasureSpec()
        ViewGroup.LayoutParams.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT.toAtMostMeasureSpec()
        0 -> throw IllegalAccessException("Need special treatment for $this")
        else -> layoutParams.height.toExactlyMeasureSpec()
    }
}

fun Int.toExactlyMeasureSpec(): Int {
    return View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.EXACTLY)
}

fun Int.toAtMostMeasureSpec(): Int {
    return View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.AT_MOST)
}