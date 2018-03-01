package io.github.deweyreed.digitalwatchview

import android.content.Context
import android.os.SystemClock
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.support.annotation.FontRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout

@Suppress("unused", "MemberVisibilityCanBePrivate")
/**
 * Created on 2018/2/26.
 */

class DigitalWatchView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val backgroundView: DigitalView
    private val foregroundView: DigitalView

    private var blinkColons: Boolean
    private var blinkRunnable: BlinkRunnable? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.DigitalWatchView, 0, 0)
        val showBackground = ta.getBoolean(R.styleable.DigitalWatchView_dwv_show_background,
                false)
        val backgroundColor = ta.getColor(R.styleable.DigitalWatchView_dwv_background_color,
                color(android.R.color.darker_gray))
        val backgroundAlpha = ta.getFloat(R.styleable.DigitalWatchView_dwv_background_alpha,
                1f)
        val foregroundColor = ta.getColor(R.styleable.DigitalWatchView_dwv_foreground_color,
                color(android.R.color.holo_green_dark))
        val normalTextSize = ta.getDimension(R.styleable.DigitalWatchView_dwv_normal_text_size,
                sp(18).toFloat())
        val showSeconds = ta.getBoolean(R.styleable.DigitalWatchView_dwv_show_seconds,
                true)
        val secondsTextSize = ta.getDimension(R.styleable.DigitalWatchView_dwv_seconds_text_size,
                sp(18).toFloat())
        val showHours = ta.getBoolean(R.styleable.DigitalWatchView_dwv_show_hours,
                true)
        val showTwoDigits = ta.getBoolean(R.styleable.DigitalWatchView_dwv_show_two_digits,
                true)
        val hours = ta.getInteger(R.styleable.DigitalWatchView_dwv_hours, 0)
        val minutes = ta.getInteger(R.styleable.DigitalWatchView_dwv_minutes, 0)
        val seconds = ta.getInteger(R.styleable.DigitalWatchView_dwv_seconds, 0)
        blinkColons = ta.getBoolean(R.styleable.DigitalWatchView_dwv_blink_colons, false)
        ta.recycle()

        backgroundView = DigitalView(context, backgroundColor, normalTextSize,
                showSeconds, secondsTextSize, showHours, showTwoDigits,
                88, 88, 88).apply {
            if (!showBackground) gone()
            alpha = backgroundAlpha.clamp(0f, 1f)
        }
        foregroundView = DigitalView(context, foregroundColor, normalTextSize,
                showSeconds, secondsTextSize, showHours, showTwoDigits,
                hours, minutes, seconds).apply {
            gravity = Gravity.END
        }

        addView(backgroundView)
        addView(foregroundView)

        if (blinkColons) setBlinkColons(true)
    }

    override fun onDetachedFromWindow() {
        stopBlinking()
        super.onDetachedFromWindow()
    }

    fun setShowBackground(show: Boolean) {
        if (show) backgroundView.show() else backgroundView.gone()
    }

    fun setBackgroundViewColorInt(@ColorInt color: Int) {
        backgroundView.setColorInt(color)
    }

    fun setBackgroundViewAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        backgroundView.alpha = alpha.clamp(0f, 1f)
    }

    fun setForegroundViewColorInt(@ColorInt color: Int) {
        foregroundView.setColorInt(color)
    }

    fun setNormalTextSize(textSize: Float) {
        backgroundView.setNormalTextSize(textSize)
        foregroundView.setNormalTextSize(textSize)
    }

    fun setShowSeconds(show: Boolean) {
        backgroundView.setShowSeconds(show)
        foregroundView.setShowSeconds(show)
    }

    fun setSecondsTextSize(textSize: Float) {
        backgroundView.setSecondsTextSize(textSize)
        foregroundView.setSecondsTextSize(textSize)
    }

    fun setShowHours(show: Boolean) {
        backgroundView.setShowHours(show)
        foregroundView.setShowHours(show)
    }

    fun setShowTwoDigits(show: Boolean) {
        foregroundView.setShowTwoDigits(show)
    }

    fun setTime(h: Int, m: Int, s: Int) {
        foregroundView.setTime(h, m, s)
    }

    fun getHours(): Int = foregroundView.hours
    fun setHours(h: Int) {
        foregroundView.setHoursOnly(h)
    }

    fun getMinutes(): Int = foregroundView.minutes
    fun setMinutes(m: Int) {
        foregroundView.setMinutesOnly(m)
    }

    fun getSeconds(): Int = foregroundView.seconds
    fun setSeconds(s: Int) {
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
            val delay = Math.max(0, startTime + 100 - endTime)
            this@DigitalWatchView.postDelayed(this, delay)
        }
    }

    private class DigitalView(context: Context,
                              @ColorInt private val textColor: Int,
                              normalTextSize: Float,
                              showSeconds: Boolean,
                              secondsTextSize: Float,
                              private var showHours: Boolean,
                              private var showTwoDigits: Boolean,
                              var hours: Int,
                              var minutes: Int,
                              var seconds: Int
    ) : LinearLayout(context) {

        companion object {
            private const val TWO_DIGITS_FORMAT = "%02d"
        }

        private val hoursView: AppCompatTextView
        private val colonHmView: AppCompatTextView
        private val minutesView: AppCompatTextView
        private val colonMsView: AppCompatTextView
        private val secondsView: AppCompatTextView

        init {
            orientation = LinearLayout.HORIZONTAL

            hoursView = createTextView(context, R.font.digital_7_mono_nums, normalTextSize, showHours)
            colonHmView = createTextView(context, R.font.digital_7_colon, normalTextSize, showHours).apply {
                text = ":"
            }
            minutesView = createTextView(context, R.font.digital_7_mono_nums, normalTextSize)
            colonMsView = createTextView(context, R.font.digital_7_colon, secondsTextSize, showSeconds).apply {
                text = ":"
            }
            secondsView = createTextView(context, R.font.digital_7_mono_nums, secondsTextSize, showSeconds)

            arrayOf(hoursView, colonHmView, minutesView, colonMsView, secondsView)
                    .forEach { addView(it) }

            setTime(hours, minutes, seconds)
        }

        fun setColorInt(@ColorInt color: Int) {
            arrayOf(hoursView, colonHmView, minutesView, colonMsView, secondsView).forEach {
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
            setTime(hours, minutes, seconds)
        }

        fun setTime(h: Int, m: Int, s: Int) {
            setHoursOnly(h)
            setMinutesOnly(m)
            setSecondsOnly(s)
        }

        fun setHoursOnly(h: Int) {
            hours = h
            if (showTwoDigits) {
                hoursView.text = TWO_DIGITS_FORMAT.format(h)
            } else {
                hoursView.text = h.toString()
            }
        }

        fun setMinutesOnly(m: Int) {
            minutes = m
            if (!showTwoDigits && !showHours) {
                minutesView.text = m.toString()
            } else {
                minutesView.text = TWO_DIGITS_FORMAT.format(m)
            }
        }

        fun setSecondsOnly(s: Int) {
            seconds = s
            secondsView.text = TWO_DIGITS_FORMAT.format(s)
        }

        fun showColons() {
            arrayOf(colonHmView, colonMsView).forEach { it.alpha = 1f }
        }

        fun hideColons() {
            arrayOf(colonHmView, colonMsView).forEach { it.alpha = 0f }
        }

        private fun createTextView(context: Context,
                                   @FontRes font: Int,
                                   textSize: Float,
                                   show: Boolean = true
        ): AppCompatTextView = AppCompatTextView(context).apply {
            typeface = ResourcesCompat.getFont(context, font)
            setTextColor(textColor)
            setTextSize(textSize)
            if (!show) gone()
        }
    }
}