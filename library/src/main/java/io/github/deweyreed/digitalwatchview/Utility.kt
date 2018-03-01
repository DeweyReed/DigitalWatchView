package io.github.deweyreed.digitalwatchview

import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View

/**
 * Created on 2018/2/27.
 */

internal fun Float.clamp(min: Float, max: Float) = Math.max(min, Math.min(max, this))

@ColorInt
internal fun View.color(@ColorRes color: Int): Int = ContextCompat.getColor(context, color)

internal fun View.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

internal fun View.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

internal fun View.gone() {
    visibility = View.GONE
}

internal fun View.show() {
    visibility = View.VISIBLE
}
