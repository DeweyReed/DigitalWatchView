package io.github.deweyreed.digitalwatchview

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import android.view.View

/**
 * Created on 2018/2/27.
 */

internal fun Float.clamp(min: Float, max: Float) = Math.max(min, Math.min(max, this))

@ColorInt
internal fun View.color(@ColorRes color: Int): Int = ContextCompat.getColor(context, color)

internal fun View.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

internal fun View.gone() {
    visibility = View.GONE
}

internal fun View.show() {
    visibility = View.VISIBLE
}

/**
 * [ResourcesCompat.getFont] doesn't check API version, resulting in accessing hidden API.
 */
internal fun Context.getFontCompat(@FontRes fontRes: Int): Typeface? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        resources.getFont(fontRes)
    } else {
        ResourcesCompat.getFont(this, fontRes)
    }
}