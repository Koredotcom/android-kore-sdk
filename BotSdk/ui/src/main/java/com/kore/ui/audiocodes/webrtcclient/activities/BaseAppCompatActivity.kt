package com.kore.ui.audiocodes.webrtcclient.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.general.AppUtils
import com.kore.ui.audiocodes.webrtcclient.general.Log
import com.kore.ui.audiocodes.webrtcclient.db.MySQLiteHelper

open class BaseAppCompatActivity : AppCompatActivity() {
    @JvmField
    var handler: Handler = Handler(Looper.getMainLooper())

    @JvmField
    var hasToolbar = true
    private var toolbar: Toolbar? = null
    private var layoutView: View? = null
    private lateinit var dataBase: MySQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        AppUtils.checkOrientation(this)
        super.onCreate(savedInstanceState)
        handler = Handler(Looper.myLooper()!!)
    }

    fun applicationContext(): Context {
        return applicationContext
    }

    fun initDataBase() {
        dataBase = MySQLiteHelper(applicationContext())
    }

    fun getDataBase(): MySQLiteHelper {
        return dataBase;
    }

    fun startNextActivity(cls: Class<*>?) {
        val intent = Intent(this@BaseAppCompatActivity, cls)
        startActivity(intent)
        finish()
    }

    override fun setContentView(layoutResID: Int) {
        layoutView = View.inflate(this, layoutResID, null)
        setContentView(layoutView)
        //addToolbar();
    }

    override fun setContentView(view: View?) {
        layoutView = view
        super.setContentView(view)
        addToolbar()
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        layoutView = view
        super.setContentView(view, params)
        addToolbar()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (hasToolbar) {
            Log.d(TAG, "onPrepareOptionsMenu: " + menu.hasVisibleItems())
            Log.d(TAG, "onPrepareOptionsMenu: " + menu.size())
            adjustTitleBar(menu.size() > 0)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    fun addToolbar() {
        if (hasToolbar) {
            val tmptoolBar = LayoutInflater.from(this).inflate(R.layout.custom_toolbar, layoutView as ViewGroup?, false)
            (layoutView as ViewGroup?)!!.addView(tmptoolBar, 0)
            toolbar = tmptoolBar.findViewById(R.id.custom_toolbar_id)
            toolbar?.title = "Kore Virtual Assist"
            toolbar?.visibility = View.VISIBLE
            setSupportActionBar(toolbar)
        }
    }

    fun adjustTitleBar(hasMenu: Boolean) {
        Log.d(TAG, "adjustTitleBar, hasMenu: $hasMenu")

        // menu button
        var layoutMarginLeft = 0 //value in dp - this is the size of the menu icon
        Log.d(TAG, "toolBar.getPaddingLeft(); " + toolbar!!.paddingLeft)
        if (hasMenu) {
            val paddingLeft = toolbar!!.paddingLeft
            layoutMarginLeft = if (paddingLeft != 12) {
                40 //value in dp - this is the size of the menu icon
            } else {
                36
            }
        }
        Log.d(TAG, "layoutMarginLeft: $layoutMarginLeft")
        val layoutMarginLeftInPixel = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, layoutMarginLeft.toFloat(), resources
                .displayMetrics
        ).toInt()
        val image = findViewById<ImageView>(R.id.toolbar_title)
        val tvToolBarTitle = findViewById<TextView>(R.id.tvToolBarTitle)
        val marginParams = image.layoutParams as LinearLayout.LayoutParams
        marginParams.setMargins(layoutMarginLeftInPixel, 0, 0, 0)
        image.layoutParams = marginParams
        tvToolBarTitle.layoutParams = marginParams
        tvToolBarTitle.text = "Kore Virtual Assist"
    }

    companion object {
        private const val TAG = "BaseAppCompatActivity"
    }
}