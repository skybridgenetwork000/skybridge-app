package com.skybridg.linexware.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class LottieSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    fun interface OnRefreshListener {
        fun onRefresh()
    }

    companion object {
        private const val DRAG_RATE = 0.5f
        private const val RUBBER_BAND_C = 0.55f
        private const val SNAP_DURATION = 320L
        private val BOUNCE_INTERPOLATOR: Interpolator = OvershootInterpolator(1.3f)
    }

    private lateinit var lottieView: LottieAnimationView
    private var maxHeaderHeightPx = 0
    private var overstretchExtraPx = 0
    private var touchSlop = 0

    private var initialDownY = 0f
    private var isDragging = false
    private var refreshing = false
    private var headerAnimator: ValueAnimator? = null
    private var listener: OnRefreshListener? = null

    init {
        init(context)
    }

    private fun init(context: Context) {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        maxHeaderHeightPx = dpToPx(72)
        overstretchExtraPx = dpToPx(40)

        lottieView = LottieAnimationView(context).apply {
            setAnimation("loading.json")
            repeatCount = LottieDrawable.INFINITE
            progress = 0f
        }

        val headerParams = LayoutParams(LayoutParams.MATCH_PARENT, 0).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        }
        addView(lottieView, 0, headerParams)
    }

    fun setPullDistanceDp(dp: Int) {
        maxHeaderHeightPx = dpToPx(dp)
    }

    fun setOverstretchDistanceDp(dp: Int) {
        overstretchExtraPx = dpToPx(dp)
    }

    fun setOnRefreshListener(listener: OnRefreshListener?) {
        this.listener = listener
    }

    fun isRefreshing(): Boolean = refreshing

    fun setRefreshing(refreshing: Boolean) {
        this.refreshing = refreshing
        if (refreshing) {
            animateHeaderHeight(getHeaderHeight(), maxHeaderHeightPx, BOUNCE_INTERPOLATOR) {
                lottieView.progress = 0f
                lottieView.playAnimation()
            }
        } else {
            lottieView.cancelAnimation()
            animateHeaderHeight(getHeaderHeight(), 0, BOUNCE_INTERPOLATOR) {
                lottieView.progress = 0f
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                initialDownY = ev.y
                isDragging = false
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = ev.y - initialDownY
                if (dy > touchSlop && !refreshing && !canChildScrollUp()) {
                    isDragging = true
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDragging = false
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (!isDragging) {
            return super.onTouchEvent(ev)
        }

        when (ev.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                val dy = ev.y - initialDownY
                val dragged = (dy * DRAG_RATE).coerceAtLeast(0f)
                val height: Int = if (dragged <= maxHeaderHeightPx) {
                    dragged.toInt()
                } else {
                    val overPull = dragged - maxHeaderHeightPx
                    val extra = rubberBand(overPull, overstretchExtraPx.toFloat())
                    (maxHeaderHeightPx + extra).toInt()
                }
                setHeaderHeight(height)

                val progress = if (maxHeaderHeightPx == 0) {
                    0f
                } else {
                    height.coerceAtMost(maxHeaderHeightPx) / maxHeaderHeightPx.toFloat()
                }
                lottieView.progress = progress
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDragging = false
                val currentHeight = getHeaderHeight()
                if (currentHeight >= maxHeaderHeightPx) {
                    setRefreshing(true)
                    listener?.onRefresh()
                } else if (currentHeight > 0) {
                    animateHeaderHeight(currentHeight, 0, BOUNCE_INTERPOLATOR, null)
                }
                return true
            }
        }
        return super.onTouchEvent(ev)
    }

    private fun rubberBand(overPull: Float, maxOver: Float): Float {
        if (maxOver <= 0) return 0f
        return ((1f - (1f / ((overPull * RUBBER_BAND_C / maxOver) + 1f))) * maxOver)
    }

    private fun canChildScrollUp(): Boolean {
        for (i in 1 until childCount) {
            val child = getChildAt(i)
            if (child.canScrollVertically(-1)) {
                return true
            }
        }
        return false
    }

    private fun getHeaderHeight(): Int = lottieView.layoutParams.height

    private fun setHeaderHeight(px: Int) {
        val params = lottieView.layoutParams as LayoutParams
        params.height = px.coerceAtLeast(0)
        lottieView.layoutParams = params
    }

    private fun animateHeaderHeight(from: Int, to: Int, interpolator: Interpolator?, onEnd: Runnable?) {
        headerAnimator?.cancel()
        headerAnimator = ValueAnimator.ofInt(from, to).apply {
            duration = SNAP_DURATION
            if (interpolator != null) {
                setInterpolator(interpolator)
            }
            addUpdateListener { anim ->
                setHeaderHeight(anim.animatedValue as Int)
            }
            if (onEnd != null) {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        onEnd.run()
                    }
                })
            }
            start()
        }
    }

    private fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()
}
