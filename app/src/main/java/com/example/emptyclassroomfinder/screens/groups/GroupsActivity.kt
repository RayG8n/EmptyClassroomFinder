package com.example.emptyclassroomfinder.screens.groups

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.emptyclassroomfinder.R
import com.example.emptyclassroomfinder.app.Custom
import com.example.emptyclassroomfinder.data.Group
import com.example.emptyclassroomfinder.screens.dashboard.DashboardActivity
import com.example.emptyclassroomfinder.screens.login.LoginActivity
import com.example.emptyclassroomfinder.screens.profile.ProfileActivity
import com.example.emptyclassroomfinder.screens.rooms.RoomsActivity
import com.example.emptyclassroomfinder.utility.getToast
import com.example.emptyclassroomfinder.screens.settings.SettingsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class GroupsActivity : AppCompatActivity(), GroupsContract.View, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var presenter: GroupsPresenter
    private lateinit var listView: ListView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private var allGroups: List<Group> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

        val app = application as Custom
        presenter = GroupsPresenter(this, GroupsModel(app))

        listView = findViewById(R.id.listViewGroups)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAddGroup)
        val fabOptions = findViewById<FloatingActionButton>(R.id.fabGroupOptions)

        setupDrawer()

        fabAdd.setOnClickListener {
            showCreateGroupDialog()
        }

        fabOptions.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add("Join Group")
            popup.menu.add("Leave Group")
            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Join Group" -> showJoinGroupDialog()
                    "Leave Group" -> showLeaveGroupDialog()
                }
                true
            }
            popup.show()
        }

        presenter.loadGroups()
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
        usernameTextView.text = (application as Custom).defaultUsername
    }

    override fun updateGroupsList(groups: List<Group>) {
        this.allGroups = groups
        runOnUiThread {
            listView.adapter = GroupsAdapter(this, groups.toMutableList(), { group ->
                presenter.onGroupClicked(group)
            }, { group ->
                presenter.onGroupLongClicked(group)
            }, { group ->
                presenter.onGroupLongClicked(group)
            })
        }
    }

    override fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showCreateGroupDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Create New Group")

        val input = EditText(this)
        input.hint = "Group Name"
        builder.setView(input)

        builder.setPositiveButton("Create") { _, _ ->
            presenter.createGroup(input.text.toString())
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    override fun showGroupDetails(group: Group) {
        val app = application as Custom
        val isOwner = group.owner == app.defaultUsername
        val isMember = group.members.contains(app.defaultUsername)

        val options = mutableListOf<String>()
        if (!isMember) options.add("Join Group")
        if (isOwner) options.add("Delete Group")
        
        if (options.isEmpty()) {
            Toast.makeText(this, "${group.name} - Members: ${group.members.joinToString()}", Toast.LENGTH_LONG).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle(group.name)
            .setMessage("Owner: ${group.owner}\nMembers: ${group.members.size}")
            .setItems(options.toTypedArray()) { _, which ->
                when (options[which]) {
                    "Join Group" -> presenter.joinGroup(group.name)
                    "Delete Group" -> presenter.deleteGroup(group.name)
                }
            }
            .setNegativeButton("Close", null)
            .show()
    }

    override fun showDeleteConfirmation(group: Group) {
        val app = application as Custom
        if (group.owner != app.defaultUsername) {
            Toast.makeText(this, "Only the owner can delete the group", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Delete Group")
            .setMessage("Do you want to delete ${group.name}?")
            .setPositiveButton("Yes") { _, _ -> presenter.deleteGroup(group.name) }
            .setNegativeButton("No", null)
            .show()
    }

    override fun showJoinGroupDialog() {
        val app = (application as Custom)
        val joinableGroups = allGroups.filter { !it.members.contains(app.defaultUsername) }

        if (joinableGroups.isEmpty()) {
            showMessage("No new groups available to join")
            return
        }

        val names = joinableGroups.map { it.name }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Select Group to Join")
            .setItems(names) { _, which ->
                presenter.joinGroup(names[which])
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun showLeaveGroupDialog() {
        val app = (application as Custom)
        val leavableGroups = allGroups.filter {
            it.members.contains(app.defaultUsername) && it.owner != app.defaultUsername
        }

        if (leavableGroups.isEmpty()) {
            showMessage("You are not in any groups that you can leave")
            return
        }

        val names = leavableGroups.map { it.name }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Select Group to Leave")
            .setItems(names) { _, which ->
                presenter.leaveGroup(names[which])
            }
            .setNegativeButton("Cancel", null)
            .show()
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
            R.id.nav_groups -> drawerLayout.closeDrawer(GravityCompat.START)
            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                finish()
            }
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

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.refresh_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        if (item.itemId == R.id.action_refresh) {
            presenter.loadGroups()
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
