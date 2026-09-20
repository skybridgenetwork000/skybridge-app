package com.skybridg.linexware.app

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import org.json.JSONArray
import java.lang.ref.WeakReference
import java.util.ArrayList
import kotlin.math.abs
import kotlin.math.roundToInt

class AutoCarousel(
    private val context: Context,
    container: LinearLayout,
    urls: List<String>?
) {

    private val containerRef: WeakReference<LinearLayout> = WeakReference(container)
    private val imageUrls: List<String> = urls ?: ArrayList()

    private var cornerRadiusPx: Int = dp(16)
    private var intervalMs: Int = 3000

    private var activeDotColor: Int = Color.BLACK
    private var inactiveDotColor: Int = Color.parseColor("#FEFEFE")
    private val dotBorderColor: Int = Color.parseColor("#E0E0E0")

    private var placeholderResId: Int = 0

    private var root: FrameLayout? = null
    private var dotsRow: View? = null
    private val slides = ArrayList<ImageView>()
    private val dotViews = ArrayList<View>()
    private val dotDrawables = ArrayList<GradientDrawable>()

    private var currentIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private var running = false

    private var gestureDetector: GestureDetector? = null

    private val autoAdvance = object : Runnable {
        override fun run() {
            val container = containerRef.get()
            val r = root
            if (!running || container == null || !container.isAttachedToWindow || imageUrls.isEmpty() || r == null) {
                return
            }
            val nextIndex = (currentIndex + 1) % imageUrls.size
            transitionTo(nextIndex, true)
            animateDots(nextIndex)
            currentIndex = nextIndex
            handler.postDelayed(this, intervalMs.toLong())
        }
    }

    companion object {
        @JvmStatic
        fun parseUrls(json: String): List<String> {
            val out = ArrayList<String>()
            try {
                val arr = JSONArray(json)
                for (i in 0 until arr.length()) {
                    out.add(arr.getString(i))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return out
        }
    }

    fun setCornerRadius(dp: Int): AutoCarousel {
        cornerRadiusPx = dp(dp)
        return this
    }

    fun setInterval(ms: Int): AutoCarousel {
        intervalMs = ms
        return this
    }

    fun setDotColors(activeColor: Int, inactiveColor: Int): AutoCarousel {
        activeDotColor = activeColor
        inactiveDotColor = inactiveColor
        return this
    }

    fun setPlaceholder(drawableResId: Int): AutoCarousel {
        placeholderResId = drawableResId
        return this
    }

    fun goToNext() {
        if (imageUrls.size < 2) return
        val nextIndex = (currentIndex + 1) % imageUrls.size
        manualAdvance(nextIndex, true)
    }

    fun goToPrevious() {
        if (imageUrls.size < 2) return
        val prevIndex = (currentIndex - 1 + imageUrls.size) % imageUrls.size
        manualAdvance(prevIndex, false)
    }

    fun start(): AutoCarousel {
        val container = containerRef.get() ?: return this

        container.removeAllViews()
        slides.clear()
        dotViews.clear()
        dotDrawables.clear()
        currentIndex = 0

        val r = FrameLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        root = r

        val count = if (imageUrls.isEmpty()) 2 else imageUrls.size
        for (i in 0 until count) {
            val iv = ImageView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = ImageView.ScaleType.CENTER_CROP
                clipToOutline = true
                background = roundedOutlineDrawable(cornerRadiusPx)
                if (placeholderResId != 0) {
                    try {
                        val bmp = BitmapFactory.decodeResource(context.resources, placeholderResId)
                        if (bmp != null) {
                            val rd = RoundedBitmapDrawableFactory.create(context.resources, bmp)
                            rd.cornerRadius = cornerRadiusPx.toFloat()
                            setImageDrawable(rd)
                        } else {
                            setImageResource(placeholderResId)
                        }
                    } catch (e: Exception) {
                        setImageResource(placeholderResId)
                    }
                }
                alpha = if (i == 0) 1f else 0f
            }
            r.addView(iv)
            slides.add(iv)
        }

        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                bottomMargin = dp(12)
            }
        }

        for (i in 0 until count) {
            val dot = View(context)
            val isActive = (i == 0)
            val lp = LinearLayout.LayoutParams(if (isActive) dp(20) else dp(7), dp(7)).apply {
                setMargins(dp(3), 0, dp(3), 0)
            }
            dot.layoutParams = lp

            val gd = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = dp(10).toFloat()
                setColor(if (isActive) activeDotColor else inactiveDotColor)
                setStroke(dp(1), dotBorderColor)
            }
            dot.background = gd

            row.addView(dot)
            dotViews.add(dot)
            dotDrawables.add(gd)
        }
        r.addView(row)
        dotsRow = row

        container.addView(r)

        running = true
        handler.removeCallbacks(autoAdvance)
        handler.postDelayed(autoAdvance, intervalMs.toLong())
        setupSwipeGestures()
        return this
    }

    fun stop() {
        running = false
        handler.removeCallbacks(autoAdvance)
    }

    private fun setupSwipeGestures() {
        val swipeDistanceThreshold = dp(40)
        val swipeVelocityThreshold = dp(60)

        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 == null) return false
                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y
                if (abs(diffX) > abs(diffY) && abs(diffX) > swipeDistanceThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX < 0) {
                        goToNext()
                    } else {
                        goToPrevious()
                    }
                    return true
                }
                return false
            }
        })

        root?.setOnTouchListener { _, event ->
            gestureDetector?.onTouchEvent(event) ?: false
        }
    }

    private fun manualAdvance(targetIndex: Int, forward: Boolean) {
        handler.removeCallbacks(autoAdvance)
        transitionTo(targetIndex, forward)
        animateDots(targetIndex)
        currentIndex = targetIndex
        if (running) {
            handler.postDelayed(autoAdvance, intervalMs.toLong())
        }
    }

    private fun transitionTo(nextIndex: Int, forward: Boolean) {
        if (slides.isEmpty()) return
        val outgoing = slides[currentIndex]
        val incoming = slides[nextIndex]
        if (outgoing === incoming) return

        incoming.bringToFront()
        dotsRow?.bringToFront()
        val r = root ?: return
        r.requestLayout()
        r.invalidate()

        var width = r.width.toFloat()
        if (width <= 0) width = 1000f

        val startOffset = if (forward) width * 0.28f else -width * 0.28f
        val endOffset = if (forward) -width * 0.28f else width * 0.28f

        incoming.alpha = 0f
        incoming.scaleX = 0.8f
        incoming.scaleY = 0.8f
        incoming.translationX = startOffset

        incoming.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .translationX(0f)
            .setDuration(600)
            .setInterpolator(OvershootInterpolator(0.8f))
            .start()

        outgoing.animate()
            .alpha(0f)
            .scaleX(0.8f)
            .scaleY(0.8f)
            .translationX(endOffset)
            .setDuration(600)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction {
                outgoing.scaleX = 1f
                outgoing.scaleY = 1f
                outgoing.translationX = 0f
            }
            .start()
    }

    private fun animateDots(activeIndex: Int) {
        for (i in 0 until dotViews.size) {
            val toActive = (i == activeIndex)
            animateSingleDot(dotViews[i], dotDrawables[i], toActive)
        }
    }

    private fun animateSingleDot(dot: View, gd: GradientDrawable, toActive: Boolean) {
        val lp = dot.layoutParams as LinearLayout.LayoutParams
        val fromWidth = lp.width
        val toWidth = if (toActive) dp(20) else dp(7)
        val fromColor = if (toActive) inactiveDotColor else activeDotColor
        val toColor = if (toActive) activeDotColor else inactiveDotColor

        if (fromWidth == toWidth) return

        val colorEvaluator = ArgbEvaluator()
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 380
            interpolator = OvershootInterpolator(2.5f)
            addUpdateListener { animation ->
                val fraction = animation.animatedFraction
                val value = animation.animatedValue as Float
                val newWidth = (fromWidth + (toWidth - fromWidth) * value).roundToInt()
                if (newWidth > 0) {
                    lp.width = newWidth
                    dot.layoutParams = lp
                }
                val blendedColor = colorEvaluator.evaluate(fraction, fromColor, toColor) as Int
                gd.setColor(blendedColor)
            }
            start()
        }
    }

    private fun roundedOutlineDrawable(radiusPx: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = radiusPx.toFloat()
            setColor(Color.parseColor("#1AFFFFFF"))
        }
    }

    private fun dp(value: Int): Int {
        val density = context.resources.displayMetrics.density
        return (value * density).roundToInt()
    }
}
