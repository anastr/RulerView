package com.github.anastr.rulerview

import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * Created by Anas Altair on 8/29/2018.
 */
enum class RulerUnit(val converter: Float, val unit: String) {
    MM(25.4f, "MM"),
    CM(2.54f, "CM"),
    IN(1f   , "IN");

    companion object {

        fun mmToPx(mm: Float, coefficient: Float, displayMetrics: DisplayMetrics): Float {
            return mm * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, coefficient, displayMetrics)
        }

        fun pxToIn(px: Float, coefficient: Float, displayMetrics: DisplayMetrics): Float {
            return px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, coefficient, displayMetrics)
        }

    }

    /**
     * @param value in IN
     */
    fun getUnitString(value: Float) = "${(value * converter).format(1)} $unit"

    /**
     * @param value in IN
     *
     * @return value in #unit
     */
    fun convert(value: Float) = value * converter
}

private fun Float.format(value: Int) = java.lang.String.format("%.${value}f", this)
