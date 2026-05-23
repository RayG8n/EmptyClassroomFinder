package com.example.emptyclassroomfinder.screens.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import com.example.emptyclassroomfinder.screens.rooms.RoomsActivity
import com.example.emptyclassroomfinder.screens.settings.SettingsActivity
import com.google.android.material.navigation.NavigationView

class ProfileActivity : AppCompatActivity(), ProfileContract.View, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var presenter: ProfilePresenter
    private lateinit var textViewUser: TextView
    private lateinit var edittextNewUsername: EditText
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        textViewUser = findViewById(R.id.textviewUser)
        edittextNewUsername = findViewById(R.id.edittextNewUsername)
        val textviewBackToDashboard = findViewById<TextView>(R.id.textviewBackToDashboard)
        val buttonUpdateProfile = findViewById<Button>(R.id.buttonUpdateProfile)
        val buttonBackToLogin = findViewById<Button>(R.id.buttonBackToLogin)

        presenter = ProfilePresenter(this, ProfileModel(application as Custom))
        presenter.initializeUsername()

        setupDrawer()

        buttonUpdateProfile.setOnClickListener {
            val newName = edittextNewUsername.text.toString()
            presenter.updateUsername(newName)
            updateDrawerHeader(newName)
        }

        textviewBackToDashboard.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

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
        
        updateDrawerHeader((application as Custom).loginUser.username)
    }

    private fun updateDrawerHeader(username: String) {
        val headerView = navView.getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.nav_header_text)
        usernameTextView.text = username
    }

    override fun displayUsername(username: String) {
        textViewUser.text = username
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun clearNewUsernameField() {
        edittextNewUsername.text.clear()
    }

    private fun showLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
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
            R.id.nav_groups -> {
                startActivity(Intent(this, GroupsActivity::class.java))
                finish()
            }
            R.id.nav_profile -> drawerLayout.closeDrawer(GravityCompat.START)
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