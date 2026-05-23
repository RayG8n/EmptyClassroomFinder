package com.example.emptyclassroomfinder.screens.settings

class SettingsContract {
    interface View {
        fun updateDarkModeStatus(isEnabled: Boolean)
        fun applyDarkMode(isEnabled: Boolean)
        fun showMessage(message: String)
        fun closeDrawer()
    }

    interface Presenter {
        fun loadSettings()
        fun setDarkMode(isEnabled: Boolean)
        fun handleNavigation(itemId: Int)
    }
}
