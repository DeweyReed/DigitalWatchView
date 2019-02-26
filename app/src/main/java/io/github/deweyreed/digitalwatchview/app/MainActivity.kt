package io.github.deweyreed.digitalwatchview.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import kotlinx.android.synthetic.main.activity_main.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.sp
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(),
    ColorPickerDialogListener, DiscreteSeekBar.OnProgressChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListeners()
    }

    override fun onDialogDismissed(dialogId: Int) = Unit

    override fun onColorSelected(dialogId: Int, color: Int) {
        when (dialogId) {
            R.id.imageBackgroundColor -> {
                imageBackgroundColor.setBackgroundColor(color)
                digitalWatchView.setBackgroundViewColorInt(color)
            }
            R.id.imageForegroundColor -> {
                imageForegroundColor.setBackgroundColor(color)
                digitalWatchView.setForegroundViewColorInt(color)
            }
        }
    }

    override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
        if (fromUser) {
            when (seekBar?.id) {
                R.id.seekBackgroundAlpha -> digitalWatchView.setBackgroundViewAlpha(value / 100f)
                R.id.seekNormalTextSize -> digitalWatchView.setNormalTextSize(sp(value).toFloat())
                R.id.seekSecondsTextSize -> digitalWatchView.setSecondsTextSize(sp(value).toFloat())
                R.id.seekHours -> digitalWatchView.setHours(value)
                R.id.seekMinutes -> digitalWatchView.setMinutes(value)
                R.id.seekSeconds -> digitalWatchView.setSeconds(value)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) = Unit

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) = Unit

    private fun setupListeners() {
        checkShowBackground.setOnCheckedChangeListener { _, isChecked ->
            digitalWatchView.setShowBackground(isChecked)
            imageBackgroundColor.isEnabled = isChecked
            seekBackgroundAlpha.isEnabled = isChecked
        }
        checkShowHours.setOnCheckedChangeListener { _, isChecked ->
            digitalWatchView.setShowHours(isChecked)
        }
        checkShowTwoDigits.setOnCheckedChangeListener { _, isChecked ->
            digitalWatchView.setShowTwoDigits(isChecked)
        }
        checkShowSeconds.setOnCheckedChangeListener { _, isChecked ->
            digitalWatchView.setShowSeconds(isChecked)
            seekSecondsTextSize.isEnabled = isChecked
        }
        checkBlinkColons.setOnCheckedChangeListener { _, isChecked ->
            digitalWatchView.setBlinkColons(isChecked)
        }

        arrayOf(imageBackgroundColor, imageForegroundColor).forEach { view ->
            view.setOnClickListener {
                ColorPickerDialog.newBuilder().setDialogId(it.id).show(this@MainActivity)
            }
        }

        arrayOf(
            seekBackgroundAlpha, seekNormalTextSize, seekSecondsTextSize,
            seekHours, seekMinutes, seekSeconds
        ).forEach {
            it.setOnProgressChangeListener(this@MainActivity)
        }

        btnExample.setOnClickListener { startActivity<SecondActivity>() }
    }
}
