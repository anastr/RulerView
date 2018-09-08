package com.github.anastr.rulerview

import android.app.AlertDialog
import android.content.Context
import android.content.res.TypedArray
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout



/**
 * Created by Anas Altair on 9/7/2018.
 */
class CalibrationPreference : DialogPreference {

    companion object {
        const val TAG = "Calibration_View"
    }

    private var calibrationView: RulerCalibrationView? = null

    var coefficient = 1f
        set(value) {
            field = value
            persistFloat(value)
        }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun onCreateDialogView(): View {
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dpTOpx(180f).toInt())
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val padding = dpTOpx(5f).toInt()
        layout.setPadding(padding, padding, padding, padding)

        calibrationView = RulerCalibrationView(context)
        calibrationView!!.tag = TAG
        layout.addView(calibrationView, params)

        return layout
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        val calibrationView = view?.findViewWithTag<RulerCalibrationView>(TAG)
        calibrationView?.coefficient = coefficient
    }

    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder?) {
        super.onPrepareDialogBuilder(builder)
        builder?.setPositiveButton(android.R.string.ok) { _, _ ->
            coefficient = calibrationView!!.coefficient
        }
        builder?.setNegativeButton(android.R.string.cancel, null)
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        return a!!.getFloat(index, 1f)
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        coefficient = if (restorePersistedValue) getPersistedFloat(coefficient) else defaultValue as Float
    }

    /**
     * convert dp to **pixel**.
     * @param dp to convert.
     * @return Dimension in pixel.
     */
    private fun dpTOpx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}