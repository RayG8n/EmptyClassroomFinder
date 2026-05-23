package com.example.emptyclassroomfinder.screens.register

import com.example.emptyclassroomfinder.app.Custom
import okhttp3.*
import java.io.IOException

class RegisterModel(private val app: Custom) {
    private val client = OkHttpClient()
    private val baseUrl = "https://emptyclassroomfinder-test.onrender.com"

    interface RegisterCallback {
        fun onSuccess()
        fun onFailure(message: String)
    }

    fun register(username: String, password: String, callback: RegisterCallback) {
        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/save-profile")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // Update local storage too if needed
                    app.defaultUsername = username
                    app.defaultPassword = password
                    callback.onSuccess()
                } else if (response.code == 409) {
                    callback.onFailure("Username already taken")
                } else {
                    callback.onFailure("Server error: ${response.code}")
                }
            }
        })
    }
}
