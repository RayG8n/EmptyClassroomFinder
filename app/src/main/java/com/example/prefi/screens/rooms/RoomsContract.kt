package com.example.prefi.screens.rooms

import com.example.prefi.data.Room

class RoomsContract {
    interface View {
        fun updateList(rooms: List<Room>)
        fun showAddRoomDialog()
        fun showRoomDetails(room: Room)
        fun showDeleteConfirmation(room: Room)
        fun showMessage(message: String)
        fun closeDrawer()
    }

    interface Presenter {
        fun loadRooms()
        fun addRoom(name: String, building: String, time: String, schedule: String)
        fun deleteRoom(room: Room)
        fun onRoomClicked(room: Room)
        fun onRoomLongClicked(room: Room)
    }
}