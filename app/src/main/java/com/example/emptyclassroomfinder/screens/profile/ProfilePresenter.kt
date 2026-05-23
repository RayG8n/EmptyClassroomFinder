package com.example.emptyclassroomfinder.screens.profile

class ProfilePresenter(
    private val view: ProfileContract.View,
    private val model: ProfileModel
) : ProfileContract.Presenter {

    override fun initializeUsername() {
        val username = model.getUsername()
        if (!username.isNullOrEmpty()) {
            view.displayUsername(username)
        } else {
            view.displayUsername("user")
        }
    }

    override fun updateUsername(newUsername: String) {
        if (newUsername.isEmpty()) {
            view.showMessage("Username cannot be empty")
            return
        }
        model.updateUsername(newUsername)
        view.displayUsername(newUsername)
        view.clearNewUsernameField()
        view.showMessage("Profile updated!")
    }
}