package com.skybridg.linexware.app

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class InvitationActivity : AppCompatActivity() {

    private lateinit var linear1: LinearLayout
    private lateinit var linear2: LinearLayout
    private lateinit var textview3: TextView
    private lateinit var edittext1: EditText
    private lateinit var linear5: LinearLayout
    private lateinit var textview6: TextView
    private lateinit var textview1: TextView
    private lateinit var textview5: TextView

    private lateinit var d_: SharedPreferences
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invitation)
        initialize(savedInstanceState)
        FirebaseApp.initializeApp(this)
        initializeLogic()
    }

    private fun initialize(savedInstanceState: Bundle?) {
        linear1 = findViewById(R.id.linear1)
        linear2 = findViewById(R.id.linear2)
        textview3 = findViewById(R.id.textview3)
        edittext1 = findViewById(R.id.edittext1)
        linear5 = findViewById(R.id.linear5)
        textview6 = findViewById(R.id.textview6)
        textview1 = findViewById(R.id.textview1)
        textview5 = findViewById(R.id.textview5)
        d_ = getSharedPreferences("d_", Activity.MODE_PRIVATE)

        linear5.setOnClickListener {
            val code = edittext1.text.toString().trim()
            if (code.isEmpty()) {
                CustomToast.showWarning(this@InvitationActivity, "Referral Code", "Please enter a valid referral code!")
            } else if (code.length >= 4) {
                d_.edit().putString("referral_code", code).apply()
                CustomToast.showSuccess(this@InvitationActivity, "Success", "Invitation code applied successfully!")
                handler.postDelayed({
                    val intent = Intent(applicationContext, DashboardActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(intent)
                    finish()
                }, 1200L)
            } else {
                CustomToast.showWarning(this@InvitationActivity, "Invalid code", "Please enter a valid referral code!")
            }
        }

        textview6.setOnClickListener {
            val intent = Intent(applicationContext, DashboardActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
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
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onResume() {
        super.onResume()
        _theme_()
    }

    fun _font_() {
        textview1.typeface = Typeface.createFromAsset(assets, "fonts/font_b.ttf")
        textview3.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        edittext1.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview5.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview6.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
    }

    fun _theme_() {
        if (d_.contains("theme")) {
            if (d_.getString("theme", "") == "dark") {
                _dark_()
            } else {
                _light_()
            }
        } else {
            val nightMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            if (nightMode) {
                _dark_()
            } else {
                _light_()
            }
        }
    }

    fun _dark_() {
        window.decorView.systemUiVisibility = 0
        window.statusBarColor = 0xFF000000.toInt()
        window.navigationBarColor = 0xFF000000.toInt()
        linear1.setBackgroundColor(0xFF000000.toInt())

        val btnBg = GradientDrawable().apply {
            cornerRadius = SketchwareUtil.getDip(applicationContext, 15)
            setColor(0xFFE0E0E0.toInt())
        }
        linear5.background = btnBg

        val editBg = GradientDrawable().apply {
            cornerRadius = SketchwareUtil.getDip(applicationContext, 15)
            setColor(0xFF212121.toInt())
        }
        edittext1.background = editBg

        textview1.setTextColor(0xFFFFFFFF.toInt())
        textview3.setTextColor(0xFF9E9E9E.toInt())
        textview5.setTextColor(0xFF000000.toInt())
        textview6.setTextColor(0xFFFFFFFF.toInt())
        edittext1.setTextColor(0xFFFFFFFF.toInt())
        edittext1.setHintTextColor(0xFF9E9E9E.toInt())

        edittext1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val focusBg = GradientDrawable().apply {
                    cornerRadius = SketchwareUtil.getDip(applicationContext, 15)
                    setStroke(2, 0xFFE0E0E0.toInt())
                    setColor(Color.TRANSPARENT)
                }
                edittext1.background = focusBg
            } else {
                val normalBg = GradientDrawable().apply {
                    cornerRadius = SketchwareUtil.getDip(applicationContext, 15)
                    setColor(0xFF212121.toInt())
                }
                edittext1.background = normalBg
            }
        }
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

        val btnBg = GradientDrawable().apply {
            cornerRadius = SketchwareUtil.getDip(applicationContext, 15)
            setColor(0xFF000000.toInt())
        }
        linear5.background = btnBg

        val editBg = GradientDrawable().apply {
            cornerRadius = SketchwareUtil.getDip(applicationContext, 15)
            setColor(0xFFEEEEEE.toInt())
        }
        edittext1.background = editBg

        textview1.setTextColor(0xFF000000.toInt())
        textview3.setTextColor(0xFF9E9E9E.toInt())
        textview5.setTextColor(0xFFFFFFFF.toInt())
        textview6.setTextColor(0xFF000000.toInt())
        edittext1.setTextColor(0xFF000000.toInt())
        edittext1.setHintTextColor(0xFF9E9E9E.toInt())

        edittext1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val focusBg = GradientDrawable().apply {
                    cornerRadius = SketchwareUtil.getDip(applicationContext, 17)
                    setStroke(2, 0xFF000000.toInt())
                    setColor(Color.TRANSPARENT)
                }
                edittext1.background = focusBg
            } else {
                val normalBg = GradientDrawable().apply {
                    cornerRadius = SketchwareUtil.getDip(applicationContext, 17)
                    setColor(0xFFEEEEEE.toInt())
                }
                edittext1.background = normalBg
            }
        }
    }
}
