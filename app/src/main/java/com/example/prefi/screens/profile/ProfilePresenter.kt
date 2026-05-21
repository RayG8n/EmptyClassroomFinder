package com.example.prefi.screens.profile


class ProfilePresenter(private val view: ProfileContract.View, private val model: ProfileModel): ProfileContract.Presenter {
    override fun initializeUsername() {
        val username = model.getUsername()

        if(!username.isNullOrEmpty()){
            view.displayUsername("Good day, $username")
        }
        else{
            view.displayUsername("Good day, user")
        }
    }
}