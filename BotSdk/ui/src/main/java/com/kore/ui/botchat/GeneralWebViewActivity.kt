package com.kore.ui.botchat

import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.URLUtil
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.kore.SDKConfig
import com.kore.data.repository.preference.PreferenceRepository
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.general.Log
import com.kore.ui.databinding.GenericWebviewLayoutBinding
import com.kore.ui.utils.BundleConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class GeneralWebViewActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_URL = "url"
        const val EXTRA_HEADER = "header"
    }

    private var webUrl: String = ""
    private lateinit var binding: GenericWebviewLayoutBinding
    private val handler = Handler(Looper.getMainLooper())
    private val prefRepository: PreferenceRepository = PreferenceRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        binding = GenericWebviewLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(
            if (SDKConfig.isUpdateStatusBarColor()) {
                prefRepository.getStringValue(
                    this,
                    THEME_NAME,
                    BundleConstants.STATUS_BAR_COLOR,
                    "")
            } else {
                ""
            }
        )

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL

        binding.webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.mProgress.isVisible = true
                binding.pleaseWait.isVisible = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.mProgress.isVisible = false
                binding.pleaseWait.isVisible = false
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    return false
                } else if (url.startsWith("intent://")) {
                    try {
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        if (intent != null) {
                            startActivity(intent)
                            finish()
                            return true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    return true
                }
                return false
            }
        }

        setUpActionBar(intent.extras?.getString(EXTRA_HEADER))
        binding.webView.setBackgroundColor(Color.WHITE)
        intent.extras?.getString(EXTRA_URL)?.let {
            webUrl = it
            binding.webView.loadUrl(it)
        }

        binding.webView.setDownloadListener { url: String, _: String, contentDisposition: String, mimeType: String, _: Long ->
            downloadFile(url, contentDisposition, mimeType, false)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.webview_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        } else if (itemId == R.id.action_share) {
            onShare()
            return true
        } else if (itemId == R.id.action_download) {
            onDownload()
            return true
        } else if (itemId == R.id.action_copy_link) {
            onCopyLink()
            return true
        }

        return true
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        if (menu.javaClass.simpleName == "MenuBuilder") {
            try {
                val m = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
                m.isAccessible = true
                m.invoke(menu, true)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return super.onMenuOpened(featureId, menu)
    }

    private fun setUpActionBar(title: String?) {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.isVisible = title != null
        title?.let {
            setSupportActionBar(toolbar)
            val actionBar = supportActionBar
            if (actionBar != null) {
                if (SDKConfig.isIsShowActionBar()) {
                    actionBar.setDisplayHomeAsUpEnabled(true)
                    actionBar.setHomeAsUpIndicator(ResourcesCompat.getDrawable(resources, R.drawable.back_arrow, theme))

                    title.let { actionBar.title = title }
                        .runCatching { actionBar.title = ContextCompat.getString(this@GeneralWebViewActivity, R.string.app_name) }
                } else {
                    actionBar.title = ""
                }
            }
        }
    }

    private fun downloadFile(url: String, contentDisposition: String?, mimeType: String?, isFromMenus: Boolean) {
        val request = DownloadManager.Request(url.toUri())

        val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
        request.setTitle(fileName)
        request.setDescription(getString(R.string.downloading_file))

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.allowScanningByMediaScanner()

        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)

        Toast.makeText(this, getString(R.string.downloading_file_name, fileName), Toast.LENGTH_LONG).show()
        if (!isFromMenus) {
            handler.postDelayed(this::finish, 1000)
        }
    }

    private fun onDownload() {
        Toast.makeText(this, getString(R.string.downloading), Toast.LENGTH_LONG).show()
        lifecycleScope.launch {
            val (mimeType, contentDisposition) = withContext(Dispatchers.IO) {
                try {
                    Log.e("Called", this@GeneralWebViewActivity.webUrl)
                    val url = URL(this@GeneralWebViewActivity.webUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "HEAD"
                    connection.connect()

                    val mimeType = connection.contentType
                    val contentDisposition = connection.getHeaderField("Content-Disposition")
                    connection.disconnect()
                    mimeType to contentDisposition
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    null to null
                }
            }
            downloadFile(this@GeneralWebViewActivity.webUrl, contentDisposition, mimeType, true)
        }
    }

    private fun onShare() {
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, webUrl)
        sendIntent.setType("text/plain")
        val chooser = Intent.createChooser(sendIntent, "Share URL via")
        startActivity(chooser)
    }

    private fun onCopyLink() {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", webUrl)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, getString(R.string.copy_to_clipboard), Toast.LENGTH_SHORT).show()
    }

    private fun changeStatusBarColor(color: String) {
        if (Build.VERSION.SDK_INT >= 35) {
            binding.statusBarBg.visibility = View.VISIBLE
            val params = binding.statusBarBg.layoutParams
            params.height = prefRepository.getIntValue(this, THEME_NAME, BundleConstants.STATUS_BAR_HEIGHT, 50)
            binding.statusBarBg.setLayoutParams(params)

            binding.statusBarBg.setBackgroundColor(
                color.takeIf { SDKConfig.isUpdateStatusBarColor() && it.isNotBlank() }?.toColorInt()
                    ?: ContextCompat.getColor(this@GeneralWebViewActivity, R.color.colorPrimary)
            )
        } else if(SDKConfig.isUpdateStatusBarColor()){
            val window = getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(
                if (color.isBlank()) ContextCompat.getColor(
                    this@GeneralWebViewActivity,
                    R.color.colorPrimary
                ) else color.toColorInt()
            )
        }
    }
}