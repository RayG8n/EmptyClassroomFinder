package com.example.emptyclassroomfinder.screens.rooms

import com.example.emptyclassroomfinder.data.Room

class RoomsPresenter(
    private val view: RoomsContract.View,
    private val model: RoomsModel
) : RoomsContract.Presenter {

    override fun loadRooms() {
        model.fetchRooms(object : RoomsModel.RoomsCallback {
            override fun onSuccess(rooms: List<Room>) {
                view.updateList(rooms)
            }

            override fun onFailure(message: String) {
                view.showMessage("Error loading rooms: $message")
            }
        })
    }

    override fun addRoom(name: String, building: String, time: String, schedule: String) {
        val newRoom = Room(name, building, time, schedule)
        model.addRoom(newRoom, object : RoomsModel.OperationCallback {
            override fun onSuccess() {
                loadRooms()
                view.showMessage("Room added!")
            }

            override fun onFailure(message: String) {
                view.showMessage("Error adding room: $message")
            }
        })
    }

    override fun deleteRoom(room: Room) {
        model.removeRoom(room, object : RoomsModel.OperationCallback {
            override fun onSuccess() {
                loadRooms()
                view.showMessage("Room deleted!")
            }

            override fun onFailure(message: String) {
                view.showMessage("Error deleting room: $message")
            }
        })
    }

    override fun onRoomClicked(room: Room) {
        view.showRoomDetails(room)
    }

    override fun onRoomLongClicked(room: Room) {
        view.showDeleteConfirmation(room)
    }
}
