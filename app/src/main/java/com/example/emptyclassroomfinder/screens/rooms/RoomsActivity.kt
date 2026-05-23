package com.example.emptyclassroomfinder.screens.rooms

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.emptyclassroomfinder.R
import com.example.emptyclassroomfinder.data.Room
import com.example.emptyclassroomfinder.screens.dashboard.DashboardActivity
import com.example.emptyclassroomfinder.screens.login.LoginActivity
import com.example.emptyclassroomfinder.screens.profile.ProfileActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RoomsActivity : AppCompatActivity(), RoomsContract.View, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var presenter: RoomsPresenter
    private lateinit var listView: ListView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)

        presenter = RoomsPresenter(this, RoomsModel())

        listView = findViewById(R.id.listViewRooms)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAddRoom)

        setupDrawer()

        fabAdd.setOnClickListener {
            showAddRoomDialog()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val room = listView.adapter.getItem(position) as Room
            presenter.onRoomClicked(room)
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val room = listView.adapter.getItem(position) as Room
            presenter.onRoomLongClicked(room)
            true
        }

        presenter.loadRooms()
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

    private fun generateTimeSlots(): List<String> {
        val times = mutableListOf<String>()
        val startHour = 7
        val endHour = 21 // 9:00 PM

        for (hour in startHour..endHour) {
            val amPm = if (hour < 12) "AM" else "PM"
            val displayHour = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }

            times.add(String.format("%d:00 %s", displayHour, amPm))
            if (hour < endHour) {
                times.add(String.format("%d:30 %s", displayHour, amPm))
            }
        }
        return times
    }

    private fun generateRoomsForBuilding(building: String): List<String> {
        val rooms = mutableListOf<String>()
        when (building) {
            "RTL" -> {
                for (i in 101..108) rooms.add(i.toString())
                for (i in 200..230) rooms.add(i.toString())
                for (i in 300..330) rooms.add(i.toString())
                for (i in 400..430) rooms.add(i.toString())
            }
            "GLE" -> {
                for (i in 200..205) rooms.add(i.toString())
                for (i in 300..305) rooms.add(i.toString())
                for (i in 400..405) rooms.add(i.toString())
                for (i in 500..505) rooms.add(i.toString())
            }
            "SAL" -> {
                for (i in 200..220) rooms.add(i.toString())
                for (i in 300..320) rooms.add(i.toString())
                for (i in 400..420) rooms.add(i.toString())
            }
            "Allied", "ACAD" -> {
                for (i in 100..120) rooms.add(i.toString())
                for (i in 200..220) rooms.add(i.toString())
                for (i in 300..320) rooms.add(i.toString())
            }
            "G-lec", "G-smart" -> {
                for (i in 101..105) rooms.add(i.toString())
            }
        }
        return rooms
    }

    override fun updateList(rooms: List<Room>) {
        runOnUiThread {
            listView.adapter = RoomsAdapter(this, rooms.toMutableList())
        }
    }

    override fun showAddRoomDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add New Room")

        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_room, null)
        
        val spinnerBuilding = view.findViewById<Spinner>(R.id.spinnerBuilding)
        val spinnerRoom = view.findViewById<Spinner>(R.id.spinnerRoomName)
        val spinnerStartTime = view.findViewById<Spinner>(R.id.spinnerStartTime)
        val spinnerEndTime = view.findViewById<Spinner>(R.id.spinnerEndTime)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        // Data for buildings
        val buildingsArray = arrayOf("RTL", "GLE", "SAL", "Allied", "ACAD", "G-lec", "G-smart")
        val buildingAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, buildingsArray)
        spinnerBuilding.adapter = buildingAdapter

        // Room spinner update logic
        spinnerBuilding.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedBuilding = buildingsArray[position]
                val rooms = generateRoomsForBuilding(selectedBuilding)
                val roomAdapter = ArrayAdapter(this@RoomsActivity, android.R.layout.simple_spinner_dropdown_item, rooms)
                spinnerRoom.adapter = roomAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val timeSlots = generateTimeSlots()
        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, timeSlots)
        spinnerStartTime.adapter = timeAdapter
        spinnerEndTime.adapter = timeAdapter

        // Date selection
        var selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        }

        builder.setView(view)
        builder.setPositiveButton("Add") { _, _ ->
            val startTimeIndex = spinnerStartTime.selectedItemPosition
            val endTimeIndex = spinnerEndTime.selectedItemPosition

            if (endTimeIndex <= startTimeIndex) {
                Toast.makeText(this, "End time must be after start time", Toast.LENGTH_SHORT).show()
            } else if (spinnerRoom.selectedItem == null) {
                Toast.makeText(this, "Please select a room", Toast.LENGTH_SHORT).show()
            } else {
                presenter.addRoom(
                    spinnerRoom.selectedItem.toString(),
                    spinnerBuilding.selectedItem.toString(),
                    "${spinnerStartTime.selectedItem} - ${spinnerEndTime.selectedItem}",
                    selectedDate
                )
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    override fun showRoomDetails(room: Room) {
        Toast.makeText(this, "Room: ${room.name} at ${room.building} (${room.schedule} ${room.time})", Toast.LENGTH_SHORT).show()
    }

    override fun showDeleteConfirmation(room: Room) {
        AlertDialog.Builder(this)
            .setTitle("Delete Room")
            .setMessage("Do you want to delete ${room.name}?")
            .setPositiveButton("Yes") { _, _ -> presenter.deleteRoom(room) }
            .setNegativeButton("No", null)
            .show()
    }

    override fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
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
            R.id.nav_rooms -> drawerLayout.closeDrawer(GravityCompat.START)
            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
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