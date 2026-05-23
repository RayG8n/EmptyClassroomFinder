package com.example.emptyclassroomfinder.screens.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.emptyclassroomfinder.R
import com.example.emptyclassroomfinder.app.Custom
import com.example.emptyclassroomfinder.screens.groups.GroupsActivity
import com.example.emptyclassroomfinder.screens.login.LoginActivity
import com.example.emptyclassroomfinder.screens.profile.ProfileActivity
import com.example.emptyclassroomfinder.screens.rooms.RoomsActivity
import com.example.emptyclassroomfinder.screens.settings.SettingsActivity
import com.google.android.material.navigation.NavigationView

class DashboardActivity : AppCompatActivity(), DashboardContract.View, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var presenter: DashboardPresenter
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        presenter = DashboardPresenter(this, DashboardModel(application as Custom))

        setupDrawer()
        setupButtons()
        
        presenter.loadUserInfo()
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
    }

    private fun setupButtons() {
        val cardRooms = findViewById<CardView>(R.id.cardRooms)
        val cardGroups = findViewById<CardView>(R.id.cardGroups)
        val cardProfile = findViewById<CardView>(R.id.cardProfile)
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)

        cardRooms.setOnClickListener {
            val intent = Intent(this, RoomsActivity::class.java)
            startActivity(intent)
        }

        cardGroups.setOnClickListener {
            showGroups()
        }

        cardProfile.setOnClickListener {
            showProfile()
        }

        buttonLogout.setOnClickListener {
            showLogin()
        }
    }

    override fun updateUserInfo(username: String) {
        val headerView = navView.getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.nav_header_text)
        usernameTextView.text = username
    }

    override fun showProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    override fun showGroups() {
        val intent = Intent(this, GroupsActivity::class.java)
        startActivity(intent)
    }

    override fun showSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun showLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        presenter.handleNavigation(item.itemId)
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
