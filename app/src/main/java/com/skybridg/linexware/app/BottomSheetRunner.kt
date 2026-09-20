package com.skybridg.linexware.app

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetRunner : BottomSheetDialogFragment() {

    companion object {
        private const val SHEET_HEIGHT_PERCENT = 0.9f
        private const val CORNER_RADIUS_DP = 25
        private var fragmentToShow: Fragment? = null

        @JvmStatic
        fun show(activity: FragmentActivity, fragment: Fragment) {
            fragmentToShow = fragment
            val sheet = BottomSheetRunner()
            sheet.show(activity.supportFragmentManager, "bottom_sheet_runner")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = FrameLayout(requireContext()).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentToShow?.let {
            childFragmentManager
                .beginTransaction()
                .replace(view.id, it)
                .commit()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                closeSheet()
                true
            } else {
                false
            }
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog ?: return
        val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) ?: return

        val radiusPx = CORNER_RADIUS_DP * resources.displayMetrics.density
        val bg = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.parseColor("#00000000"))
            cornerRadii = floatArrayOf(radiusPx, radiusPx, radiusPx, radiusPx, 0f, 0f, 0f, 0f)
        }
        bottomSheet.background = bg

        val screenHeight = resources.displayMetrics.heightPixels
        val fixedHeight = (screenHeight * SHEET_HEIGHT_PERCENT).toInt()

        val params = bottomSheet.layoutParams
        params.height = fixedHeight
        bottomSheet.layoutParams = params

        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.peekHeight = fixedHeight
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    fun closeSheet() {
        if (isAdded) dismiss()
    }
}
