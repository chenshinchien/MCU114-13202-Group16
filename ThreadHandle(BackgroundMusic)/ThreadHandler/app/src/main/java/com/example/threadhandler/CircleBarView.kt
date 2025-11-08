package com.example.threadhandler

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CircleProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 25f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    var progress = 0
        set(value) {
            field = value.coerceIn(0, 100)
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width.coerceAtMost(height) / 2f) - 40

        paint.color = Color.LTGRAY
        canvas.drawCircle(centerX, centerY, radius, paint)

        paint.color = Color.parseColor("#3F51B5")
        val rect = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(rect, -90f, 360f * progress / 100f, false, paint)

        paint.style = Paint.Style.FILL
        paint.textSize = 70f
        paint.color = Color.BLACK
        val text = "$progress%"
        val textWidth = paint.measureText(text)
        canvas.drawText(text, centerX - textWidth / 2, centerY + 25f, paint)

        paint.style = Paint.Style.STROKE
    }
}
