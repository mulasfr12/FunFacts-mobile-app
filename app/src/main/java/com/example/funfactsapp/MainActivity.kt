package com.example.funfactsapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.funfactsapp.databinding.ActivityMainBinding
import com.example.funfactsapp.utils.NotificationHelper
import com.example.funfactsapp.worker.WorkerScheduler
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        // ✅ Load theme preference before setting content view
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        setTheme(if (isDarkMode) R.style.Theme_FunFactsApp_Dark else R.style.Theme_FunFactsApp)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NotificationHelper.createNotificationChannel(this)
        // ✅ Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)

        // ✅ Setup Toolbar & Navigation Drawer
        setupToolbar()
        setupNavigationDrawer()

        // ✅ Schedule Background Worker to Fetch Facts
        WorkerScheduler.scheduleBackgroundFactFetchWorker(this)

        // ✅ Safe Navigation Handling
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        navHostFragment?.let {
            val navController = it.navController
            binding.bottomNavigationView.setupWithNavController(navController)
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)  // ✅ Ensure Toolbar Works

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)  // ✅ Opens Drawer when clicking menu
        }
    }

    private fun setupNavigationDrawer() {
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_dark_mode -> {
                    toggleDarkMode()
                    drawerLayout.closeDrawer(GravityCompat.START) // ✅ Close drawer after selection
                    return@setNavigationItemSelectedListener true
                }
                R.id.menu_close_app -> {
                    showExitConfirmationDialog() // ✅ Show exit confirmation dialog
                    return@setNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun showExitConfirmationDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ -> finishAffinity() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.window?.attributes?.windowAnimations = R.style.DialogSlideUpAnimation
        dialog.show()
    }

    private fun toggleDarkMode() {
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        val editor = sharedPreferences.edit()
        editor.putBoolean("dark_mode", !isDarkMode)
        editor.apply()

        // ✅ Apply Theme Change
        AppCompatDelegate.setDefaultNightMode(
            if (!isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // ✅ Restart Activity to Apply Changes
        recreate()
    }
}
