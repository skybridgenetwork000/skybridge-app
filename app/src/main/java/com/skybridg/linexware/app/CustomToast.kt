package com.skybridg.linexware.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class CustomToast private constructor(
    private val activity: Activity,
    private val title: String,
    private val message: String,
    private val type: ToastType
) {

    enum class ToastType {
        SUCCESS, ERROR, WARNING, INFO
    }

    enum class ThemeMode {
        AUTO, DARK, LIGHT
    }

    companion object {
        private const val PREFS_NAME = "d_"
        private const val KEY_THEME = "theme"
        private const val FONT_TITLE_PATH = "fonts/title_font.ttf"
        private const val FONT_MESSAGE_PATH = "fonts/message_font.ttf"

        private var themeMode = ThemeMode.AUTO
        private var currentActiveToast: CustomToast? = null

        @JvmStatic
        fun setThemeMode(mode: ThemeMode) {
            themeMode = mode
        }

        @JvmStatic
        fun showSuccess(activity: Activity?, title: String, message: String) {
            show(activity, title, message, ToastType.SUCCESS)
        }

        @JvmStatic
        fun showError(activity: Activity?, title: String, message: String) {
            show(activity, title, message, ToastType.ERROR)
        }

        @JvmStatic
        fun showWarning(activity: Activity?, title: String, message: String) {
            show(activity, title, message, ToastType.WARNING)
        }

        @JvmStatic
        fun showInfo(activity: Activity?, title: String, message: String) {
            show(activity, title, message, ToastType.INFO)
        }

        @JvmStatic
        fun show(activity: Activity?, title: String, message: String, type: ToastType) {
            if (activity == null || activity.isFinishing || activity.isDestroyed) {
                return
            }
            currentActiveToast?.dismiss(false)
            val newToast = CustomToast(activity, title, message, type)
            currentActiveToast = newToast
            newToast.createAndShowLayout()
        }
    }

    private var parentLayout: FrameLayout? = null
    private var toastCard: LinearLayout? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isDismissed = false

    private fun isDarkMode(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.contains(KEY_THEME)) {
            return prefs.getString(KEY_THEME, "") == "dark"
        }
        return when (themeMode) {
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
            ThemeMode.AUTO -> {
                val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                nightModeFlags == Configuration.UI_MODE_NIGHT_YES
            }
        }
    }

    private fun createAndShowLayout() {
        val rootContainer = activity.findViewById<ViewGroup>(android.R.id.content) ?: return
        val context: Context = activity
        val density = context.resources.displayMetrics.density
        val isDark = isDarkMode(context)

        parentLayout = FrameLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                topMargin = (50 * density).toInt()
                leftMargin = (16 * density).toInt()
                rightMargin = (16 * density).toInt()
            }
        }

        toastCard = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.TOP
            val padPx = (16 * density).toInt()
            setPadding(padPx, padPx, padPx, padPx)
            val maxWidth = (380 * density).toInt()
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            clipToOutline = true

            val bgDrawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 24 * density
                if (isDark) {
                    setColor(Color.parseColor("#171717"))
                    setStroke((1 * density).toInt(), Color.parseColor("#4D333333"))
                } else {
                    setColor(Color.parseColor("#FBFBFC"))
                    setStroke((1 * density).toInt(), Color.parseColor("#1A000000"))
                }
            }
            background = bgDrawable
            elevation = if (isDark) 16 * density else 10 * density

            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    if (width > maxWidth) {
                        layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
                            width = maxWidth
                        }
                    }
                }
            })
        }

        val iconView = ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams((24 * density).toInt(), (24 * density).toInt()).apply {
                rightMargin = (12 * density).toInt()
            }
            setImageDrawable(ToastIconDrawable(context, type, isDark))
        }
        toastCard?.addView(iconView)

        val textContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
        }

        val titleView = TextView(context).apply {
            text = title
            setTextColor(if (isDark) Color.WHITE else Color.parseColor("#18181B"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            letterSpacing = -0.015f
            try {
                val customTitleFont = Typeface.createFromAsset(context.assets, FONT_TITLE_PATH)
                setTypeface(customTitleFont, Typeface.BOLD)
            } catch (e: Exception) {
                setTypeface(Typeface.create("sans-serif", Typeface.BOLD))
            }
        }
        textContainer.addView(titleView)

        val messageView = TextView(context).apply {
            text = message
            setTextColor(if (isDark) Color.parseColor("#A1A1AA") else Color.parseColor("#52525B"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setLineSpacing(0f, 1.15f)
            try {
                val customMsgFont = Typeface.createFromAsset(context.assets, FONT_MESSAGE_PATH)
                setTypeface(customMsgFont, Typeface.NORMAL)
            } catch (e: Exception) {
                setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL))
            }
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = (4 * density).toInt()
            }
        }
        textContainer.addView(messageView)

        toastCard?.addView(textContainer)
        parentLayout?.addView(toastCard)
        rootContainer.addView(parentLayout)

        parentLayout?.apply {
            alpha = 0f
            translationY = -200 * density
            scaleX = 0.7f
            scaleY = 0.7f
            animate()
                .alpha(1f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(400)
                .setInterpolator(OvershootInterpolator(1.2f))
                .start()
        }

        handler.postDelayed({ dismiss(true) }, 4000)
    }

    private fun dismiss(animate: Boolean) {
        if (isDismissed) return
        isDismissed = true

        if (currentActiveToast == this) {
            currentActiveToast = null
        }

        val layout = parentLayout ?: return
        if (layout.parent == null) return

        if (animate) {
            val density = activity.resources.displayMetrics.density
            layout.animate()
                .alpha(0f)
                .translationY(-100 * density)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(300)
                .setInterpolator(AccelerateInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        cleanup()
                    }
                }).start()
        } else {
            cleanup()
        }
    }

    private fun cleanup() {
        handler.removeCallbacksAndMessages(null)
        val layout = parentLayout
        if (layout != null && layout.parent != null) {
            (layout.parent as ViewGroup).removeView(layout)
        }
    }

    private class ToastIconDrawable(
        context: Context,
        private val type: ToastType,
        private val isDark: Boolean
    ) : Drawable() {
        private val density: Float = context.resources.displayMetrics.density
        private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        override fun draw(canvas: Canvas) {
            val cx = bounds.exactCenterX()
            val cy = bounds.exactCenterY()
            val r = 10 * density

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2 * density
            paint.strokeCap = Paint.Cap.ROUND
            paint.strokeJoin = Paint.Join.ROUND

            when (type) {
                ToastType.SUCCESS -> {
                    paint.color = Color.parseColor(if (isDark) "#8C9B21" else "#657302")
                    canvas.drawCircle(cx, cy, r, paint)
                    canvas.drawLine(cx - (3.5f * density), cy + (0.5f * density), cx - (1f * density), cy + (3f * density), paint)
                    canvas.drawLine(cx - (1f * density), cy + (3f * density), cx + (4.5f * density), cy - (2.5f * density), paint)
                }
                ToastType.ERROR -> {
                    paint.color = Color.parseColor(if (isDark) "#EF4444" else "#DC2626")
                    canvas.drawCircle(cx, cy, r, paint)
                    canvas.drawLine(cx - (3f * density), cy - (3f * density), cx + (3f * density), cy + (3f * density), paint)
                    canvas.drawLine(cx + (3f * density), cy - (3f * density), cx - (3f * density), cy + (3f * density), paint)
                }
                ToastType.WARNING -> {
                    paint.color = Color.parseColor(if (isDark) "#EAB308" else "#D97706")
                    canvas.drawCircle(cx, cy, r, paint)
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(cx - (0.75f * density), cy - (4f * density), cx + (0.75f * density), cy + (1.5f * density), paint)
                    canvas.drawCircle(cx, cy + (4f * density), 1.0f * density, paint)
                }
                ToastType.INFO -> {
                    paint.color = Color.parseColor(if (isDark) "#3B82F6" else "#2563EB")
                    canvas.drawCircle(cx, cy, r, paint)
                    paint.style = Paint.Style.FILL
                    canvas.drawCircle(cx, cy - (4f * density), 1.0f * density, paint)
                    canvas.drawRect(cx - (0.75f * density), cy - (1.5f * density), cx + (0.75f * density), cy + (4f * density), paint)
                }
            }
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }
}
