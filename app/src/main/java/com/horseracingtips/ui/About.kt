package com.horseracingtips.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.preference.PreferenceManager
import com.horseracingtips.ENGLISH
import com.horseracingtips.LANGUAGE_KEY
import com.horseracingtips.R
import com.horseracingtips.databinding.ActivityAboutBinding
import com.horseracingtips.databinding.ActivityPrivacyBinding
import com.horseracingtips.utils.changeLocale
import com.horseracingtips.utils.getLocale
import com.horseracingtips.utils.languageSetup

class About : AppCompatActivity() {

    private var _binding: ActivityAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var actionBar = supportActionBar
        actionBar?.title = getString(R.string.about_act)
        actionBar?.setDisplayHomeAsUpEnabled(true)
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