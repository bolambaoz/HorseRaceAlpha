package com.horseracingtips.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebSettings
import androidx.preference.PreferenceManager
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.horseracingtips.ENGLISH
import com.horseracingtips.LANGUAGE_KEY
import com.horseracingtips.R
import com.horseracingtips.databinding.ActivityPrivacyBinding
import com.horseracingtips.utils.changeLocale
import com.horseracingtips.utils.getLocale
import com.horseracingtips.utils.languageSetup
import kotlinx.android.synthetic.main.activity_video_stream.*

class Privacy : AppCompatActivity() {

    private var _binding: ActivityPrivacyBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var actionBar = supportActionBar
        actionBar?.title = getString(R.string.privacy_act)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        val url = "file:///android_asset/termcondition.html"

        binding.apply {

            if (this@Privacy.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    WebSettingsCompat.setForceDark(privacyWebview.settings,WebSettingsCompat.FORCE_DARK_ON)
                    privacyWebview.loadUrl(url)
                }
            }else {
                privacyWebview.loadUrl(url)
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = languageSetup(newBase)
        super.attachBaseContext(lang?.let { newBase?.changeLocale(it) })
    }
}