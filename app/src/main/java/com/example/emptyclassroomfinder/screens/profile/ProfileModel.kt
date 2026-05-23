package com.example.emptyclassroomfinder.screens.profile

import com.example.emptyclassroomfinder.app.Custom

class ProfileModel(private val app: Custom) {
    fun getUsername(): String {
        return app.loginUser.username
    }

    fun updateUsername(newUsername: String) {
        app.loginUser.username = newUsername
        app.defaultUsername = newUsername
    }
}