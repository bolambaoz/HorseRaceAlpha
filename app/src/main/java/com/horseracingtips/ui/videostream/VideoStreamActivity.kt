package com.horseracingtips.ui.videostream

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color.BLACK
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.horseracingtips.LANGUAGE_KEY
import com.horseracingtips.ui.login.LoginPage
import com.horseracingtips.R
import com.horseracingtips.data.network.response.HorseRaceResponse
import com.horseracingtips.utils.*
import kotlinx.android.synthetic.main.activity_video_stream.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.lang.Exception

class VideoStreamActivity : AppCompatActivity(),VSListener, KodeinAware {

    override val kodein by kodein()

    private lateinit var videoSteamViewModel: VideoSteamViewModel
    private val factory: VideoStreamFactory by instance()

    private lateinit var linkStr: String
    private lateinit var webViewClient: WebViewClient
    private lateinit var prefs: SharedPreferences

    private var clickLogin = false

    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_stream)
        videoSteamViewModel = ViewModelProvider(this, factory)[VideoSteamViewModel::class.java]
        videoSteamViewModel.vsListener = this

        val intent = intent
        val link = intent.getStringExtra("link")
        val linkLogin = intent.getBooleanExtra("isLogin", false)

        clickLogin = linkLogin

        videoSteamViewModel.linkVideo = link.toString()

        click_here_fb.setOnClickListener {
            this@VideoStreamActivity.toThreeLink(this@VideoStreamActivity, URL_FB_HORSE)
        }

        webViewClient = WebViewClient()
        webview.webViewClient = webViewClient
        webview.setBackgroundColor(BLACK)
        running_text_card.isSelected = true

        videoSteamViewModel.getGliveLink(this.getIpAddres())

        banner_mini.setOnClickListener {
            this.toThreeLink(this, URL_BANNER_3WE)
        }

        btn_to_login.setOnClickListener {
            this.toThreeLink(this, URL_3WE)
            isLoginPopUpRemove()
//            startActivity(Intent(this, LoginPage::class.java).apply {
//            })
        }
        btn_ads.setOnClickListener {
            this@VideoStreamActivity.toThreeLink(this, URL_BANNER_3WE)
        }
        initialLanguageSetup(this)

    }

    private fun initialLanguageSetup(context: Context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val currentLocale = getLocale(context.resources)
        if (!prefs.contains(LANGUAGE_KEY)) null else prefs.getString(LANGUAGE_KEY, currentLocale.toString())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(baseContext, "Landscape Mode", Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT < 16) {
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(baseContext, "Portrait Mode", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (webview != null){
            webview.removeAllViews()
            webview.destroy()
        }
    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = languageSetup(newBase)
        super.attachBaseContext(lang?.let { newBase?.changeLocale(it) })
    }

    override fun onStarted(message: String, from: String) {
        this.snackBar("Loading video...",this,false)
        webview.apply {
            this.loadUrl(message)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.builtInZoomControls = false
            settings.setSupportZoom(false)
            overScrollMode = WebView.OVER_SCROLL_NEVER
            settings.useWideViewPort = true
            setBackgroundColor(0)
            setInitialScale(1)

        }

        Coroutines.main {
            startingAds()
        }

        if (!clickLogin){
            Coroutines.main {
                isLoginPopUp()
            }
        }else{
            isLoginPopUpRemove()
        }
    }

    private suspend fun startingAds(){
        delay(5000)
       try {
           this.popUpAds(this, "")
       }catch (e: Exception){
           Log.d(TAG, "Activity Error")
       }
    }

    private suspend fun isLoginPopUp(){
        delay(10000)
//        webview.destroy()
        pls_login_layout.visibility = View.VISIBLE

    }

    private fun isLoginPopUpRemove(){
//        webview.reload()
        pls_login_layout.visibility = View.GONE
    }

    override fun onSuccess(loginResponse: HorseRaceResponse) {
    }

    override fun onFailure(message: String) {
        toast(message)
    }

    override fun onLoading() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}


//webview.webViewClient = WebViewClient()
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString("rtext", rTxt);
//        outState.putString("ftext", dTxt);
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//
//    }
//