package com.skybridg.linexware.app

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.caverock.androidsvg.SVG
import java.io.File
import java.io.FileInputStream

object SvgView {

    @JvmStatic
    fun nopull(v: String) {
    }

    @JvmStatic
    fun GetFileSvg(input: String, img: ImageView) {
        try {
            val startDir = File(input)
            val fileInputStream = FileInputStream(startDir)
            val svg = SVG.getFromInputStream(fileInputStream)
            val drawable = PictureDrawable(svg.renderToPicture())
            img.setImageDrawable(drawable)
            fileInputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun GetAsster(context: Context, img: ImageView, input: String) {
        try {
            val ss = SVG.getFromAsset(context.assets, input)
            val d = PictureDrawable(ss.renderToPicture())
            img.setImageDrawable(d)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
