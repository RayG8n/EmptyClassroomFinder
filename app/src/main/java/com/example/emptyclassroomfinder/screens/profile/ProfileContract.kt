package com.example.emptyclassroomfinder.screens.profile

class ProfileContract {
    interface View {
        fun displayUsername(message: String)
        fun showMessage(message: String)
        fun clearNewUsernameField()
    }

    interface Presenter {
        fun initializeUsername()
        fun updateUsername(newUsername: String)
    }
}