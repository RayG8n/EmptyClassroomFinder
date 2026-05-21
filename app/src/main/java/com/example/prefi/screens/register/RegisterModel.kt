package com.example.prefi.screens.register

import com.example.prefi.app.Custom
import okhttp3.*
import java.io.IOException

class RegisterModel(private val app: Custom) {
    private val client = OkHttpClient()

    interface RegisterCallback {
        fun onSuccess()
        fun onFailure(message: String)
    }

    fun isUsernameTaken(username: String): Boolean {
        // This should also probably be a network call, but for now we keep it simple
        return username.equals(app.defaultUsername, ignoreCase = true)
    }

    fun register(username: String, password: String, callback: RegisterCallback) {
        // Replace this with your new Render or Railway URL if Glitch is down
        // Example: "https://my-backend.onrender.com/save-profile"
        val url = "https://emptyclassroomfinder-1.onrender.com/save-profile"

        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url(url)
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
                } else {
                    callback.onFailure("Server error: ${response.code}")
                }
            }
        })
    }
}
