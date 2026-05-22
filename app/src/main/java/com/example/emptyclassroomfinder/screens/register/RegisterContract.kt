package com.example.emptyclassroomfinder.screens.register

class RegisterContract {
    interface View {
        fun showSuccess()
        fun showEmptyField()
        fun showPasswordMismatch()
        fun showUsernameTaken()
        fun showError(message: String)
        fun goToLogin()
    }

    interface Presenter {
        fun register(username: String, password: String, confirmPassword: String)
    }
}