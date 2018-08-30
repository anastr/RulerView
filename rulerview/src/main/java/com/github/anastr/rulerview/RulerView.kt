package com.github.anastr.rulerview

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by Anas Altair on 8/8/2018.
 */
class RulerView : View {

    private val colorPaintMask = Paint(Paint.ANTI_ALIAS_FLAG)
    private val grayPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val grayPaintReplace: Paint
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val textPaintReplace: TextPaint

    private var rulerX = 0f

    private val minDistance = dpTOpx(10f)

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
        set(value) {
            field = value
            invalidate()
        }

    private var pointerX = 0f

    var unit: RulerUnit = RulerUnit.MM
        set(value) {
            field = value
            invalidate()
        }

    var coefficient = 1f
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        colorPaintMask.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        grayPaint.color = Color.DKGRAY
        grayPaintReplace = Paint(grayPaint)
        grayPaintReplace.color = Color.WHITE
        grayPaintReplace.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)

        textPaint.textSize = dpTOpx(20f)
        textPaint.color = context.resources.getColor(R.color.colorAccent)
        textPaint.textAlign = Paint.Align.CENTER
        textPaintReplace = TextPaint(textPaint)
        textPaintReplace.color = Color.WHITE
        textPaintReplace.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawMarks(canvas, grayPaint, textPaint)

        canvas?.drawRect(left.toFloat(), 0f, rulerX, height.toFloat(), colorPaintMask)

        drawMarks(canvas, grayPaintReplace, textPaintReplace)
    }

    private fun drawMarks(canvas: Canvas?, paint: Paint, textPaint: Paint) {
        val oneMmInPx = RulerUnit.mmToPx(1f, coefficient, resources.displayMetrics)
        for(i in 1..1000) {
            val x = oneMmInPx * i
            when {
                i%10 == 0 -> {
                    canvas?.drawLine(x, 0f, x, markCmWidth, paint)
                    canvas?.drawLine(x, height.toFloat(), x, height - markCmWidth, paint)
                    canvas?.drawText("${i/10}", x, markCmWidth + textPaint.textSize, textPaint)
                    canvas?.drawText("${i/10}", x, height - markCmWidth - dpTOpx(2f), textPaint)
                }
                i%5 == 0 -> {
                    canvas?.drawLine(x, 0f, x, markHalfCmWidth, paint)
                    canvas?.drawLine(x, height.toFloat(), x, height - markHalfCmWidth, paint)
                }
                else -> {
                    canvas?.drawLine(x, 0f, x, markMmWidth, paint)
                    canvas?.drawLine(x, height.toFloat(), x, height - markMmWidth, paint)
                }
            }
            if (x >= width)
                break
        }
        canvas?.drawText(unit.getUnitString(RulerUnit.pxToIn(rulerX, coefficient, resources.displayMetrics))
                , width * .5f, height * .5f + textPaint.textSize*.5f, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                pointerX = event.x
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - pointerX
                rulerX += dx
                // clamp
                rulerX = Math.max(0f, Math.min(width.toFloat(), rulerX))
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

    fun getDistance () = unit.convert(RulerUnit.pxToIn(rulerX, coefficient, resources.displayMetrics))

    override fun onSaveInstanceState(): Parcelable? {
        super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putFloat("rulerX", rulerX)
        bundle.putFloat("coefficient", coefficient)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var _state = state
        val bundle = _state as Bundle
        rulerX = bundle.getFloat("rulerX")
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

    /**
     * convert pixel to **dp**.
     * @param px to convert.
     * @return Dimension in dp.
     */
    fun pxTOdp(px: Float): Float {
        return px / context.resources.displayMetrics.density
    }
}
