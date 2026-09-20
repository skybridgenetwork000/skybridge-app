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
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import java.util.HashMap

class MenuActivity : AppCompatActivity() {

    private var data: HashMap<String, Any> = HashMap()

    private lateinit var linear1: LinearLayout
    private lateinit var linear2: LinearLayout
    private lateinit var vscroll1: ScrollView
    private lateinit var imageview1: ImageView
    private lateinit var linear3: LinearLayout
    private lateinit var circleimageview1: CircleImageView
    private lateinit var textview1: TextView
    private lateinit var textview28: TextView
    private lateinit var linear10: LinearLayout
    private lateinit var textview2: TextView
    private lateinit var linear4: LinearLayout
    private lateinit var textview24: TextView
    private lateinit var linear38: LinearLayout
    private lateinit var textview19: TextView
    private lateinit var linear30: LinearLayout
    private lateinit var linear25: LinearLayout
    private lateinit var linear42: LinearLayout
    private lateinit var linear27: LinearLayout
    private lateinit var linear12: LinearLayout
    private lateinit var linear13: LinearLayout
    private lateinit var linear18: LinearLayout
    private lateinit var textview9: TextView
    private lateinit var textview10: TextView
    private lateinit var imageview5: ImageView
    private lateinit var linear19: LinearLayout
    private lateinit var textview14: TextView
    private lateinit var imageview6: ImageView
    private lateinit var textview11: TextView
    private lateinit var textview12: TextView
    private lateinit var linear5: LinearLayout
    private lateinit var linear14: LinearLayout
    private lateinit var linear21: LinearLayout
    private lateinit var linear23: LinearLayout
    private lateinit var imageview2: ImageView
    private lateinit var textview3: TextView
    private lateinit var imageview7: ImageView
    private lateinit var textview13: TextView
    private lateinit var imageview9: ImageView
    private lateinit var textview16: TextView
    private lateinit var imageview11: ImageView
    private lateinit var textview17: TextView
    private lateinit var linear39: LinearLayout
    private lateinit var linear41: LinearLayout
    private lateinit var imageview23: ImageView
    private lateinit var textview25: TextView
    private lateinit var imageview25: ImageView
    private lateinit var textview26: TextView
    private lateinit var imageview19: ImageView
    private lateinit var textview22: TextView
    private lateinit var imageview13: ImageView
    private lateinit var textview18: TextView
    private lateinit var imageview27: ImageView
    private lateinit var textview27: TextView

