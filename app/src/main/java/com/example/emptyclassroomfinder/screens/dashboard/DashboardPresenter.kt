package com.example.emptyclassroomfinder.screens.dashboard

import android.content.Intent
import com.example.emptyclassroomfinder.R
import com.example.emptyclassroomfinder.screens.rooms.RoomsActivity

class DashboardPresenter(
    private val view: DashboardContract.View,
    private val model: DashboardModel
) : DashboardContract.Presenter {

    override fun loadUserInfo() {
        val username = model.getUsername()
        view.updateUserInfo(username)
    }

    override fun handleNavigation(itemId: Int) {
        when (itemId) {
            R.id.nav_profile -> view.showProfile()
            R.id.nav_logout -> view.showLogin()
            R.id.nav_rooms -> {
                (view as? DashboardActivity)?.let {
                    it.startActivity(Intent(it, RoomsActivity::class.java))
                }
            }
            R.id.nav_dashboard -> view.closeDrawer()
            R.id.nav_settings -> {
                // Future implementation
                view.closeDrawer()
            }
        }
        view.closeDrawer()
    }
}