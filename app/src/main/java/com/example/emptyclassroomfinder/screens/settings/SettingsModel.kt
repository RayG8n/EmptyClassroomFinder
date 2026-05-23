package com.example.emptyclassroomfinder.screens.settings

import com.example.emptyclassroomfinder.app.Custom

class SettingsModel(private val app: Custom) {

    fun setDarkMode(isEnabled: Boolean) {
        app.isDarkMode = isEnabled
    }

    fun isDarkModeEnabled(): Boolean {
        return app.isDarkMode
    }
}
