package com.example.animapp

import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator

fun animation(fromAlpha : Float, toAlpha : Float, this_duration : Long) : AlphaAnimation {
    return AlphaAnimation(fromAlpha, toAlpha).apply {
        duration = this_duration
        repeatCount = AlphaAnimation.INFINITE
        repeatMode = AlphaAnimation.REVERSE
        interpolator = LinearInterpolator()
    }
}