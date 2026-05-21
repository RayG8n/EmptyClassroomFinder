package com.example.prefi.screens.rooms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.prefi.R
import com.example.prefi.data.Room

class RoomsAdapter(private val context: Context, private val rooms: MutableList<Room>) : BaseAdapter() {

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
        
        nameText.text = room.name
        buildingText.text = room.building
        timeText.text = room.time
        scheduleText.text = room.schedule
        
        return view
    }
}