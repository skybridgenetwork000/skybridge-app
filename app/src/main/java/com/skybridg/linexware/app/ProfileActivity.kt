package com.skybridg.linexware.app

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.HashMap

class ProfileActivity : AppCompatActivity() {

    private var data: HashMap<String, Any> = HashMap()
    private var accountId: String = ""

    private lateinit var linear1: LinearLayout
    private lateinit var linear2: LinearLayout
    private lateinit var vscroll1: ScrollView
    private lateinit var imageview1: ImageView
    private lateinit var textview2: TextView
    private lateinit var linear18: LinearLayout
    private lateinit var linear5: LinearLayout
    private lateinit var linear8: LinearLayout
    private lateinit var linear13: LinearLayout
    private lateinit var linear6: LinearLayout
    private lateinit var linear3: LinearLayout
    private lateinit var linear7: LinearLayout
    private lateinit var textview5: TextView
    private lateinit var textview6: TextView
    private lateinit var linear4: LinearLayout
    private lateinit var textview4: TextView
    private lateinit var textview3: TextView
    private lateinit var linear9: LinearLayout
    private lateinit var linear12: LinearLayout
    private lateinit var textview9: TextView
    private lateinit var textview10: TextView
    private lateinit var linear14: LinearLayout
    private lateinit var linear15: LinearLayout
    private lateinit var linear17: LinearLayout
    private lateinit var textview13: TextView
    private lateinit var imageview2: ImageView
    private lateinit var textview14: TextView
    private lateinit var linear16: LinearLayout
    private lateinit var textview11: TextView
    private lateinit var textview12: TextView

    private lateinit var d_: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        initialize(savedInstanceState)
        FirebaseApp.initializeApp(this)
        initializeLogic()
    }

    private fun initialize(savedInstanceState: Bundle?) {
        linear1 = findViewById(R.id.linear1)
        linear2 = findViewById(R.id.linear2)
        vscroll1 = findViewById(R.id.vscroll1)
        imageview1 = findViewById(R.id.imageview1)
        textview2 = findViewById(R.id.textview2)
        linear18 = findViewById(R.id.linear18)
        linear5 = findViewById(R.id.linear5)
        linear8 = findViewById(R.id.linear8)
        linear13 = findViewById(R.id.linear13)
        linear6 = findViewById(R.id.linear6)
        linear3 = findViewById(R.id.linear3)
        linear7 = findViewById(R.id.linear7)
        textview5 = findViewById(R.id.textview5)
        textview6 = findViewById(R.id.textview6)
        linear4 = findViewById(R.id.linear4)
        textview4 = findViewById(R.id.textview4)
        textview3 = findViewById(R.id.textview3)
        linear9 = findViewById(R.id.linear9)
        linear12 = findViewById(R.id.linear12)
        textview9 = findViewById(R.id.textview9)
        textview10 = findViewById(R.id.textview10)
        linear14 = findViewById(R.id.linear14)
        linear15 = findViewById(R.id.linear15)
        linear17 = findViewById(R.id.linear17)
        textview13 = findViewById(R.id.textview13)
        imageview2 = findViewById(R.id.imageview2)
        textview14 = findViewById(R.id.textview14)
        linear16 = findViewById(R.id.linear16)
        textview11 = findViewById(R.id.textview11)
        textview12 = findViewById(R.id.textview12)
        d_ = getSharedPreferences("d_", Activity.MODE_PRIVATE)

        imageview1.setOnClickListener {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        imageview2.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("clipboard", accountId)
            clipboard.setPrimaryClip(clip)
            CustomToast.showSuccess(this@ProfileActivity, "Copied", "Account ID copied successfully!")
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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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
        textview2.typeface = Typeface.createFromAsset(assets, "fonts/font_b.ttf")
        textview3.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview6.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview10.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview14.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview12.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview4.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview5.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview9.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview13.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview11.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
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

    fun _dark_() {
        window.decorView.systemUiVisibility = 0
        window.statusBarColor = 0xFF000000.toInt()
        window.navigationBarColor = 0xFF000000.toInt()
        linear1.setBackgroundColor(0xFF000000.toInt())
        imageview1.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview1.background = roundedDrawable(18, 0xFF212121L)
        linear5.background = roundedDrawable(20, 0xFF212121L)
        linear13.background = roundedDrawable(20, 0xFF212121L)
        linear8.background = roundedDrawable(20, 0xFF212121L)
        textview4.setTextColor(0xFFFFFFFF.toInt())
        textview5.setTextColor(0xFFFFFFFF.toInt())
        textview9.setTextColor(0xFFFFFFFF.toInt())
        textview13.setTextColor(0xFFFFFFFF.toInt())
        textview2.setTextColor(0xFFFFFFFF.toInt())
        imageview2.setColorFilter(0xFF9E9E9E.toInt(), PorterDuff.Mode.MULTIPLY)
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
        linear5.background = roundedDrawable(20, 0xFFEEEEEEL)
        linear13.background = roundedDrawable(20, 0xFFEEEEEEL)
        linear8.background = roundedDrawable(20, 0xFFEEEEEEL)
        textview4.setTextColor(0xFF000000.toInt())
        textview5.setTextColor(0xFF000000.toInt())
        textview9.setTextColor(0xFF000000.toInt())
        textview13.setTextColor(0xFF000000.toInt())
        textview2.setTextColor(0xFF000000.toInt())
        imageview2.setColorFilter(0xFF9E9E9E.toInt(), PorterDuff.Mode.MULTIPLY)
    }

    fun _check_data_() {
        if (d_.contains("user")) {
            try {
                val type = object : TypeToken<HashMap<String, Any>>() {}.type
                data = Gson().fromJson(d_.getString("user", "{}"), type)
                textview5.text = data["first_name"]?.toString() ?: data["name"]?.toString() ?: "Skybridge"
                textview4.text = data["last_name"]?.toString() ?: "User"
                textview9.text = data["email"]?.toString() ?: "user@skybridge.app"
                accountId = data["account_id"]?.toString() ?: d_.getString("account_id", "skybridge_user") ?: "skybridge_user"
                textview13.text = accountId

                val kyc = data["kyc"]?.toString() ?: "false"
                if (kyc == "true") {
                    textview11.text = "Verified"
                    textview11.setTextColor(0xFF4CAF50.toInt())
                    textview11.background = roundedDrawable(10, 0x2000E676L)
                } else if (kyc == "pending") {
                    textview11.text = "Pending"
                    textview11.setTextColor(0xFF2196F3.toInt())
                    textview11.background = roundedDrawable(10, 0x102196FEL)
                } else {
                    textview11.text = "Unverified"
                    textview11.setTextColor(0xFFF44336.toInt())
                    textview11.background = roundedDrawable(10, 0x10F44336L)
                }
            } catch (e: Exception) {
                textview5.text = "Skybridge"
                textview4.text = "User"
                textview9.text = "user@skybridge.app"
                accountId = d_.getString("account_id", "skybridge_user") ?: "skybridge_user"
                textview13.text = accountId
                textview11.text = "Verified"
                textview11.setTextColor(0xFF4CAF50.toInt())
                textview11.background = roundedDrawable(10, 0x2000E676L)
            }
        } else {
            textview5.text = "Skybridge"
            textview4.text = "User"
            textview9.text = "user@skybridge.app"
            accountId = d_.getString("account_id", "skybridge_user") ?: "skybridge_user"
            textview13.text = accountId
            textview11.text = "Verified"
            textview11.setTextColor(0xFF4CAF50.toInt())
            textview11.background = roundedDrawable(10, 0x2000E676L)
        }
    }
}
