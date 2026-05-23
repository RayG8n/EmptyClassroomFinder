package com.example.emptyclassroomfinder.screens.login

 class LoginContract {
    interface View{
        fun showSuccess()
        fun showInvalidCredentials()
        fun showEmptyField()
        fun showMessage(message: String)
        fun showHome()
    }

    interface Presenter{
        fun login(username: String, password: String)
    }


}