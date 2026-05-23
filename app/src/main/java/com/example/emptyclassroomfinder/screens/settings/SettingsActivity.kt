package com.example.emptyclassroomfinder.screens.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.emptyclassroomfinder.R
import com.example.emptyclassroomfinder.app.Custom
import com.example.emptyclassroomfinder.screens.dashboard.DashboardActivity
import com.example.emptyclassroomfinder.screens.login.LoginActivity
import com.example.emptyclassroomfinder.screens.profile.ProfileActivity
import com.example.emptyclassroomfinder.screens.rooms.RoomsActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity(), SettingsContract.View, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var presenter: SettingsPresenter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var switchDarkMode: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        presenter = SettingsPresenter(this, SettingsModel(application as Custom))

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        switchDarkMode = findViewById(R.id.switchDarkMode)

        setupDrawer()

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            presenter.setDarkMode(isChecked)
        }

        presenter.loadSettings()
    }

    private fun setupDrawer() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener(this)

        val headerView = navView.getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.nav_header_text)
        usernameTextView.text = (application as Custom).defaultUsername
    }

    override fun updateDarkModeStatus(isEnabled: Boolean) {
        switchDarkMode.isChecked = isEnabled
    }

    override fun applyDarkMode(isEnabled: Boolean) {
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_dashboard -> {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
            R.id.nav_rooms -> {
                startActivity(Intent(this, RoomsActivity::class.java))
                finish()
            }
            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            R.id.nav_settings -> drawerLayout.closeDrawer(GravityCompat.START)
            R.id.nav_logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }
}
