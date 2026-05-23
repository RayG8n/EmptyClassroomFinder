package com.example.emptyclassroomfinder.app

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.example.emptyclassroomfinder.data.User

class Custom : Application() {

    private val prefs by lazy { getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }

    var defaultUsername: String
        get() = prefs.getString("username", "test") ?: "test"
        set(value) = prefs.edit { putString("username", value) }

    var defaultPassword: String
        get() = prefs.getString("password", "test") ?: "test"
        set(value) = prefs.edit { putString("password", value) }

    var isDarkMode: Boolean
        get() = prefs.getBoolean("dark_mode", false)
        set(value) = prefs.edit { putBoolean("dark_mode", value) }

    var loginUser = User()
}
