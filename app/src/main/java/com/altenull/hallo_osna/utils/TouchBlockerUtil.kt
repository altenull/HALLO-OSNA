package com.altenull.hallo_osna.utils

import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import com.altenull.hallo_osna.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object TouchBlockerUtil {
    fun blockTouchForMainModeReveal(window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        CoroutineScope(Dispatchers.Default).launch {
            delay(Constants.AnimDuration.MainModeReveal)
            Handler(Looper.getMainLooper()).post {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

    fun blockTouchForMainSceneReveal(window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        CoroutineScope(Dispatchers.Default).launch {
            delay(Constants.AnimDuration.SceneReveal)
            Handler(Looper.getMainLooper()).post {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

    fun blockTouchForHelpEnter(window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        CoroutineScope(Dispatchers.Default).launch {
            // The duration of `help_enter.xml` animation is @android:integer/config_mediumAnimTime (400ms).
            delay(400L)
            Handler(Looper.getMainLooper()).post {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }
}
