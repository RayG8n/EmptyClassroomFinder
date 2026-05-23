package com.example.emptyclassroomfinder.app

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.example.emptyclassroomfinder.data.User

class Custom : Application() {

    var isDarkMode: Boolean
        get() = prefs.getBoolean("is_dark_mode", false)
        set(value) = prefs.edit { putBoolean("is_dark_mode", value) }
    private val prefs by lazy { getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }

    var defaultUsername: String
        get() = prefs.getString("username", "") ?: ""
        set(value) = prefs.edit { putString("username", value) }

    var defaultPassword: String
        get() = prefs.getString("password", "") ?: ""
        set(value) = prefs.edit { putString("password", value) }

    var loginUser = User()
}
