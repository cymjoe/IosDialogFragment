package com.cymjoe.testfiosdialogfragmentdemo.iosdialogfragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

import com.cymjoe.testfiosdialogfragmentdemo.MainActivity
import com.cymjoe.testfiosdialogfragmentdemo.R
import com.cymjoe.testfiosdialogfragmentdemo.TestAdapter


class IosDialogView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnTopScrollYListener {


    init {
        init()
    }

    var canTouch = true//是否可以拖动
    private var activity: Activity? = null
    lateinit var inflater: View
    lateinit var rich: WebView
    lateinit var rv: CustomRecyclerView
    lateinit var scroll: CustomScrollView
    lateinit var imgClose: ImageView
    lateinit var ll_bg: LinearLayout
    val duration = 300L//动画时长

    private fun init() {
        inflater = LayoutInflater.from(context).inflate(R.layout.iosdialog_fragment, this)
        ll_bg = inflater.findViewById(R.id.ll_bg)
        rich = inflater.findViewById(R.id.rich)
        rv = inflater.findViewById(R.id.rv)
        scroll = inflater.findViewById(R.id.scroll)
        imgClose = inflater.findViewById(R.id.img_close)
        ll_bg.setOnClickListener {  }
        imgClose.setOnClickListener {
            hint(upY)
        }
        scroll.setOnTopScrollYListener(this)
        rv.setOnTopScrollYListener(this)
        this@IosDialogView.visibility = View.GONE
        val adapter = TestAdapter(
            arrayListOf(
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                ""
            ), context
        )
        rv.adapter = adapter
        rich.loadUrl("https://www.baidu.com/")
        rich.webViewClient = object : WebViewClient() {

        }
    }

    var upY = 0f//手指抬起时的位移距离（Y）
    var downY = 0f//按下坐标（Y）
    var moveY = 0f//位移距离（Y）
    var noTopMove = 0f//未滑动到顶部的移动距离

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (canTouch) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    upY = 0f
                    downY = ev.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isTop && isRvTop) {
                        moveY = ev.rawY - downY - noTopMove
                        onScrollY(moveY)
                        if (moveY + upY > 0) {
                            //滑动时屏蔽webView长点击事件
                            rich.setOnLongClickListener { true }
                            return true
                        }
                    } else {
                        noTopMove = ev.rawY - downY
                    }
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    noTopMove = 0f
                    //取消屏蔽webView长点击事件
                    rich.setOnLongClickListener { false }
                    upY = y
                    if (upY > getScreenHeight() * 0.55f - resources.getDimensionPixelOffset(R.dimen.dp_53)) {
                        hint(upY)
                    } else {
                        show(upY)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    private fun getScreenHeight(): Int {
        val wm = context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
        val point = Point()
        wm.defaultDisplay.getRealSize(point)
        return point.y
    }


    fun show(listener: OnUpdateListener) {
        this.onUpdateListener = listener

        show(getScreenHeight().toFloat())

    }

    private fun show(n: Float) {
        this.visibility = VISIBLE
        val anim =
            ObjectAnimator.ofFloat(this, "translationY", n, 0f)
        anim.duration = duration
        anim.start()
        anim.addUpdateListener { animation -> onUpdateListener?.onUpdate(animation.animatedValue as Float / getScreenHeight()) }

    }

    fun hint(n: Float) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)


        val anim =
            ObjectAnimator.ofFloat(this, "translationY", n, getScreenHeight().toFloat())
        anim.duration = duration
        anim.start()
        anim.addUpdateListener { animation -> onUpdateListener?.onUpdate(animation.animatedValue as Float / getScreenHeight()) }
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                this@IosDialogView.visibility = View.GONE

            }
        })
    }

    var onUpdateListener: OnUpdateListener? = null

    interface OnUpdateListener {
        fun onUpdate(n: Float)
    }

    override fun onScrollY(it: Float) {
        if (it + upY > 0f) {
            y = it + upY
            onUpdateListener?.onUpdate(y / getScreenHeight())

        }
    }

    /**
     * 手指从ScrollView 或者RecyclerView抬起监听
     */
    override fun onUp() {
        upY = y
        //判断当前view移动距离
        if (upY > getScreenHeight() * 0.55f -  resources.getDimensionPixelOffset(R.dimen.dp_53)) {
            hint(upY)
        } else {
            show(upY)
        }
    }

    var isTop = true
    var isRvTop = true

    /**
     * 监听ScrollView是否到顶部
     */
    override fun isTop(isTop: Boolean) {
        this.isTop = isTop
    }

    /**
     * 监听RecyclerView是否到顶部
     */
    override fun isRvTop(isRvTop: Boolean) {
        this.isRvTop = isRvTop
    }

    fun setActivity(mainActivity: MainActivity) {
        this.activity = mainActivity
    }


}