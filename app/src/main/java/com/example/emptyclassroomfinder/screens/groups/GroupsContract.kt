package com.example.emptyclassroomfinder.screens.groups

import com.example.emptyclassroomfinder.data.Group

class GroupsContract {
    interface View {
        fun updateGroupsList(groups: List<Group>)
        fun showMessage(message: String)
        fun showCreateGroupDialog()
        fun showGroupDetails(group: Group)
        fun showDeleteConfirmation(group: Group)
        fun showJoinGroupDialog()
        fun showLeaveGroupDialog()
        fun closeDrawer()
    }

    interface Presenter {
        fun loadGroups()
        fun createGroup(name: String)
        fun joinGroup(groupName: String)
        fun leaveGroup(groupName: String)
        fun deleteGroup(groupName: String)
        fun onGroupClicked(group: Group)
        fun onGroupLongClicked(group: Group)
    }
}
