package com.example.prefi.screens.rooms

import com.example.prefi.data.Room

class RoomsPresenter(
    private val view: RoomsContract.View,
    private val model: RoomsModel
) : RoomsContract.Presenter {

    override fun loadRooms() {
        view.updateList(model.getRooms())
    }

    override fun addRoom(name: String, building: String, time: String, schedule: String) {
        val newRoom = Room(name, building, time, schedule)
        model.addRoom(newRoom)
        view.updateList(model.getRooms())
        view.showMessage("Room added!")
    }

    override fun deleteRoom(room: Room) {
        model.removeRoom(room)
        view.updateList(model.getRooms())
        view.showMessage("Room deleted!")
    }

    override fun onRoomClicked(room: Room) {
        view.showRoomDetails(room)
    }

    override fun onRoomLongClicked(room: Room) {
        view.showDeleteConfirmation(room)
    }
}