package com.example.prefi.screens.dashboard

import android.content.Intent
import com.example.prefi.R
import com.example.prefi.screens.rooms.RoomsActivity

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
                // We'll need to add showRooms to the View interface
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