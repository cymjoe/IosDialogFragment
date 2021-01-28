package com.cymjoe.testfiosdialogfragmentdemo

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

import com.cymjoe.testfiosdialogfragmentdemo.iosdialogfragment.IosDialogView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IosDialogView.OnUpdateListener {
    private fun transparentStatusBar(activity: Activity) {
        transparentStatusBar(activity.window)
    }

    private fun transparentStatusBar(window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val vis = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = option or vis
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        transparentStatusBar(this)
        view_bg.setOnClickListener { }
        fragment.setActivity(this)
    }

    fun clicks(view: View) {
        fragment.show(this)
    }


    override fun onBackPressed() {
        if (fragment.visibility == View.VISIBLE) {
            fragment.hint(0f)
        } else {
            super.onBackPressed()
        }
    }

    override fun onUpdate(n: Float) {
        val scales = n * 0.1f + 0.9f
        rl.scaleX = scales
        rl.scaleY = scales

        Log.d("asjdklas", scales.toString())
        if (scales == 1f) {
            rl.background = resources.getDrawable(R.drawable.rec_0_red)
            view_bg.visibility = View.GONE
        } else {
            rl.background = resources.getDrawable(R.drawable.rec_5_red)
            view_bg.visibility = View.VISIBLE
        }
    }

}