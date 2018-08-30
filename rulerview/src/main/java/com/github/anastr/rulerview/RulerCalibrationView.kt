package com.github.anastr.rulerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcelable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.github.anastr.rulerview.RulerUnit.Companion.mmToPx

/**
 * Created by Anas Altair on 8/29/2018.
 */
class RulerCalibrationView : View {

    private val grayPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val colorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    var coefficient = 1f
        set(value) {
            field = value
            invalidate()
        }
    private val maxCoefficient = 1.5f
    private val minCoefficient = .5f

    var markCmWidth = dpTOpx(20f)
        set(value) {
            field = value
            invalidate()
        }
    var markHalfCmWidth = dpTOpx(15f)
        set(value) {
            field = value
            invalidate()
        }
    var markMmWidth = dpTOpx(10f)

    private var pointerX :Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var onCoefficientChange: ((Float) -> Unit)? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        grayPaint.color = Color.GRAY
        colorPaint.color = context.resources.getColor(R.color.colorAccent)

        textPaint.textSize = dpTOpx(20f)
        textPaint.color = context.resources.getColor(R.color.colorAccent)
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val oneMmInPx = mmToPx(1f, coefficient, resources.displayMetrics)
        for (i in 1 .. 100) {
            val x = oneMmInPx * i
            when {
                i%10 == 0 -> {
                    canvas?.drawLine(x, 0f, x, markCmWidth, colorPaint)
                    canvas?.drawLine(x, height.toFloat(), x, height - markCmWidth, colorPaint)
                    canvas?.drawText("${i/10}", x, height*.5f + textPaint.textSize*.5f, textPaint)
                }
                i%5 == 0 -> {
                    canvas?.drawLine(x, 0f, x, markHalfCmWidth, grayPaint)
                    canvas?.drawLine(x, height.toFloat(), x, height - markHalfCmWidth, grayPaint)
                }
                else -> {
                    canvas?.drawLine(x, 0f, x, markMmWidth, grayPaint)
                    canvas?.drawLine(x, height.toFloat(), x, height - markMmWidth, grayPaint)
                }
            }
        }
        canvas?.drawLine(0f, 0f, oneMmInPx * 100f, 0f, colorPaint)
        canvas?.drawLine(0f, height.toFloat(), oneMmInPx * 100f, height.toFloat(), colorPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                pointerX = event.x
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - pointerX
                coefficient += dx * .001f
                // clamp
                coefficient = Math.max(minCoefficient, Math.min(maxCoefficient, coefficient))
                onCoefficientChange?.invoke(coefficient)
                pointerX = event.x
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                return false
            }
        }
        return false
    }

    override fun onSaveInstanceState(): Parcelable? {
        super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putFloat("coefficient", coefficient)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var _state = state
        val bundle = _state as Bundle
        coefficient = bundle.getFloat("coefficient")
        _state = bundle.getParcelable("superState")
        super.onRestoreInstanceState(_state)
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