[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-DigitalWatchView-green.svg?style=flat)]( https://android-arsenal.com/details/1/6847)
[![newest version](https://jitpack.io/v/DeweyReed/DigitalWatchView.svg)](https://jitpack.io/#DeweyReed/DigitalWatchView)

# DigitalWatchView

Just looks like your digital watch.

![preview image](https://github.com/DeweyReed/DigitalWatchView/blob/master/image/preview.gif?raw=true)

## Install

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```Groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency => [![newest version](https://jitpack.io/v/DeweyReed/DigitalWatchView.svg)](https://jitpack.io/#DeweyReed/DigitalWatchView)

```Groovy
dependencies {
    implementation 'com.github.DeweyReed:DigitalWatchView:$version'
}
```

**1.0.2 is the last version using the support library.**

## Try

Download a sample APK [here](https://github.com/DeweyReed/DigitalWatchView/releases/download/1.0/sample.apk)

## Usage

```Kotlin
<io.github.deweyreed.digitalwatchview.DigitalWatchView
    android:id="@+id/digitalWatchView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="16dp"
    app:dwv_background_alpha="0.1"
    app:dwv_background_color="@android:color/black"
    app:dwv_blink_colons="true"
    app:dwv_foreground_color="@android:color/holo_red_light"
    app:dwv_hours="8"
    app:dwv_minutes="23"
    app:dwv_normal_text_size="24sp"
    app:dwv_seconds="57"
    app:dwv_seconds_text_size="12sp"
    app:dwv_show_background="true"
    app:dwv_show_hours="true"
    app:dwv_show_seconds="true"
    app:dwv_show_two_digits="false" />
```

## Attributes

|xml|method|type|default|meaning|
|:-:|:-:|:-:|:-:|:-|
|dwv_show_background|setShowBackground|boolean|false|Show a shadow background|
|dwv_background_color|setBackgroundViewColorInt|(ColorInt) int|darker_gray|Background text color|
|dwv_background_alpha|setBackgroundViewAlpha|float([0.0, 1.0])|1.0|Set background text alpha|
|dwv_foreground_color|setForegroundViewColorInt|(ColorInt) int|holo_green_dark|Digital text color|
|dwv_normal_text_size|setNormalTextSize|float|18sp|Set hours and minutes text size|
|dwv_show_seconds|setShowSeconds|boolean|true|Show seconds digits|
|dwv_seconds_text_size|setSecondsTextSize|float|18sp|Set seconds text size|
|dwv_show_hours|setShowHours|boolean|true|Show hours digits|
|dwv_show_two_digits|setShowTwoDigits|boolean|true|Use %02d format for hours digits(minutes if hours are hidden)|
|dwv_hours|setHours|int|0|Set hours|
|dwv_minutes|setMinutes|int|0|Set minutes|
|dwv_seconds|setSeconds|int|0|Set seconds|
|dwv_blink_colons|setBlinkColons|boolean|false|Blink colons like a digital watch|
||setTime|(int, int, int)||Set hours, minutes and seconds using one method|
||getHours|||Return current hours|
||getMinutes|||Return current minutes|
||getSeconds|||Return current seconds|
||Other Getters|||Every setter has its corresponding getter|

## =-=

`digital_7_colon.ttf` the two colons use is a subset font of `digital_7.ttf`. It simply includes one glyph(the colon) but takes 2.89KB space. So does `digital_7_mono_nums.ttf`, ten numbers glyphs take 5.44KB space.

I do care about APK size but I don't know if there is a way to reduce these two fonts size. I need help!!

## License

[MIT License](https://github.com/DeweyReed/DigitalWatchView/blob/master/LICENSE)