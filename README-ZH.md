# DigitalWatchView
电子表控件

![](https://github.com/DeweyReed/DigitalWatchView/blob/master/image/preview.gif?raw=true)

## 安装
Step 1. 在根build.gradle添加jitpack.io:
```
allprojects {
	repositories {
        ...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2.
```
dependencies {
	implementation 'com.github.DeweyReed:DigitalWatchView:$version'
}
```
[![](https://jitpack.io/v/DeweyReed/DigitalWatchView.svg)](https://jitpack.io/#DeweyReed/DigitalWatchView)

## 属性

|xml|method|type|default|meaning|
|:-:|:-:|:-:|:-:|:-|
|dwv_show_background|setShowBackground|boolean|false|显示数字后的88背景阴影|
|dwv_background_color|setBackgroundViewColorInt|(ColorInt) int|darker_gray|背景阴影颜色|
|dwv_background_alpha|setBackgroundViewAlpha|float([0.0, 1.0])|1.0|背景阴影透明度|
|dwv_foreground_color|setForegroundViewColorInt|(ColorInt) int|holo_green_dark|数字颜色|
|dwv_normal_text_size|setNormalTextSize|float|18sp|小时和分钟的文字大小|
|dwv_show_seconds|setShowSeconds|boolean|true|显示秒钟|
|dwv_seconds_text_size|setSecondsTextSize|float|18sp|秒钟的文字大小|
|dwv_show_hours|setShowHours|boolean|true|设置小时|
|dwv_show_two_digits|setShowTwoDigits|boolean|true|小时（如果不显示小时使用的话，就是分钟）使用%02d的格式|
|dwv_hours|setHours|int|0|设置小时|
|dwv_minutes|setMinutes|int|0|设置分钟|
|dwv_seconds|setSeconds|int|0|设置秒钟|
|dwv_blink_colons|setBlinkColons|boolean|false|闪烁两个冒号|
||setTime|(int, int, int)||一次性设置时分秒|
||getHours|||获取小时|
||getMinutes|||获取分钟|
||getSeconds|||获取秒钟|

## ..
两个冒号使用的字体`digital_7_colon.ttf`是`digital_7.ttf`的子集。仅包含了一个冒号却占2.89KB空间。`digital_7_mono_nums.ttf`也是，十个数字占了5.44KB的空间。

我自然很在意APK大小，但不清楚减小这两个字体占用大小的方法。需要有缘人的帮助！

## License
[MIT License](https://github.com/DeweyReed/DigitalWatchView/blob/master/LICENSE)