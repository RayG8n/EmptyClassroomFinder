package com.example.emptyclassroomfinder.screens.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.emptyclassroomfinder.R
import com.example.emptyclassroomfinder.app.Custom
import com.example.emptyclassroomfinder.screens.dashboard.DashboardActivity
import com.example.emptyclassroomfinder.screens.groups.GroupsActivity
import com.example.emptyclassroomfinder.screens.login.LoginActivity
import com.example.emptyclassroomfinder.screens.settings.SettingsActivity
import com.google.android.material.navigation.NavigationView

class ProfileActivity : AppCompatActivity(), ProfileContract.View, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var presenter: ProfilePresenter
    private lateinit var textViewWelcome: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        textViewWelcome = findViewById(R.id.textviewUser)
        val buttonBackToLogin = findViewById<Button>(R.id.buttonBackToLogin)

        presenter = ProfilePresenter(this, ProfileModel(application as Custom))
        presenter.initializeUsername()

        setupDrawer()

        buttonBackToLogin.setOnClickListener {
            showLogin()
        }
    }

    private fun setupDrawer() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener(this)
        

        val headerView = navView.getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.nav_header_text)

        usernameTextView.text = "User"
    }

    override fun displayUsername(message: String) {
        textViewWelcome.text = message
    }

    private fun showLogin() {
        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_dashboard -> {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
            R.id.nav_profile -> drawerLayout.closeDrawer(GravityCompat.START)
            R.id.nav_groups -> {
                startActivity(Intent(this, GroupsActivity::class.java))
                finish()
            }
            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                finish()
            }
            R.id.nav_logout -> showLogin()
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