package com.horseracingtips

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import com.horseracingtips.databinding.ActivityMainBinding
import com.horseracingtips.utils.changeLocale
import com.horseracingtips.utils.getLocale
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private lateinit var prefs: SharedPreferences

    lateinit var currentLocale: Locale

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

       initialLanguageSetup(this)

    }

    private fun initialLanguageSetup(context: Context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        currentLocale = getLocale(context.resources)
        if (!prefs.contains(LANGUAGE_KEY)) null else prefs.getString(LANGUAGE_KEY, currentLocale.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_to_english -> {
                if (currentLocale.toString() != ENGLISH){changeLanguage(ENGLISH)}
            }
            R.id.action_to_chinese -> {
                if (currentLocale.toString() != CHINESE){changeLanguage(CHINESE)}
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun attachBaseContext(newBase: Context?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(newBase)
        val currentLocale = newBase?.let { getLocale(it.resources) }
        var lang = if (!prefs.contains(LANGUAGE_KEY)) ENGLISH else prefs.getString(LANGUAGE_KEY, currentLocale.toString())
        super.attachBaseContext(lang?.let { newBase?.changeLocale(it) })
    }

    private fun changeLanguage(str: String) {
        when (str) {
            ENGLISH -> {
                prefs.edit().putString(LANGUAGE_KEY, str).commit()
                refreshMain()
            }
            CHINESE -> {
                prefs.edit().putString(LANGUAGE_KEY, str).commit()
                refreshMain()
            }
        }
    }

    private fun refreshMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

const val ENGLISH = "en"
const val CHINESE = "zh"
const val LANGUAGE_KEY = "LANGUAGE"


//override val kodein by kodein()
//mainViewModel = ViewModelProvider(this,factory)[MainViewModel::class.java]
//private lateinit var mainViewModel: MainViewModel
//private val factory: MainFactory by instance()
//private fun initialWindowSetup() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
//        }
//
//        WindowCompat.getInsetsController(window, window.decorView)?.apply {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            )
//        }
//
//    }