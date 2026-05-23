package com.example.emptyclassroomfinder.screens.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.emptyclassroomfinder.app.Custom
import com.example.emptyclassroomfinder.R
import com.example.emptyclassroomfinder.screens.login.LoginActivity
import com.example.emptyclassroomfinder.utility.getEditTextValue
import com.example.emptyclassroomfinder.utility.getToast

class RegisterActivity : Activity(), RegisterContract.View {
    private lateinit var registerPresenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerPresenter = RegisterPresenter(this, RegisterModel(application as Custom))

        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val username = getEditTextValue(R.id.edittextUsername).trim()
            val password = getEditTextValue(R.id.edittextPassword).trim()
            val confirmPassword = getEditTextValue(R.id.edittextConfirmPassword).trim()

            registerPresenter.register(username, password, confirmPassword)
        }
    }

    override fun showSuccess() {
        runOnUiThread {
            getToast("Registration successful!")
        }
    }

    override fun showEmptyField() {
        runOnUiThread {
            getToast("All fields cannot be empty!")
        }
    }

    override fun showPasswordMismatch() {
        runOnUiThread {
            getToast("Passwords do not match!")
        }
    }

    override fun showUsernameTaken() {
        runOnUiThread {
            getToast("Username is already taken!")
        }
    }

    override fun showError(message: String) {
        runOnUiThread {
            getToast(message)
        }
    }

    override fun goToLogin() {
        runOnUiThread {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
