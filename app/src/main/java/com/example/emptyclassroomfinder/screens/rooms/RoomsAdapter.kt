package com.example.emptyclassroomfinder.screens.rooms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.example.emptyclassroomfinder.R
import com.example.emptyclassroomfinder.data.Room
import java.text.SimpleDateFormat
import java.util.Locale

class RoomsAdapter(
    private val context: Context, 
    private val rooms: MutableList<Room>,
    private val onDeleteClick: (Room) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = rooms.size
    override fun getItem(position: Int): Any = rooms[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_room, parent, false)
        
        val room = rooms[position]
        
        val nameText = view.findViewById<TextView>(R.id.textRoomName)
        val buildingText = view.findViewById<TextView>(R.id.textBuilding)
        val timeText = view.findViewById<TextView>(R.id.textTime)
        val scheduleText = view.findViewById<TextView>(R.id.textSchedule)
        val dayOfWeekText = view.findViewById<TextView>(R.id.textDayOfWeek)
        val btnDelete = view.findViewById<ImageButton>(R.id.btnDeleteRoom)
        
        nameText.text = room.name
        buildingText.text = room.building
        timeText.text = room.time
        scheduleText.text = room.schedule

        // Parse day of week from room.schedule (assuming format yyyy-MM-dd)
        val dayOfWeek = try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(room.schedule)
            if (date != null) {
                val daySdf = SimpleDateFormat("EEEE", Locale.getDefault())
                daySdf.format(date)
            } else {
                ""
            }
        } catch (e: Exception) {
            "" // If it's not a date, don't show anything extra
        }
        dayOfWeekText.text = dayOfWeek
        dayOfWeekText.visibility = if (dayOfWeek.isNotEmpty()) View.VISIBLE else View.GONE
        
        btnDelete.setOnClickListener {
            onDeleteClick(room)
        }
        
        return view
    }
}