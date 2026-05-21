package com.example.prefi.screens.profile

import com.example.prefi.app.Custom

class ProfileModel(private val app: Custom) {
    fun getUsername(): String {
        return app.loginUser.username
    }
}