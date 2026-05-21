package com.example.prefi.screens.dashboard

import com.example.prefi.app.Custom

class DashboardModel(private val app: Custom) {
    fun getUsername(): String {
        // This could come from SharedPreferences or a Database
        return app.loginUser.username
    }
}