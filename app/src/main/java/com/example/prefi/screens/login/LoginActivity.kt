package com.example.prefi.screens.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.prefi.app.Custom
import com.example.prefi.R
import com.example.prefi.screens.dashboard.DashboardActivity
import com.example.prefi.screens.profile.ProfileActivity
import com.example.prefi.screens.register.RegisterActivity
import com.example.prefi.utility.getEditTextValue
import com.example.prefi.utility.getToast

class LoginActivity : Activity(), LoginContract.View {
    lateinit var Presenter: LoginPresenter

    var username = "";
    var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Presenter = LoginPresenter(this, LoginModel(application as Custom))
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textviewRegister = findViewById<TextView>(R.id.textviewRegister)

        buttonLogin.setOnClickListener{
           username = getEditTextValue(R.id.editextUsername)
           password = getEditTextValue(R.id.editextPassword)

            Presenter.login(username, password)
        }

        textviewRegister.setOnClickListener{

            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)

        }
    }

    override fun showSuccess() {
        getToast("Login successful!")
    }

    override fun showInvalidCredentials() {
        getToast("Credentials do not match!")
    }

    override fun showEmptyField() {
        getToast("Username and Password cannot be empty!")
    }

    override fun showHome() {

        val dashboardIntent = Intent(this, DashboardActivity::class.java)

        startActivity(dashboardIntent)
    }
}