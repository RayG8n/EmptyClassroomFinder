package com.example.emptyclassroomfinder.screens.settings

import com.example.emptyclassroomfinder.R

class SettingsPresenter(
    private val view: SettingsContract.View,
    private val model: SettingsModel
) : SettingsContract.Presenter {

    override fun loadSettings() {
        view.updateDarkModeStatus(model.isDarkModeEnabled())
    }

    override fun setDarkMode(isEnabled: Boolean) {
        model.setDarkMode(isEnabled)
        view.applyDarkMode(isEnabled)
        view.showMessage("Dark mode ${if (isEnabled) "enabled" else "disabled"}")
    }

    override fun handleNavigation(itemId: Int) {
        // Navigation logic will be handled in Activity for simplicity, 
        // or we can delegate here if we want strict MVP
    }
}
