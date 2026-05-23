package com.example.emptyclassroomfinder.screens.rooms

import com.example.emptyclassroomfinder.data.Room
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class RoomsModel {
    private val client = OkHttpClient()
    private val baseUrl = "https://emptyclassroomfinder-test.onrender.com"

    interface RoomsCallback {
        fun onSuccess(rooms: List<Room>)
        fun onFailure(message: String)
    }

    interface OperationCallback {
        fun onSuccess()
        fun onFailure(message: String)
    }

    fun fetchRooms(callback: RoomsCallback) {
        val request = Request.Builder()
            .url("$baseUrl/get-rooms")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()
                    val rooms = mutableListOf<Room>()
                    try {
                        val jsonArray = JSONArray(jsonData)
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            rooms.add(Room(
                                obj.getString("name"),
                                obj.getString("building"),
                                obj.getString("time"),
                                obj.getString("schedule")
                            ))
                        }
                        callback.onSuccess(rooms)
                    } catch (e: Exception) {
                        callback.onFailure("Parsing error: ${e.message}")
                    }
                } else {
                    callback.onFailure("Server error: ${response.code} ${response.message}")
                }
            }
        })
    }

    fun addRoom(room: Room, callback: OperationCallback) {
        val formBody = FormBody.Builder()
            .add("name", room.name)
            .add("building", room.building)
            .add("time", room.time)
            .add("schedule", room.schedule)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/save-room") // matching your /save-profile pattern
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback.onSuccess()
                } else {
                    callback.onFailure("Server error: ${response.code} ${response.message}")
                }
            }
        })
    }

    fun removeRoom(room: Room, callback: OperationCallback) {
        // Assuming delete by name for simplicity
        val formBody = FormBody.Builder()
            .add("name", room.name)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/delete-room")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback.onSuccess()
                } else {
                    callback.onFailure("Server error: ${response.code} ${response.message}")
                }
            }
        })
    }
}
