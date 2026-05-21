package com.example.prefi.screens.register

import android.os.Handler
import android.os.Looper

class RegisterPresenter(private val view: RegisterContract.View, private val model: RegisterModel) : RegisterContract.Presenter {

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun register(username: String, password: String, confirmPassword: String) {
        if (username.isNullOrEmpty() || password.isNullOrEmpty() || confirmPassword.isNullOrEmpty()) {
            view.showEmptyField()
            return
        }

        if (password != confirmPassword) {
            view.showPasswordMismatch()
            return
        }

        if (model.isUsernameTaken(username)) {
            view.showUsernameTaken()
            return
        }

        model.register(username, password, object : RegisterModel.RegisterCallback {
            override fun onSuccess() {
                mainHandler.post {
                    view.showSuccess()
                    view.goToLogin()
                }
            }

            override fun onFailure(message: String) {
                mainHandler.post {
                    view.showError(message)
                }
            }
        })
    }
}
