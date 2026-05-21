package com.example.prefi.screens.rooms

import com.example.prefi.data.Room

class RoomsModel {
    private val roomsList = mutableListOf<Room>()

    init {
        // Initial sample data
        roomsList.add(Room("301", "GLE", "8:30 AM - 12:30 PM", "Monday"))
        roomsList.add(Room("202", "RTL", "1:00 PM - 5:00 PM", "Tuesday"))
    }

    fun getRooms(): List<Room> = roomsList

    fun addRoom(room: Room) {
        roomsList.add(room)
    }

    fun removeRoom(room: Room) {
        roomsList.remove(room)
    }
}