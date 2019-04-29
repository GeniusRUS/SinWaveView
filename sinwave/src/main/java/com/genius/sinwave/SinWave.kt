package com.genius.sinwave

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange

class SinWave(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var waveColor: Int
    private var waviness: Float
    private val wavePath = Path()
    private val wavePaint = Paint().apply {
        isDither = true
        isAntiAlias = true
    }

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SinWave)
        waveColor = typedArray.getColor(R.styleable.SinWave_waveColor, Color.RED)
        waviness = typedArray.getFloat(R.styleable.SinWave_waviness, 1F).coerceIn(0F, 1F)
        typedArray.recycle()

        wavePaint.color = waveColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 100
        val desiredHeight = MIN_HEIGHT_DP.toPx.toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> Math.min(desiredWidth, widthSize)
            MeasureSpec.UNSPECIFIED -> desiredWidth
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(desiredHeight, heightSize)
            MeasureSpec.UNSPECIFIED -> desiredHeight
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)

        recalculateWavinessPoints(waviness)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(wavePath, wavePaint)
    }

    fun updateWaviness(@FloatRange(from = 0.0, to = 1.0) waviness: Float) {
        recalculateWavinessPoints(waviness.coerceIn(0F, 1F))
        invalidate()
    }

    private fun recalculateWavinessPoints(newWaviness: Float) {
        waviness = newWaviness
        val middlePoint = measuredHeight * 0.25F * waviness
        val topPoint = measuredHeight * 0.75F * waviness

        wavePath.apply {
            reset()
            moveTo(0F, 0F)
            quadTo(measuredWidth * WAVE_STEP, topPoint, measuredWidth * WAVE_STEP * 2, middlePoint)
            quadTo(measuredWidth * WAVE_STEP * 3, - middlePoint, measuredWidth * WAVE_STEP * 4, middlePoint)
            quadTo(measuredWidth * WAVE_STEP * 5, topPoint, measuredWidth.toFloat() * WAVE_STEP * 7, 0F)
        }
    }

    private val Float.toPx: Float
        get() = this * context.resources.displayMetrics.density

    companion object {
        private const val MIN_HEIGHT_DP = 24F
        private const val WAVE_STEP = 1F / 5F
    }
}