package com.horseracingtips.ui

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.horseracingtips.BuildConfig
import com.horseracingtips.ENGLISH
import com.horseracingtips.LANGUAGE_KEY
import com.horseracingtips.R
import com.horseracingtips.databinding.ActivityPrivacyBinding
import com.horseracingtips.databinding.ActivitySettingBinding
import com.horseracingtips.utils.changeLocale
import com.horseracingtips.utils.getLocale
import com.horseracingtips.utils.languageSetup
import android.app.ActivityManager
import android.content.res.Configuration

import android.os.Build
import java.lang.Exception


class Setting : AppCompatActivity() {

    private var _binding: ActivitySettingBinding? = null
    private val binding get() = _binding!!

    var alertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var actionBar = supportActionBar
        actionBar?.title = getString(R.string.setting_act)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val versionName = BuildConfig.VERSION_NAME

        createAlertCache()

//        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
////            Configuration.UI_MODE_NIGHT_YES -> {
////                binding.switch1.isChecked = true
////            }
//            Configuration.UI_MODE_NIGHT_NO -> {
//                binding.switch1.isChecked = true
//            }
////            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
//        }
        binding.switch1.isChecked = this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        binding.apply {

            btnClearCache.setOnClickListener {
                // clear cache
                alertDialog?.show()
            }

            switch1.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

            txtAppVersion.text = versionName

        }
    }

    private fun createAlertCache() {
        val alertDialogBuilder = AlertDialog.Builder(this@Setting)
        alertDialogBuilder.setTitle(getString(R.string.clear_cache_title))
        alertDialogBuilder.setMessage(getString(R.string.clear_cache_warning))
        alertDialogBuilder.setPositiveButton(getString(R.string.yes_txt)) { _: DialogInterface, i: Int ->
            try {
                if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                    (getSystemService(ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
                } else {
                    Runtime.getRuntime().exec("pm clear " + applicationContext.packageName)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel_txt)) { _: DialogInterface, i: Int -> }

        alertDialog = alertDialogBuilder.create()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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