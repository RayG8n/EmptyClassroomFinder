package com.example.emptyclassroomfinder.screens.login

import com.example.emptyclassroomfinder.app.Custom
import com.example.emptyclassroomfinder.data.User
import okhttp3.*
import java.io.IOException

class LoginModel(private val app: Custom) {
    private val client = OkHttpClient()
    private val baseUrl = "https://emptyclassroomfinder-test.onrender.com"

    interface LoginCallback {
        fun onSuccess()
        fun onFailure(message: String)
    }

    fun login(username: String, password: String, callback: LoginCallback) {
        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/login")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    saveData(username, password)
                    callback.onSuccess()
                } else if (response.code == 401) {
                    callback.onFailure("Invalid username or password")
                } else {
                    callback.onFailure("Server error: ${response.code}")
                }
            }
        })
    }

    fun saveData(username: String, password: String) {
        app.loginUser = User(username, password)
        // Also update default values for persistence if needed
        app.defaultUsername = username
        app.defaultPassword = password
    }
}
