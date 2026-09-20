package com.skybridg.linexware.app

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import com.google.firebase.FirebaseApp
import org.json.JSONObject

class AuthActivity : AppCompatActivity() {

    private lateinit var linear1: LinearLayout
    private lateinit var linear8: LinearLayout
    private lateinit var linear2: LinearLayout
    private lateinit var linear3: LinearLayout
    private lateinit var linear4: LinearLayout
    private lateinit var linear7: LinearLayout
    private lateinit var textview3: TextView
    private lateinit var imageview1: ImageView
    private lateinit var textview2: TextView
    private lateinit var linear5: LinearLayout
    private lateinit var textview6: TextView
    private lateinit var linear6: LinearLayout
    private lateinit var imageview2: ImageView
    private lateinit var textview5: TextView

    private lateinit var d_: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth)
        initialize(savedInstanceState)
        FirebaseApp.initializeApp(this)
        initializeLogic()
    }

    private fun initialize(savedInstanceState: Bundle?) {
        linear1 = findViewById(R.id.linear1)
        linear8 = findViewById(R.id.linear8)
        linear2 = findViewById(R.id.linear2)
        linear3 = findViewById(R.id.linear3)
        linear4 = findViewById(R.id.linear4)
        linear7 = findViewById(R.id.linear7)
        textview3 = findViewById(R.id.textview3)
        imageview1 = findViewById(R.id.imageview1)
        textview2 = findViewById(R.id.textview2)
        linear5 = findViewById(R.id.linear5)
        textview6 = findViewById(R.id.textview6)
        linear6 = findViewById(R.id.linear6)
        imageview2 = findViewById(R.id.imageview2)
        textview5 = findViewById(R.id.textview5)

        d_ = getSharedPreferences("d_", Activity.MODE_PRIVATE)

        linear5.setOnClickListener {
            val accountId = "skybridge_user"
            val email = "user@skybridge.app"
            val userJson = JSONObject().apply {
                put("account_id", accountId)
                put("name", "Skybridge User")
                put("email", email)
                put("profile", "")
                put("status", "active")
            }.toString()

            d_.edit()
                .putString("account_id", accountId)
                .putString("email", email)
                .putString("authentication", accountId)
                .putString("user", userJson)
                .putString("backup", "{}")
                .apply()

            CustomToast.showSuccess(this@AuthActivity, "Welcome", "Signed in successfully")

            val intent = Intent(applicationContext, DashboardActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        textview6.setOnClickListener {
            _chromeTab("https://skybridge.linexware.net.ng/privacy%26policy/")
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
        textview2.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview3.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview6.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview5.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
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
        linear1.setBackgroundResource(R.drawable.bgn_2)
        imageview1.setImageResource(R.drawable.icc_1)
        textview2.setTextColor(0xFFFFFFFF.toInt())
        textview3.setTextColor(0xFF9E9E9E.toInt())
        textview5.setTextColor(0xFFFFFFFF.toInt())
        textview6.setTextColor(0xFF757575.toInt())
        textview6.text = Html.fromHtml("On continue clicked you've agreed to our <a href=\"https://linexware.net.ng/ontcoin/\">Terms & Conditions </a>\nand read our <a href=\"https://linexware.net.ng/ontcoin/\">Privacy & Policy </a>.")
        textview6.setLinkTextColor(0xFFFFFFFF.toInt())
        textview6.linksClickable = true

        val gd = GradientDrawable().apply {
            cornerRadius = SketchwareUtil.getDip(applicationContext, 17)
            setColor(0xFF212121.toInt())
        }
        linear5.background = gd
    }

    fun _light_() {
        var flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        window.decorView.systemUiVisibility = flags
        window.statusBarColor = 0xFFFFFFFF.toInt()
        window.navigationBarColor = 0xFFFFFFFF.toInt()
        linear1.setBackgroundResource(R.drawable.bgn_1)
        imageview1.setImageResource(R.drawable.icc_2)
        textview2.setTextColor(0xFF000000.toInt())
        textview3.setTextColor(0xFF9E9E9E.toInt())
        textview5.setTextColor(0xFF000000.toInt())
        textview6.setTextColor(0xFF9E9E9E.toInt())
        textview6.text = Html.fromHtml("On continue clicked you've agreed to our <a href=\"https://linexware.net.ng/ontcoin/\">Terms & Conditions </a>\nand read our <a href=\"https://linexware.net.ng/ontcoin/\">Privacy & Policy </a>.")
        textview6.setLinkTextColor(0xFF000000.toInt())
        textview6.linksClickable = true

        val gd = GradientDrawable().apply {
            cornerRadius = SketchwareUtil.getDip(applicationContext, 17)
            setColor(0xFFE0E0E0.toInt())
        }
        linear5.background = gd
    }

    fun _chromeTab(link: String) {
        val isDark = if (d_.contains("theme")) {
            d_.getString("theme", "") == "dark"
        } else {
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        }

        val color = if (isDark) Color.parseColor("#000000") else Color.parseColor("#FFFFFF")
        val colorSchemeParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(color)
            .build()

        try {
            val customTabsIntent = CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(colorSchemeParams)
                .setShowTitle(true)
                .build()
            customTabsIntent.launchUrl(this, Uri.parse(link))
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(browserIntent)
            } catch (ignored: Exception) {
            }
        }
    }
}
