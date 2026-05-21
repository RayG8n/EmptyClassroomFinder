package com.example.prefi.screens.login

import com.example.prefi.app.Custom
import com.example.prefi.data.User

class LoginModel(private val app: Custom) {

    fun validate(username: String, password: String): Boolean {
        return username.equals(app.defaultUsername, false) && password.equals(app.defaultPassword, false)
    }

    fun saveData(username: String, password: String) {
        app.loginUser = User(username, password)
    }
}