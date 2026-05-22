package com.example.emptyclassroomfinder.screens.profile

class ProfileContract {
    interface View {
        fun displayUsername(message: String)
    }

    interface Presenter{
        fun initializeUsername()
    }

}