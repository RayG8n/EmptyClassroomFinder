package com.example.emptyclassroomfinder.screens.dashboard

import com.example.emptyclassroomfinder.app.Custom

class DashboardModel(private val app: Custom) {
    fun getUsername(): String {
        return app.loginUser.username
    }
}