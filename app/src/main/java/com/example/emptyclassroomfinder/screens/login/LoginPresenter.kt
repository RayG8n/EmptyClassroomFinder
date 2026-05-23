package com.example.emptyclassroomfinder.screens.login

class LoginPresenter(private val view: LoginContract.View, private val model: LoginModel): LoginContract.Presenter {

    override fun login(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            model.login(username, password, object : LoginModel.LoginCallback {
                override fun onSuccess() {
                    view.showSuccess()
                    view.showHome()
                }

                override fun onFailure(message: String) {
                    if (message.contains("Invalid", ignoreCase = true)) {
                        view.showInvalidCredentials()
                    } else {
                        view.showMessage(message)
                    }
                }
            })
        } else {
            view.showEmptyField()
        }
    }
}
