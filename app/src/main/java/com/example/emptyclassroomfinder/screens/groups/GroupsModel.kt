package com.example.emptyclassroomfinder.screens.groups

import com.example.emptyclassroomfinder.app.Custom
import com.example.emptyclassroomfinder.data.Group
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class GroupsModel(private val app: Custom) {
    private val client = OkHttpClient()
    private val baseUrl = "https://emptyclassroomfinder-test.onrender.com"

    interface GroupsCallback {
        fun onSuccess(groups: List<Group>)
        fun onFailure(message: String)
    }

    interface OperationCallback {
        fun onSuccess()
        fun onFailure(message: String)
    }

    fun fetchGroups(callback: GroupsCallback) {
        val request = Request.Builder()
            .url("$baseUrl/get-groups")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()
                    val groups = mutableListOf<Group>()
                    try {
                        val jsonArray = JSONArray(jsonData)
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val membersJson = obj.getJSONArray("members")
                            val members = mutableListOf<String>()
                            for (j in 0 until membersJson.length()) {
                                members.add(membersJson.getString(j))
                            }
                            groups.add(Group(
                                obj.getString("name"),
                                obj.getString("owner"),
                                members
                            ))
                        }
                        callback.onSuccess(groups)
                    } catch (e: Exception) {
                        callback.onFailure("Parsing error: ${e.message}")
                    }
                } else {
                    callback.onFailure("Server error: ${response.code}")
                }
            }
        })
    }

    fun createGroup(name: String, callback: OperationCallback) {
        val formBody = FormBody.Builder()
            .add("name", name)
            .add("owner", app.defaultUsername)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/create-group")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) callback.onSuccess()
                else if (response.code == 409) callback.onFailure("Group name already taken")
                else callback.onFailure("Server error: ${response.code}")
            }
        })
    }

    fun joinGroup(groupName: String, callback: OperationCallback) {
        val formBody = FormBody.Builder()
            .add("name", groupName)
            .add("username", app.defaultUsername)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/join-group")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) callback.onSuccess()
                else callback.onFailure("Server error: ${response.code}")
            }
        })
    }

    fun leaveGroup(groupName: String, callback: OperationCallback) {
        val formBody = FormBody.Builder()
            .add("name", groupName)
            .add("username", app.defaultUsername)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/leave-group")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) callback.onSuccess()
                else callback.onFailure("Server error: ${response.code}")
            }
        })
    }

    fun deleteGroup(groupName: String, callback: OperationCallback) {
        val formBody = FormBody.Builder()
            .add("name", groupName)
            .add("username", app.defaultUsername)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/delete-group")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) callback.onSuccess()
                else if (response.code == 403) callback.onFailure("Only the owner can delete this group")
                else callback.onFailure("Server error: ${response.code}")
            }
        })
    }
}
