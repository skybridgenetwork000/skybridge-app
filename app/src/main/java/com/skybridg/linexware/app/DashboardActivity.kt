package com.skybridg.linexware.app

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.AutoTransition
import android.transition.TransitionManager
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
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class DashboardActivity : AppCompatActivity() {

    private var st_ = ""
    private var lt_ = ""
    private var text_ = 0
    private var isBalanceHidden = false
    private var isMiningActive = false
    private var currentMiningSession = 0.000
    private var currentBalance = 1250.750

    private var data: HashMap<String, Any> = HashMap()
    private val images = ArrayList<String>()

    private lateinit var linear1: LinearLayout
    private lateinit var linear2: LinearLayout
    private lateinit var swipe: LottieSwipeRefreshLayout
    private lateinit var linear23: LinearLayout
    private lateinit var imageview2: ImageView
    private lateinit var textview24: TextView
    private lateinit var vscroll1: ScrollView
    private lateinit var linear6: LinearLayout
    private lateinit var linear5: LinearLayout
    private lateinit var linear7: LinearLayout
    private lateinit var linCaro: LinearLayout
    private lateinit var linear24: LinearLayout
    private lateinit var textview27: TextView
    private lateinit var linear26: LinearLayout
    private lateinit var linear3: LinearLayout
    private lateinit var linear11: LinearLayout
    private lateinit var linear9: LinearLayout
    private lateinit var textview5: TextView
    private lateinit var line: LinearLayout
    private lateinit var linear10: LinearLayout
    private lateinit var textview7: TextView
    private lateinit var textview6: TextView
    private lateinit var linear4: LinearLayout
    private lateinit var visibleHideIcon: ImageView
    private lateinit var balanceBigText: TextView
    private lateinit var balanceSmallText: TextView
    private lateinit var textview4: TextView
    private lateinit var linear12: LinearLayout
    private lateinit var linear13: LinearLayout
    private lateinit var currentPriceText: TextView
    private lateinit var textview10: TextView
    private lateinit var totalSupplyText: TextView
    private lateinit var textview12: TextView
    private lateinit var textview13: TextView
    private lateinit var linear18: LinearLayout
    private lateinit var linear19: LinearLayout
    private lateinit var textview17: TextView
    private lateinit var miningRate: TextView
    private lateinit var linear20: LinearLayout
    private lateinit var linear21: LinearLayout
    private lateinit var miningButtonText: TextView
    private lateinit var currentMiningCount: TextView
    private lateinit var textview23: TextView
    private lateinit var linear25: LinearLayout
    private lateinit var text: TextView
    private lateinit var textview26: TextView
    private lateinit var imageview4: ImageView
    private lateinit var imageview5: ImageView
    private lateinit var imageview6: ImageView
    private lateinit var imageview7: ImageView

    private lateinit var d_: SharedPreferences
    private val handler = Handler(Looper.getMainLooper())
    private var miningRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        initialize(savedInstanceState)
        FirebaseApp.initializeApp(this)
        initializeLogic()
    }

    private fun initialize(savedInstanceState: Bundle?) {
        linear1 = findViewById(R.id.linear1)
        linear2 = findViewById(R.id.linear2)
        swipe = findViewById(R.id.swipe)
        linear23 = findViewById(R.id.linear23)
        imageview2 = findViewById(R.id.imageview2)
        textview24 = findViewById(R.id.textview24)
        vscroll1 = findViewById(R.id.vscroll1)
        linear6 = findViewById(R.id.linear6)
        linear5 = findViewById(R.id.linear5)
        linear7 = findViewById(R.id.linear7)
        linCaro = findViewById(R.id.linCaro)
        linear24 = findViewById(R.id.linear24)
        textview27 = findViewById(R.id.textview27)
        linear26 = findViewById(R.id.linear26)
        linear3 = findViewById(R.id.linear3)
        linear11 = findViewById(R.id.linear11)
        linear9 = findViewById(R.id.linear9)
        textview5 = findViewById(R.id.textview5)
        line = findViewById(R.id.line)
        linear10 = findViewById(R.id.linear10)
        textview7 = findViewById(R.id.textview7)
        textview6 = findViewById(R.id.textview6)
        linear4 = findViewById(R.id.linear4)
        visibleHideIcon = findViewById(R.id.visible_hide_icon)
        balanceBigText = findViewById(R.id.balance_big_text)
        balanceSmallText = findViewById(R.id.balance_small_text)
        textview4 = findViewById(R.id.textview4)
        linear12 = findViewById(R.id.linear12)
        linear13 = findViewById(R.id.linear13)
        currentPriceText = findViewById(R.id.current_price_text)
        textview10 = findViewById(R.id.textview10)
        totalSupplyText = findViewById(R.id.total_supply_text)
        textview12 = findViewById(R.id.textview12)
        textview13 = findViewById(R.id.textview13)
        linear18 = findViewById(R.id.linear18)
        linear19 = findViewById(R.id.linear19)
        textview17 = findViewById(R.id.textview17)
        miningRate = findViewById(R.id.mining_rate)
        linear20 = findViewById(R.id.linear20)
        linear21 = findViewById(R.id.linear21)
        miningButtonText = findViewById(R.id.mining_button_text)
        currentMiningCount = findViewById(R.id.current_mining_count)
        textview23 = findViewById(R.id.textview23)
        linear25 = findViewById(R.id.linear25)
        text = findViewById(R.id.text)
        textview26 = findViewById(R.id.textview26)
        imageview4 = findViewById(R.id.imageview4)
        imageview5 = findViewById(R.id.imageview5)
        imageview6 = findViewById(R.id.imageview6)
        imageview7 = findViewById(R.id.imageview7)
        d_ = getSharedPreferences("d_", Activity.MODE_PRIVATE)

        imageview2.setOnClickListener {
            val intent = Intent(applicationContext, MenuActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        linear25.setOnClickListener {
            TransitionManager.beginDelayedTransition(linear1, AutoTransition())
            if (text_ == 0) {
                text.text = lt_
                imageview4.rotation = 180f
                text_ = 1
            } else {
                text.text = st_
                imageview4.rotation = 0f
                text_ = 0
            }
        }

        visibleHideIcon.setOnClickListener {
            isBalanceHidden = !isBalanceHidden
            if (isBalanceHidden) {
                balanceBigText.text = "****"
                balanceSmallText.text = ""
                visibleHideIcon.alpha = 0.4f
            } else {
                _set_balance_(String.format(java.util.Locale.US, "%.3f", currentBalance))
                visibleHideIcon.alpha = 1.0f
            }
        }

        miningButtonText.setOnClickListener {
            isMiningActive = !isMiningActive
            if (isMiningActive) {
                miningButtonText.text = "Mining Active"
                CustomToast.showSuccess(this@DashboardActivity, "Mining Started", "Your cloud node is now active!")
                startMiningLoop()
            } else {
                miningButtonText.text = "Start Mining"
                stopMiningLoop()
                CustomToast.showInfo(this@DashboardActivity, "Mining Paused", "Mining node has stopped.")
            }
        }

        swipe.setOnRefreshListener {
            handler.postDelayed({
                swipe.setRefreshing(false)
                _check_data_()
                CustomToast.showSuccess(this@DashboardActivity, "Updated", "Data updated successfully")
            }, 1200L)
        }
    }

    private fun startMiningLoop() {
        stopMiningLoop()
        miningRunnable = object : Runnable {
            override fun run() {
                if (isMiningActive) {
                    currentMiningSession += 0.005
                    currentBalance += 0.005
                    currentMiningCount.text = String.format(java.util.Locale.US, "+%.3f", currentMiningSession)
                    if (!isBalanceHidden) {
                        _set_balance_(String.format(java.util.Locale.US, "%.3f", currentBalance))
                    }
                    saveLocalBalance()
                    handler.postDelayed(this, 1000L)
                }
            }
        }
        handler.post(miningRunnable!!)
    }

    private fun stopMiningLoop() {
        miningRunnable?.let { handler.removeCallbacks(it) }
        miningRunnable = null
    }

    private fun saveLocalBalance() {
        val backupJson = JSONObject().apply {
            put("balance", currentBalance.toString())
        }.toString()
        d_.edit().putString("backup", backupJson).apply()
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

        TransitionManager.beginDelayedTransition(linear1, AutoTransition())
        vscroll1.isVerticalScrollBarEnabled = false
        vscroll1.isHorizontalScrollBarEnabled = false

        st_ = "Welcome to SkyBridge Network, \n\na community driven crypto ecosystem built to make digital assets more accessible, transparent, and rewarding for everyone.\n\n Our mission is to combine the power of artificial intelligence with blockchain innovation to create a smarter and fairer mining experience where every member has the opportunity to participate in the growth of the network."
        lt_ = "Welcome to SkyBridge Network, \n\na community driven crypto ecosystem built to make digital assets more accessible, transparent, and rewarding for everyone.\n\n Our mission is to combine the power of artificial intelligence with blockchain innovation to create a smarter and fairer mining experience where every member has the opportunity to participate in the growth of the network.\n\nSkyBridge is more than a mining application. It is a growing digital community designed around trust, transparency, and long term sustainability. We believe cryptocurrency should be simple to understand and available to everyone, regardless of experience. By using intelligent technologies and a user focused approach, we are building a platform that removes unnecessary complexity while creating a secure environment for our global community.\n\nAs the SkyBridge ecosystem continues to evolve, members will gain access to exciting features, community events, ecosystem rewards, and future utilities that support the growth of the network. Our native community coin is currently under development, and we are working towards the launch of our mainnet with a strong focus on security, reliability, and decentralization. Every contribution from our community helps shape the future of the project.\n\nOur vision is to build a next generation social cryptocurrency that is powered by its community and strengthened through innovation. Together, we are creating a network where technology, transparency, and collaboration come together to unlock new opportunities in the digital economy.\n\nThank you for joining SkyBridge Network. This is only the beginning of an exciting journey toward a smarter, more connected, and community powered future of cryptocurrency. Together, we are building the bridge to the next generation of digital finance."
        text.text = st_
        imageview4.rotation = 0f
        text_ = 0

        images.clear()
        images.add("https://skybridge.linexware.net.ng/images/file_00000000793481f491934f4f2b9b976c%20%281%29.png")
        images.add("https://skybridge.linexware.net.ng/images/file_00000000ceec820a85df2c0a6f233727.png")
        AutoCarousel(this@DashboardActivity, linCaro, images)
            .setCornerRadius(20)
            .setPlaceholder(R.drawable.carousel_im)
            .start()
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
        _check_data_()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMiningLoop()
    }

    fun _font_() {
        balanceBigText.typeface = Typeface.createFromAsset(assets, "fonts/font_b.ttf")
        balanceSmallText.typeface = Typeface.createFromAsset(assets, "fonts/font_b.ttf")
        textview4.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview5.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview6.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview7.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        currentPriceText.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        totalSupplyText.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview10.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview12.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview13.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textview17.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        miningRate.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        currentMiningCount.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview23.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        miningButtonText.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
        textview24.typeface = Typeface.createFromAsset(assets, "fonts/font_b.ttf")
        textview26.typeface = Typeface.createFromAsset(assets, "fonts/font_b.ttf")
        text.typeface = Typeface.createFromAsset(assets, "fonts/font_sb.ttf")
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
        linear3.background = roundedDrawable(20, 0xFF212121L)
        textview6.background = roundedDrawable(10, 0x2000E676L)
        linear12.background = roundedDrawable(15, 0xFF0E0E0EL)
        linear13.background = roundedDrawable(15, 0xFF0E0E0EL)
        linear19.background = roundedDrawable(15, 0xFF212121L)
        textview13.background = roundedDrawable(10, 0xFF212121L)
        linear24.background = roundedDrawable(20, 0xFF212121L)
        text.background = roundedDrawable(15, 0xFF0E0E0EL)
        miningButtonText.background = roundedDrawable(15, 0xFF0E0E0EL)
        DashUtils.setDashedStroke(linear7, Color.parseColor("#212121"), 3, 15f, 6f, SketchwareUtil.getDip(applicationContext, 20))
        line.setBackgroundColor(0xFF000000.toInt())
        imageview2.setColorFilter(0xFF9E9E9E.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview4.setColorFilter(0xFFEEEEEE.toInt(), PorterDuff.Mode.MULTIPLY)
        visibleHideIcon.setColorFilter(0xFFFFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
        textview24.setTextColor(0xFFE0E0E0.toInt())
        textview27.setTextColor(0xFFBDBDBD.toInt())
        textview5.setTextColor(0xFFE0E0E0.toInt())
        textview7.setTextColor(0xFFEEEEEE.toInt())
        balanceBigText.setTextColor(0xFFFFFFFF.toInt())
        balanceSmallText.setTextColor(0xFF9E9E9E.toInt())
        textview4.setTextColor(0xFFBDBDBD.toInt())
        currentPriceText.setTextColor(0xFFE0E0E0.toInt())
        textview10.setTextColor(0xFF757575.toInt())
        totalSupplyText.setTextColor(0xFFE0E0E0.toInt())
        textview12.setTextColor(0xFF757575.toInt())
        textview13.setTextColor(0xFFE0E0E0.toInt())
        textview17.setTextColor(0xFF9E9E9E.toInt())
        miningRate.setTextColor(0xFFE0E0E0.toInt())
        miningButtonText.setTextColor(0xFFEEEEEE.toInt())
        currentMiningCount.setTextColor(0xFFE0E0E0.toInt())
        textview23.setTextColor(0xFF757575.toInt())
        text.setTextColor(0xFFE0E0E0.toInt())
        textview26.setTextColor(0xFFEEEEEE.toInt())
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
        linear3.background = roundedDrawable(20, 0xFFEEEEEEL)
        textview6.background = roundedDrawable(10, 0x2000E676L)
        linear12.background = roundedDrawable(15, 0xFFFFFFFFL)
        linear13.background = roundedDrawable(15, 0xFFFFFFFFL)
        linear19.background = roundedDrawable(15, 0xFFEEEEEEL)
        textview13.background = roundedDrawable(10, 0xFFEEEEEEL)
        linear24.background = roundedDrawable(20, 0xFFEEEEEEL)
        text.background = roundedDrawable(15, 0xFFFFFFFFL)
        miningButtonText.background = roundedDrawable(15, 0xFF000000L)
        DashUtils.setDashedStroke(linear7, Color.parseColor("#e0e0e0"), 3, 15f, 6f, SketchwareUtil.getDip(applicationContext, 20))
        line.setBackgroundColor(0xFFE0E0E0.toInt())
        imageview2.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        imageview4.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        visibleHideIcon.setColorFilter(0xFF000000.toInt(), PorterDuff.Mode.MULTIPLY)
        textview24.setTextColor(0xFF000000.toInt())
        textview27.setTextColor(0xFF212121.toInt())
        textview5.setTextColor(0xFF0E0E0E.toInt())
        textview7.setTextColor(0xFF000000.toInt())
        balanceBigText.setTextColor(0xFF000000.toInt())
        balanceSmallText.setTextColor(0xFF757575.toInt())
        textview4.setTextColor(0xFF757575.toInt())
        currentPriceText.setTextColor(0xFF000000.toInt())
        textview10.setTextColor(0xFF9E9E9E.toInt())
        totalSupplyText.setTextColor(0xFF000000.toInt())
        textview12.setTextColor(0xFF9E9E9E.toInt())
        textview13.setTextColor(0xFF0E0E0E.toInt())
        textview17.setTextColor(0xFF9E9E9E.toInt())
        miningRate.setTextColor(0xFF0E0E0E.toInt())
        miningButtonText.setTextColor(0xFFFFFFFF.toInt())
        currentMiningCount.setTextColor(0xFF0E0E0E.toInt())
        textview23.setTextColor(0xFF9E9E9E.toInt())
        text.setTextColor(0xFF616161.toInt())
        textview26.setTextColor(0xFF000000.toInt())
    }

    fun _set_balance_(balance: String) {
        val decimalLimit = 3
        val wholePart: String
        val decimalPart: String

        if (balance.contains(".")) {
            val dotIndex = balance.indexOf(".")
            wholePart = balance.substring(0, dotIndex)
            val decimals = balance.substring(dotIndex)
            decimalPart = if (decimals.length - 1 > decimalLimit) {
                decimals.substring(0, decimalLimit + 1)
            } else {
                decimals
            }
        } else {
            wholePart = balance
            decimalPart = ".000"
        }

        balanceBigText.text = wholePart
        balanceSmallText.text = decimalPart
    }

    fun _check_data_() {
        if (d_.contains("backup")) {
            try {
                val type = object : TypeToken<HashMap<String, Any>>() {}.type
                data = Gson().fromJson(d_.getString("backup", "{}"), type)
                val bal = data["balance"]?.toString() ?: "1250.750"
                currentBalance = bal.toDoubleOrNull() ?: 1250.750
            } catch (e: Exception) {
                currentBalance = 1250.750
            }
        } else {
            currentBalance = 1250.750
        }
        if (!isBalanceHidden) {
            _set_balance_(String.format(java.util.Locale.US, "%.3f", currentBalance))
        }
    }
}
