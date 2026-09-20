package com.skybridg.linexware.app

import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Insets
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class ThemeActivity : AppCompatActivity() {

    private lateinit var linear1: LinearLayout
    private lateinit var linear2: LinearLayout
    private lateinit var linear14: LinearLayout
    private lateinit var linear15: LinearLayout
    private lateinit var linear16: LinearLayout
    private lateinit var imageview1: ImageView
    private lateinit var textview2: TextView
    private lateinit var imageview7: ImageView
    private lateinit var textview13: TextView
    private lateinit var ck1: LinearLayout
    private lateinit var imageview8: ImageView
    private lateinit var textview14: TextView
    private lateinit var ck2: LinearLayout
    private lateinit var imageview9: ImageView
    private lateinit var textview15: TextView
    private lateinit var ck3: LinearLayout

    private lateinit var d_: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theme)
        initialize(savedInstanceState)
        FirebaseApp.initializeApp(this)
        initializeLogic()
    }

    private fun initialize(savedInstanceState: Bundle?) {
        linear1 = findViewById(R.id.linear1)
        linear2 = findViewById(R.id.linear2)
        linear14 = findViewById(R.id.linear14)
        linear15 = findViewById(R.id.linear15)
        linear16 = findViewById(R.id.linear16)
        imageview1 = findViewById(R.id.imageview1)
        textview2 = findViewById(R.id.textview2)
        imageview7 = findViewById(R.id.imageview7)
        textview13 = findViewById(R.id.textview13)
        ck1 = findViewById(R.id.ck1)
        imageview8 = findViewById(R.id.imageview8)
        textview14 = findViewById(R.id.textview14)
        ck2 = findViewById(R.id.ck2)
        imageview9 = findViewById(R.id.imageview9)
        textview15 = findViewById(R.id.textview15)
        ck3 = findViewById(R.id.ck3)
        d_ = getSharedPreferences("d_", Activity.MODE_PRIVATE)

        linear14.setOnClickListener {
            d_.edit().putString("theme", "dark").apply()
            _ck(1.0)
            _theme_()
        }

        linear15.setOnClickListener {
            d_.edit().putString("theme", "light").apply()
            _ck(2.0)
            _theme_()
        }

        linear16.setOnClickListener {
            d_.edit().remove("theme").apply()
            _ck(3.0)
            _theme_()
        }

        imageview1.setOnClickListener {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    private fun initializeLogic() {
        if (Build.VERSION.SDK_INT >= 30) {
            val rootView: View = linear1
            val originalLeft = rootView.paddingLeft
            val originalTop = rootView.paddingTop
            val originalRight = rootView.paddingRight
            val originalBottom = rootView.paddingBottom

            rootView.setOnApplyWindowInsetsListener { v, insets ->
                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                val navigationBarInsets = insets.getInsets(WindowInsets.Type.navigationBars())
                val finalLeft = originalLeft + statusBarInsets.left
                val finalTop = originalTop + statusBarInsets.top
                val finalRight = originalRight + statusBarInsets.right
                val finalBottom = originalBottom + navigationBarInsets.bottom
                v.setPadding(finalLeft, finalTop, finalRight, finalBottom)
                insets
            }
        }

        _font_()
        _theme_()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onResume() {
        super.onResume()
        _theme_()
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun _font_() {
        textview2.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview13.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview14.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview15.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
    }

    fun _theme_() {
        if (d_.contains("theme")) {
            if (d_.getString("theme", "") == "dark") {
                _dark_()
                _ck(1.0)
            } else {
                _light_()
                _ck(2.0)
            }
        } else {
            val nightMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            if (nightMode) {
                _dark_()
            } else {
                _light_()
            }
            _ck(3.0)
        }
    }

    private fun roundedDrawable(radiusDp: Int, color: Long): GradientDrawable {
        return GradientDrawable().apply {
            cornerRadius = SketchwareUtil.getDip(applicationContext, radiusDp)
            setColor(color.toInt())
        }
    }

    fun _dark_() {
        window.decorView.systemUiVisibility = 0
        window.statusBarColor = 0xFF000000.toInt()
        window.navigationBarColor = 0xFF000000.toInt()
        linear1.setBackgroundColor(0xFF000000.toInt())
        imageview1.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview1.background = roundedDrawable(18, 0xFF212121L)
        linear14.background = roundedDrawable(20, 0xFF212121L)
        linear15.background = roundedDrawable(20, 0xFF212121L)
        linear16.background = roundedDrawable(20, 0xFF212121L)
        textview2.setTextColor(0xFFFFFFFF.toInt())
        textview13.setTextColor(0xFFFFFFFF.toInt())
        textview14.setTextColor(0xFFFFFFFF.toInt())
        textview15.setTextColor(0xFFFFFFFF.toInt())
        imageview7.background = roundedDrawable(15, 0xFF000000L)
        imageview8.background = roundedDrawable(15, 0xFF000000L)
        imageview9.background = roundedDrawable(15, 0xFF000000L)
        imageview7.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview8.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview9.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        ck1.background = roundedDrawable(100, 0xFFFFFFFFL)
        ck2.background = roundedDrawable(100, 0xFFFFFFFFL)
        ck3.background = roundedDrawable(100, 0xFFFFFFFFL)
    }

    fun _light_() {
        var flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        window.decorView.systemUiVisibility = flags
        window.statusBarColor = 0xFFFFFFFF.toInt()
        window.navigationBarColor = 0xFFFFFFFF.toInt()
        linear1.setBackgroundColor(0xFFFFFFFF.toInt())
        imageview1.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview1.background = roundedDrawable(18, 0xFFEEEEEEL)
        linear14.background = roundedDrawable(20, 0xFFEEEEEEL)
        linear15.background = roundedDrawable(20, 0xFFEEEEEEL)
        linear16.background = roundedDrawable(20, 0xFFEEEEEEL)
        textview2.setTextColor(0xFF000000.toInt())
        textview13.setTextColor(0xFF000000.toInt())
        textview14.setTextColor(0xFF000000.toInt())
        textview15.setTextColor(0xFF000000.toInt())
        imageview7.background = roundedDrawable(15, 0xFFFFFFFFL)
        imageview8.background = roundedDrawable(15, 0xFFFFFFFFL)
        imageview9.background = roundedDrawable(15, 0xFFFFFFFFL)
        imageview7.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview8.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview9.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        ck1.background = roundedDrawable(100, 0xFF000000L)
        ck2.background = roundedDrawable(100, 0xFF000000L)
        ck3.background = roundedDrawable(100, 0xFF000000L)
    }

    fun _ck(_id: Double) {
        ck1.visibility = if (_id == 1.0) View.VISIBLE else View.INVISIBLE
        ck2.visibility = if (_id == 2.0) View.VISIBLE else View.INVISIBLE
        ck3.visibility = if (_id == 3.0) View.VISIBLE else View.INVISIBLE
    }
}
