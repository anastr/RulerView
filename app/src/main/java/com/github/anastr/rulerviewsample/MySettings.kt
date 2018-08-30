package com.github.anastr.rulerviewsample

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Anas Altair on 8/29/2018.
 */
class MySettings(context: Context) {

    private val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var rulerCoefficient: Float
        get() = sharedPref.getFloat("ruler_coefficient", 1f)
        set(value) {
            val editor = sharedPref.edit()
            editor.putFloat("ruler_coefficient", value)
            editor.apply()
        }

}