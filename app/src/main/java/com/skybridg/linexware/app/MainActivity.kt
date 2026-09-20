package com.skybridg.linexware.app

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private val timer = Timer()
    private lateinit var linear1: LinearLayout
    private lateinit var splashImage: ImageView
    private lateinit var d_: SharedPreferences
    private var timerTask: TimerTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        initialize(savedInstanceState)
        FirebaseApp.initializeApp(this)
        initializeLogic()
    }

    private fun initialize(savedInstanceState: Bundle?) {
        linear1 = findViewById(R.id.linear1)
        splashImage = findViewById(R.id.splash_image)
        d_ = getSharedPreferences("d_", Activity.MODE_PRIVATE)
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

        _theme_()

        timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val intent = if (d_.contains("authentication")) {
                        Intent(applicationContext, DashboardActivity::class.java)
                    } else {
                        Intent(applicationContext, AuthActivity::class.java)
                    }
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
            }
        }
        timer.schedule(timerTask, 2000L)
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

    override fun onDestroy() {
        super.onDestroy()
        timerTask?.cancel()
        timer.cancel()
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
        splashImage.setImageResource(R.drawable.icc_1)
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
        splashImage.setImageResource(R.drawable.icc_2)
    }
}
