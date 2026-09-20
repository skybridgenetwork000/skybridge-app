package com.skybridg.linexware.app

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.util.SparseBooleanArray
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.*

object SketchwareUtil {

    const val TOP = 1
    const val CENTER = 2
    const val BOTTOM = 3

    @JvmStatic
    fun CustomToast(
        context: Context,
        message: String,
        textColor: Int,
        textSize: Int,
        bgColor: Int,
        radius: Int,
        gravity: Int
    ) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        val view = toast.view ?: return
        val textView = view.findViewById<TextView>(android.R.id.message)
        textView.textSize = textSize.toFloat()
        textView.setTextColor(textColor)
        textView.gravity = Gravity.CENTER

        val gradientDrawable = GradientDrawable().apply {
            setColor(bgColor)
            cornerRadius = radius.toFloat()
        }
        view.background = gradientDrawable
        view.setPadding(15, 10, 15, 10)
        view.elevation = 10f

        when (gravity) {
            1 -> toast.setGravity(Gravity.TOP, 0, 150)
            2 -> toast.setGravity(Gravity.CENTER, 0, 0)
            3 -> toast.setGravity(Gravity.BOTTOM, 0, 150)
        }
        toast.show()
    }

    @JvmStatic
    fun CustomToastWithIcon(
        context: Context,
        message: String,
        textColor: Int,
        textSize: Int,
        bgColor: Int,
        radius: Int,
        gravity: Int,
        icon: Int
    ) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        val view = toast.view ?: return
        val textView = view.findViewById<TextView>(android.R.id.message)
        textView.textSize = textSize.toFloat()
        textView.setTextColor(textColor)
        textView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        textView.gravity = Gravity.CENTER
        textView.compoundDrawablePadding = 10

        val gradientDrawable = GradientDrawable().apply {
            setColor(bgColor)
            cornerRadius = radius.toFloat()
        }
        view.background = gradientDrawable
        view.setPadding(10, 10, 10, 10)
        view.elevation = 10f

        when (gravity) {
            1 -> toast.setGravity(Gravity.TOP, 0, 150)
            2 -> toast.setGravity(Gravity.CENTER, 0, 0)
            3 -> toast.setGravity(Gravity.BOTTOM, 0, 150)
        }
        toast.show()
    }

    @JvmStatic
    fun sortListMap(
        listMap: ArrayList<HashMap<String, Any>>?,
        key: String,
        isNumber: Boolean,
        ascending: Boolean
    ) {
        if (listMap == null) return
        listMap.sortWith(Comparator { map1, map2 ->
            if (isNumber) {
                val count1 = (map1[key]?.toString() ?: "0").toIntOrNull() ?: 0
                val count2 = (map2[key]?.toString() ?: "0").toIntOrNull() ?: 0
                if (ascending) count1.compareTo(count2) else count2.compareTo(count1)
            } else {
                val str1 = map1[key]?.toString() ?: ""
                val str2 = map2[key]?.toString() ?: ""
                if (ascending) str1.compareTo(str2) else str2.compareTo(str1)
            }
        })
    }

    @JvmStatic
    fun CropImage(activity: Activity, path: String, requestCode: Int) {
        try {
            val intent = Intent("com.android.camera.action.CROP").apply {
                val file = File(path)
                val contentUri = Uri.fromFile(file)
                setDataAndType(contentUri, "image/*")
                putExtra("crop", "true")
                putExtra("aspectX", 1)
                putExtra("aspectY", 1)
                putExtra("outputX", 280)
                putExtra("outputY", 280)
                putExtra("return-data", false)
            }
            activity.startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Your device doesn't support the crop action!", Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    fun isConnected(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val info = cm?.activeNetworkInfo
            info != null && info.isConnected
        } catch (e: Exception) {
            false
        }
    }

    @JvmStatic
    fun copyFromInputStream(inputStream: InputStream): String {
        val outputStream = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var i: Int
        try {
            while (inputStream.read(buf).also { i = it } != -1) {
                outputStream.write(buf, 0, i)
            }
            outputStream.close()
            inputStream.close()
        } catch (ignored: Exception) {
        }
        return outputStream.toString()
    }

    @JvmStatic
    fun hideKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    @JvmStatic
    fun showKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    @JvmStatic
    fun showMessage(context: Context, s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun getLocationX(view: View): Int {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return location[0]
    }

    @JvmStatic
    fun getLocationY(view: View): Int {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return location[1]
    }

    @JvmStatic
    fun getRandom(min: Int, max: Int): Int {
        return Random().nextInt(max - min + 1) + min
    }

    @JvmStatic
    fun getCheckedItemPositionsToArray(list: ListView): ArrayList<Double> {
        val result = ArrayList<Double>()
        val arr: SparseBooleanArray = list.checkedItemPositions
        for (i in 0 until arr.size()) {
            if (arr.valueAt(i)) {
                result.add(arr.keyAt(i).toDouble())
            }
        }
        return result
    }

    @JvmStatic
    fun getDip(context: Context, input: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            input.toFloat(),
            context.resources.displayMetrics
        )
    }

    @JvmStatic
    fun getDisplayWidthPixels(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    @JvmStatic
    fun getDisplayHeightPixels(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    @JvmStatic
    fun getAllKeysFromMap(map: Map<String, Any>?, output: ArrayList<String>?) {
        if (output == null) return
        output.clear()
        if (map == null || map.isEmpty()) return
        output.addAll(map.keys)
    }
}
