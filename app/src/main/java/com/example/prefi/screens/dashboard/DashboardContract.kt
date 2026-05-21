package com.example.prefi.screens.dashboard

class DashboardContract {
    interface View {
        fun updateUserInfo(username: String)
        fun showProfile()
        fun showLogin()
        fun closeDrawer()
    }

    interface Presenter {
        fun loadUserInfo()
        fun handleNavigation(itemId: Int)
    }
}