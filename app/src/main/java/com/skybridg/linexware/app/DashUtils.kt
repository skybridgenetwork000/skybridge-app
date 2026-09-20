package com.skybridg.linexware.app

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.TextView

object DashUtils {

    @JvmStatic
    fun setDashedUnderline(tv: TextView, color: Int, strokeWidthPx: Float, dashWidth: Float, dashGap: Float) {
        val drawable = ShapeDrawable(object : Shape() {
            override fun draw(canvas: Canvas, paint: Paint) {
                canvas.drawLine(0f, height - 1f, width, height - 1f, paint)
            }
        })
        val p = drawable.paint
        p.color = color
        p.strokeWidth = strokeWidthPx
        p.style = Paint.Style.STROKE
        p.pathEffect = DashPathEffect(floatArrayOf(dashWidth, dashGap), 0f)
        tv.setPadding(tv.paddingLeft, tv.paddingTop, tv.paddingRight, strokeWidthPx.toInt() + 4)
        tv.foreground = drawable
    }

    @JvmStatic
    fun setDashedStroke(view: View, color: Int, strokeWidthPx: Int, dashWidth: Float, dashGap: Float, cornerRadiusPx: Float) {
        val gd = GradientDrawable().apply {
            setColor(Color.TRANSPARENT)
            setStroke(strokeWidthPx, color, dashWidth, dashGap)
            cornerRadius = cornerRadiusPx
        }
        view.foreground = gd
        setRoundedClip(view, cornerRadiusPx)
    }

    @JvmStatic
    fun setDashedStroke(view: View, color: Int, strokeWidthPx: Int, dashWidth: Float, dashGap: Float, cornerRadiusPx: Double) {
        setDashedStroke(view, color, strokeWidthPx, dashWidth, dashGap, cornerRadiusPx.toFloat())
    }

    @JvmStatic
    fun setRoundedClip(view: View, cornerRadiusPx: Float) {
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(v: View, outline: Outline) {
                outline.setRoundRect(0, 0, v.width, v.height, cornerRadiusPx)
            }
        }
        view.clipToOutline = true
    }
}
