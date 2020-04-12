package io.github.deweyreed.digitalwatchview

import android.content.Context
import android.graphics.Color
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.FontRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.max

@Suppress("unused", "MemberVisibilityCanBePrivate")
/**
 * Created on 2018/2/26.
 */

class DigitalWatchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val backgroundView: DigitalView
    private val foregroundView: DigitalView

    private var showBackground = false

    @ColorInt
    private var backgroundColorInt = -1
    private var backgroundAlpha = 1f

    @ColorInt
    private var foregroundColorInt = -1
    private var normalTextSize = 1f
    private var showSeconds = true
    private var secondsTextSize = 1f
    private var showHours = true
    private var showTwoDigits = true
    private var hours = 0
    private var minutes = 0
    private var seconds = 0

    private var blinkColons: Boolean
    private var blinkRunnable: BlinkRunnable? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.DigitalWatchView, 0, 0)
        showBackground = ta.getBoolean(
            R.styleable.DigitalWatchView_dwv_show_background,
            false
        )
        backgroundColorInt = ta.getColor(
            R.styleable.DigitalWatchView_dwv_background_color,
            color(android.R.color.darker_gray)
        )
        backgroundAlpha = ta.getFloat(
            R.styleable.DigitalWatchView_dwv_background_alpha,
            1f
        )
        foregroundColorInt = ta.getColor(
            R.styleable.DigitalWatchView_dwv_foreground_color,
            Color.BLACK
        )
        normalTextSize = ta.getDimension(
            R.styleable.DigitalWatchView_dwv_normal_text_size,
            sp(18).toFloat()
        )
        showSeconds = ta.getBoolean(
            R.styleable.DigitalWatchView_dwv_show_seconds,
            true
        )
        secondsTextSize = ta.getDimension(
            R.styleable.DigitalWatchView_dwv_seconds_text_size,
            sp(18).toFloat()
        )
        showHours = ta.getBoolean(
            R.styleable.DigitalWatchView_dwv_show_hours,
            true
        )
        showTwoDigits = ta.getBoolean(
            R.styleable.DigitalWatchView_dwv_show_two_digits,
            true
        )
        hours = ta.getInteger(R.styleable.DigitalWatchView_dwv_hours, 0)
        minutes = ta.getInteger(R.styleable.DigitalWatchView_dwv_minutes, 0)
        seconds = ta.getInteger(R.styleable.DigitalWatchView_dwv_seconds, 0)
        blinkColons = ta.getBoolean(R.styleable.DigitalWatchView_dwv_blink_colons, false)
        ta.recycle()

        backgroundView = DigitalView(
            context, backgroundColorInt, normalTextSize,
            showSeconds, secondsTextSize, showHours, showTwoDigits,
            88, 88, 88
        ).apply {
            if (!showBackground) gone()
            alpha = backgroundAlpha.clamp(0f, 1f)
        }
        foregroundView = DigitalView(
            context, foregroundColorInt, normalTextSize,
            showSeconds, secondsTextSize, showHours, showTwoDigits,
            hours, minutes, seconds
        )

        addView(backgroundView)
        addView(foregroundView)

        if (blinkColons) setBlinkColons(true)
    }

    override fun onDetachedFromWindow() {
        stopBlinking()
        super.onDetachedFromWindow()
    }

    fun getShowBackground() = showBackground
    fun setShowBackground(show: Boolean) {
        showBackground = show
        if (show) backgroundView.show() else backgroundView.gone()
    }

    @ColorInt
    fun getBackgroundViewColorInt() = backgroundColorInt

    fun setBackgroundViewColorInt(@ColorInt color: Int) {
        backgroundColorInt = color
        backgroundView.setColorInt(color)
    }

    fun getBackgroundViewAlpha() = backgroundAlpha
    fun setBackgroundViewAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        backgroundAlpha = alpha
        backgroundView.alpha = alpha.clamp(0f, 1f)
    }

    @ColorInt
    fun getForegroundViewColorInt() = foregroundColorInt

    fun setForegroundViewColorInt(@ColorInt color: Int) {
        foregroundColorInt = color
        foregroundView.setColorInt(color)
    }

    fun getNormalTextSize() = normalTextSize
    fun setNormalTextSize(textSize: Float) {
        normalTextSize = textSize
        backgroundView.setNormalTextSize(textSize)
        foregroundView.setNormalTextSize(textSize)
    }

    fun getShowSeconds() = showSeconds
    fun setShowSeconds(show: Boolean) {
        showSeconds = show
        backgroundView.setShowSeconds(show)
        foregroundView.setShowSeconds(show)
    }

    fun getSecondsTextSize() = secondsTextSize
    fun setSecondsTextSize(textSize: Float) {
        secondsTextSize = textSize
        backgroundView.setSecondsTextSize(textSize)
        foregroundView.setSecondsTextSize(textSize)
    }

    fun getShowHours() = showHours
    fun setShowHours(show: Boolean) {
        showHours = show
        backgroundView.setShowHours(show)
        foregroundView.setShowHours(show)
    }

    fun getShowTwoDigits() = showTwoDigits
    fun setShowTwoDigits(show: Boolean) {
        showTwoDigits = show
        foregroundView.setShowTwoDigits(show)
        setTime(hours, minutes, seconds)
    }

    fun setTime(h: Int, m: Int, s: Int) {
        foregroundView.setTime(h, m, s)
    }

    fun getHours(): Int = hours
    fun setHours(h: Int) {
        hours = h
        foregroundView.setHoursOnly(h)
    }

    fun getMinutes(): Int = minutes
    fun setMinutes(m: Int) {
        minutes = m
        foregroundView.setMinutesOnly(m)
    }

    fun getSeconds(): Int = seconds
    fun setSeconds(s: Int) {
        seconds = s
        foregroundView.setSecondsOnly(s)
    }

    fun setBlinkColons(blink: Boolean) {
        blinkColons = blink
        stopBlinking()
        if (blinkColons) {
            blinkRunnable = BlinkRunnable()
            post(blinkRunnable)
        }
    }

    private fun stopBlinking() {
        removeCallbacks(blinkRunnable)
        backgroundView.showColons()
        foregroundView.showColons()
        blinkRunnable = null
    }

    private inner class BlinkRunnable : Runnable {
        override fun run() {
            val startTime = SystemClock.elapsedRealtime()
            if (blinkColons) {
                if (SystemClock.elapsedRealtime() % 1000 < 500) {
                    backgroundView.hideColons()
                    foregroundView.hideColons()
                } else {
                    backgroundView.showColons()
                    foregroundView.showColons()
                }
            }
            val endTime = SystemClock.elapsedRealtime()
            // Try to maintain a consistent period of time between redraws.
            val delay = max(0, startTime + 100 - endTime)
            this@DigitalWatchView.postDelayed(this, delay)
        }
    }

    private class DigitalView(
        context: Context,
        @ColorInt private val textColor: Int,
        normalTextSize: Float,
        showSeconds: Boolean,
        secondsTextSize: Float,
        private var showHours: Boolean,
        private var showTwoDigits: Boolean,
        hours: Int,
        minutes: Int,
        seconds: Int
    ) : ConstraintLayout(context) {

        companion object {
            private const val TWO_DIGITS_FORMAT = "%02d"
        }

        private val hoursView: TextView
        private val colonHmView: TextView
        private val minutesView: TextView
        private val colonMsView: TextView
        private val secondsView: TextView

        private val allViews: Array<TextView>

        init {

            fun View.add(f: LayoutParams.() -> Unit) {
                addView(
                    this,
                    LayoutParams(
                        LayoutParams.MATCH_CONSTRAINT,
                        LayoutParams.WRAP_CONTENT
                    ).apply {
                        matchConstraintDefaultWidth = LayoutParams.MATCH_CONSTRAINT_WRAP
                        constrainedWidth = true
                        topToTop = LayoutParams.PARENT_ID
                        bottomToBottom = LayoutParams.PARENT_ID
                        f.invoke(this)
                    }
                )
            }

            hoursView = createTextView(
                context = context,
                font = R.font.digital_7_mono_nums,
                textSize = normalTextSize,
                show = showHours
            ).apply {
                id = R.id.dwv_id_hours
                add {
                    startToStart = LayoutParams.PARENT_ID
                    endToStart = R.id.dwv_id_colon_hours_minutes
                    horizontalChainStyle = LayoutParams.CHAIN_PACKED
                }
            }
            colonHmView = createTextView(
                context = context,
                font = R.font.digital_7_colon,
                textSize = normalTextSize,
                show = showHours
            ).apply {
                id = R.id.dwv_id_colon_hours_minutes
                text = ":"
                add {
                    startToEnd = R.id.dwv_id_hours
                    endToStart = R.id.dwv_id_minutes
                }
            }
            minutesView = createTextView(
                context = context,
                font = R.font.digital_7_mono_nums,
                textSize = normalTextSize
            ).apply {
                id = R.id.dwv_id_minutes
                add {
                    startToEnd = R.id.dwv_id_colon_hours_minutes
                    endToStart = R.id.dwv_id_colon_minutes_seconds
                }
            }
            colonMsView = createTextView(
                context = context,
                font = R.font.digital_7_colon,
                textSize = secondsTextSize,
                show = showSeconds
            ).apply {
                id = R.id.dwv_id_colon_minutes_seconds
                text = ":"
                add {
                    startToEnd = R.id.dwv_id_minutes
                    endToStart = R.id.dwv_id_seconds
                    topToTop = LayoutParams.UNSET
                    bottomToBottom = LayoutParams.UNSET
                    baselineToBaseline = R.id.dwv_id_minutes
                }
            }
            secondsView = createTextView(
                context = context,
                font = R.font.digital_7_mono_nums,
                textSize = secondsTextSize,
                show = showSeconds
            ).apply {
                id = R.id.dwv_id_seconds
                add {
                    startToEnd = R.id.dwv_id_colon_minutes_seconds
                    endToEnd = LayoutParams.PARENT_ID
                    topToTop = LayoutParams.UNSET
                    bottomToBottom = LayoutParams.UNSET
                    baselineToBaseline = R.id.dwv_id_minutes
                }
            }

            allViews = arrayOf(hoursView, colonHmView, minutesView, colonMsView, secondsView)

            setTime(hours, minutes, seconds)
        }

        fun setColorInt(@ColorInt color: Int) {
            allViews.forEach {
                it.setTextColor(color)
            }
        }

        fun setNormalTextSize(textSize: Float) {
            arrayOf(hoursView, colonHmView, minutesView).forEach { it.textSize = textSize }
        }

        fun setShowSeconds(show: Boolean) {
            arrayOf(colonMsView, secondsView).run {
                if (show) forEach { it.show() } else forEach { it.gone() }
            }
        }

        fun setSecondsTextSize(textSize: Float) {
            arrayOf(colonMsView, secondsView).forEach { it.textSize = textSize }
        }

        fun setShowHours(show: Boolean) {
            showHours = show
            arrayOf(hoursView, colonHmView).run {
                if (show) forEach { it.show() } else forEach { it.gone() }
            }
        }

        fun setShowTwoDigits(show: Boolean) {
            showTwoDigits = show
        }

        fun setTime(h: Int, m: Int, s: Int) {
            setHoursOnly(h)
            setMinutesOnly(m)
            setSecondsOnly(s)
        }

        fun setHoursOnly(h: Int) {
            if (showTwoDigits) {
                hoursView.text = TWO_DIGITS_FORMAT.format(h)
            } else {
                hoursView.text = if (h < 10) " $h" else h.toString()
            }
        }

        fun setMinutesOnly(m: Int) {
            if (!showTwoDigits && !showHours) {
                minutesView.text = m.toString()
            } else {
                minutesView.text = TWO_DIGITS_FORMAT.format(m)
            }
        }

        fun setSecondsOnly(s: Int) {
            secondsView.text = TWO_DIGITS_FORMAT.format(s)
        }

        fun showColons() {
            arrayOf(colonHmView, colonMsView).forEach { it.alpha = 1f }
        }

        fun hideColons() {
            arrayOf(colonHmView, colonMsView).forEach { it.alpha = 0f }
        }

        private fun createTextView(
            context: Context,
            @FontRes font: Int,
            textSize: Float,
            show: Boolean = true
        ): TextView = AppCompatTextView(context).apply {
            typeface = context.getFontCompat(font)
            setTextColor(textColor)
            setTextSize(textSize)
            maxLines = 1
            // TextViewCompat.setAutoSizeTextTypeWithDefaults(
            //     this,
            //     TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
            // )
            if (!show) gone()
        }
    }
}