    private lateinit var d_: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)
        initialize(savedInstanceState)
        FirebaseApp.initializeApp(this)
        initializeLogic()
    }

    private fun initialize(savedInstanceState: Bundle?) {
        linear1 = findViewById(R.id.linear1)
        linear2 = findViewById(R.id.linear2)
        vscroll1 = findViewById(R.id.vscroll1)
        imageview1 = findViewById(R.id.imageview1)
        linear3 = findViewById(R.id.linear3)
        circleimageview1 = findViewById(R.id.circleimageview1)
        textview1 = findViewById(R.id.textview1)
        textview28 = findViewById(R.id.textview28)
        linear10 = findViewById(R.id.linear10)
        textview2 = findViewById(R.id.textview2)
        linear4 = findViewById(R.id.linear4)
        textview24 = findViewById(R.id.textview24)
        linear38 = findViewById(R.id.linear38)
        textview19 = findViewById(R.id.textview19)
        linear30 = findViewById(R.id.linear30)
        linear25 = findViewById(R.id.linear25)
        linear42 = findViewById(R.id.linear42)
        linear27 = findViewById(R.id.linear27)
        linear12 = findViewById(R.id.linear12)
        linear13 = findViewById(R.id.linear13)
        linear18 = findViewById(R.id.linear18)
        textview9 = findViewById(R.id.textview9)
        textview10 = findViewById(R.id.textview10)
        imageview5 = findViewById(R.id.imageview5)
        linear19 = findViewById(R.id.linear19)
        textview14 = findViewById(R.id.textview14)
        imageview6 = findViewById(R.id.imageview6)
        textview11 = findViewById(R.id.textview11)
        textview12 = findViewById(R.id.textview12)
        linear5 = findViewById(R.id.linear5)
        linear14 = findViewById(R.id.linear14)
        linear21 = findViewById(R.id.linear21)
        linear23 = findViewById(R.id.linear23)
        imageview2 = findViewById(R.id.imageview2)
        textview3 = findViewById(R.id.textview3)
        imageview7 = findViewById(R.id.imageview7)
        textview13 = findViewById(R.id.textview13)
        imageview9 = findViewById(R.id.imageview9)
        textview16 = findViewById(R.id.textview16)
        imageview11 = findViewById(R.id.imageview11)
        textview17 = findViewById(R.id.textview17)
        linear39 = findViewById(R.id.linear39)
        linear41 = findViewById(R.id.linear41)
        imageview23 = findViewById(R.id.imageview23)
        textview25 = findViewById(R.id.textview25)
        imageview25 = findViewById(R.id.imageview25)
        textview26 = findViewById(R.id.textview26)
        imageview19 = findViewById(R.id.imageview19)
        textview22 = findViewById(R.id.textview22)
        imageview13 = findViewById(R.id.imageview13)
        textview18 = findViewById(R.id.textview18)
        imageview27 = findViewById(R.id.imageview27)
        textview27 = findViewById(R.id.textview27)
        d_ = getSharedPreferences("d_", Activity.MODE_PRIVATE)

        imageview1.setOnClickListener {
            finish()
        }

        linear25.setOnClickListener {
            _chromeTab("https://skybridge.linexware.net.ng/privacy%26policy/")
        }

        linear12.setOnClickListener {
            CustomToast.showInfo(this@MenuActivity, "KYC Verification", "KYC Verification is unavailable at the moment!")
        }

        linear5.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        linear23.setOnClickListener {
            val intent = Intent(applicationContext, ThemeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        linear39.setOnClickListener {
            _chromeTab("https://skybridge.linexware.net.ng/whitepaper/")
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
        _check_data_()
        vscroll1.isVerticalScrollBarEnabled = false
        vscroll1.isHorizontalScrollBarEnabled = false
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
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
        textview9.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview11.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview10.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview12.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview3.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview28.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview1.typeface = Typeface.createFromAsset(assets, "fonts/font_b.ttf")
        textview14.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview13.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview16.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview17.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview25.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview26.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview22.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview18.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview2.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview24.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview19.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
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

    private fun roundedDrawable(radiusDp: Int, color: Long): GradientDrawable {
        return GradientDrawable().apply {
            cornerRadius = SketchwareUtil.getDip(applicationContext, radiusDp)
            setColor(color.toInt())
        }
    }

    private fun customCornersDrawable(topRadiusDp: Int, bottomRadiusDp: Int, color: Long): GradientDrawable {
        val top = SketchwareUtil.getDip(applicationContext, topRadiusDp)
        val btm = SketchwareUtil.getDip(applicationContext, bottomRadiusDp)
        return GradientDrawable().apply {
            setColor(color.toInt())
            cornerRadii = floatArrayOf(top, top, top, top, btm, btm, btm, btm)
        }
    }

    fun _dark_() {
        window.decorView.systemUiVisibility = 0
        window.statusBarColor = 0xFF000000.toInt()
        window.navigationBarColor = 0xFF000000.toInt()
        linear1.setBackgroundColor(0xFF000000.toInt())
        imageview1.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview1.background = roundedDrawable(18, 0xFF212121L)
        imageview2.background = roundedDrawable(15, 0xFF000000L)
        SvgView.GetAsster(applicationContext, imageview5, "profile/shield-security.svg")
        SvgView.GetAsster(applicationContext, imageview6, "profile/gift.svg")
        textview14.background = roundedDrawable(15, 0xFF212121L)
        imageview7.background = roundedDrawable(15, 0xFF000000L)
        imageview9.background = roundedDrawable(15, 0xFF000000L)
        imageview11.background = roundedDrawable(15, 0xFF000000L)
        imageview23.background = roundedDrawable(15, 0xFF000000L)
        imageview25.background = roundedDrawable(15, 0xFF000000L)
        imageview19.background = roundedDrawable(15, 0xFF000000L)
        imageview13.background = roundedDrawable(15, 0xFF000000L)
        imageview27.background = roundedDrawable(15, 0xFF000000L)

        DashUtils.setDashedStroke(linear13, Color.parseColor("#212121"), 3, 15f, 6f, SketchwareUtil.getDip(applicationContext, 20))
        DashUtils.setDashedStroke(linear12, Color.parseColor("#212121"), 3, 15f, 6f, SketchwareUtil.getDip(applicationContext, 20))

        linear5.background = customCornersDrawable(20, 10, 0xFF212121L)
        linear23.background = customCornersDrawable(10, 20, 0xFF212121L)
        linear39.background = customCornersDrawable(20, 10, 0xFF212121L)
        linear41.background = customCornersDrawable(10, 20, 0xFF212121L)
        linear30.background = customCornersDrawable(20, 10, 0xFF212121L)
        linear25.background = customCornersDrawable(10, 20, 0xFF212121L)
        linear42.background = roundedDrawable(20, 0xFF212121L)
        linear14.background = roundedDrawable(10, 0xFF212121L)
        linear21.background = roundedDrawable(10, 0xFF212121L)

        textview1.setTextColor(0xFFFFFFFF.toInt())
        textview28.setTextColor(0xFF616161.toInt())
        textview2.setTextColor(0xFF9E9E9E.toInt())
        textview24.setTextColor(0xFF9E9E9E.toInt())
        textview19.setTextColor(0xFF9E9E9E.toInt())
        textview9.setTextColor(0xFFFFFFFF.toInt())
        textview10.setTextColor(0xFF757575.toInt())
        textview14.setTextColor(0xFFFFFFFF.toInt())
        textview11.setTextColor(0xFFFFFFFF.toInt())
        textview12.setTextColor(0xFF757575.toInt())
        textview3.setTextColor(0xFFFFFFFF.toInt())
        textview13.setTextColor(0xFFFFFFFF.toInt())
        textview16.setTextColor(0xFFFFFFFF.toInt())
        textview17.setTextColor(0xFFFFFFFF.toInt())
        textview25.setTextColor(0xFFFFFFFF.toInt())
        textview26.setTextColor(0xFFFFFFFF.toInt())
        textview22.setTextColor(0xFFFFFFFF.toInt())
        textview18.setTextColor(0xFFFFFFFF.toInt())

        imageview2.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview7.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview9.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview11.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview23.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview25.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview19.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview13.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
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
        imageview2.background = roundedDrawable(15, 0xFFFFFFFFL)
        SvgView.GetAsster(applicationContext, imageview5, "profile/shield-security.svg")
        SvgView.GetAsster(applicationContext, imageview6, "profile/gift.svg")
        textview14.background = roundedDrawable(15, 0xFFEEEEEEL)
        imageview7.background = roundedDrawable(15, 0xFFFFFFFFL)
        imageview9.background = roundedDrawable(15, 0xFFFFFFFFL)
        imageview11.background = roundedDrawable(15, 0xFFFFFFFFL)
        imageview23.background = roundedDrawable(15, 0xFFFFFFFFL)
        imageview25.background = roundedDrawable(15, 0xFFFFFFFFL)
        imageview19.background = roundedDrawable(15, 0xFFFFFFFFL)
        imageview13.background = roundedDrawable(15, 0xFFFFFFFFL)
        imageview27.background = roundedDrawable(15, 0xFFFFFFFFL)

        DashUtils.setDashedStroke(linear13, Color.parseColor("#e0e0e0"), 3, 15f, 6f, SketchwareUtil.getDip(applicationContext, 20))
        DashUtils.setDashedStroke(linear12, Color.parseColor("#e0e0e0"), 3, 15f, 6f, SketchwareUtil.getDip(applicationContext, 20))

        linear5.background = customCornersDrawable(20, 10, 0xFFEEEEEEL)
        linear23.background = customCornersDrawable(10, 20, 0xFFEEEEEEL)
        linear39.background = customCornersDrawable(20, 10, 0xFFEEEEEEL)
        linear41.background = customCornersDrawable(10, 20, 0xFFEEEEEEL)
        linear30.background = customCornersDrawable(20, 10, 0xFFEEEEEEL)
        linear25.background = customCornersDrawable(10, 20, 0xFFEEEEEEL)
        linear42.background = roundedDrawable(20, 0xFFEEEEEEL)
        linear14.background = roundedDrawable(10, 0xFFEEEEEEL)
        linear21.background = roundedDrawable(10, 0xFFEEEEEEL)

        textview1.setTextColor(0xFF000000.toInt())
        textview28.setTextColor(0xFF9E9E9E.toInt())
        textview2.setTextColor(0xFF9E9E9E.toInt())
        textview24.setTextColor(0xFF9E9E9E.toInt())
        textview19.setTextColor(0xFF9E9E9E.toInt())
        textview9.setTextColor(0xFF000000.toInt())
        textview10.setTextColor(0xFF9E9E9E.toInt())
        textview14.setTextColor(0xFF9E9E9E.toInt())
        textview11.setTextColor(0xFF000000.toInt())
        textview12.setTextColor(0xFF9E9E9E.toInt())
        textview3.setTextColor(0xFF000000.toInt())
        textview13.setTextColor(0xFF000000.toInt())
        textview16.setTextColor(0xFF000000.toInt())
        textview17.setTextColor(0xFF000000.toInt())
        textview25.setTextColor(0xFF000000.toInt())
        textview26.setTextColor(0xFF000000.toInt())
        textview22.setTextColor(0xFF000000.toInt())
        textview18.setTextColor(0xFF000000.toInt())

        imageview2.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview7.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview9.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview11.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview23.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview25.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview19.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview13.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
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

    fun _check_data_() {
        if (d_.contains("user")) {
            try {
                val type = object : TypeToken<HashMap<String, Any>>() {}.type
                data = Gson().fromJson(d_.getString("user", "{}"), type)
                textview1.text = data["first_name"]?.toString() ?: data["name"]?.toString() ?: "Skybridge User"
                textview28.text = data["email"]?.toString() ?: "user@skybridge.app"
            } catch (e: Exception) {
                textview1.text = "Skybridge User"
                textview28.text = "user@skybridge.app"
            }
        } else {
            textview1.text = "Skybridge User"
            textview28.text = "user@skybridge.app"
        }
    }
}
