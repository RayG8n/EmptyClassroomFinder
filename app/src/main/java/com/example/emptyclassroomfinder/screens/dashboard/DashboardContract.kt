package com.example.emptyclassroomfinder.screens.dashboard

class DashboardContract {
    interface View {
        fun updateUserInfo(username: String)
        fun showProfile()
        fun showGroups()
        fun showSettings()
        fun showLogin()
        fun closeDrawer()
    }

    interface Presenter {
        fun loadUserInfo()
        fun handleNavigation(itemId: Int)
    }
}