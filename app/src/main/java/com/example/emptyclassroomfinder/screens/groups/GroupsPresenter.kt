package com.example.emptyclassroomfinder.screens.groups

import com.example.emptyclassroomfinder.data.Group

class GroupsPresenter(
    private val view: GroupsContract.View,
    private val model: GroupsModel
) : GroupsContract.Presenter {

    override fun loadGroups() {
        model.fetchGroups(object : GroupsModel.GroupsCallback {
            override fun onSuccess(groups: List<Group>) {
                view.updateGroupsList(groups)
            }

            override fun onFailure(message: String) {
                view.showMessage("Error loading groups: $message")
            }
        })
    }

    override fun createGroup(name: String) {
        if (name.isEmpty()) {
            view.showMessage("Group name cannot be empty")
            return
        }
        model.createGroup(name, object : GroupsModel.OperationCallback {
            override fun onSuccess() {
                loadGroups()
                view.showMessage("Group created!")
            }

            override fun onFailure(message: String) {
                view.showMessage("Error creating group: $message")
            }
        })
    }

    override fun joinGroup(groupName: String) {
        model.joinGroup(groupName, object : GroupsModel.OperationCallback {
            override fun onSuccess() {
                loadGroups()
                view.showMessage("Joined group!")
            }

            override fun onFailure(message: String) {
                view.showMessage("Error joining group: $message")
            }
        })
    }

    override fun deleteGroup(groupName: String) {
        model.deleteGroup(groupName, object : GroupsModel.OperationCallback {
            override fun onSuccess() {
                loadGroups()
                view.showMessage("Group deleted!")
            }

            override fun onFailure(message: String) {
                view.showMessage("Error deleting group: $message")
            }
        })
    }

    override fun onGroupClicked(group: Group) {
        view.showGroupDetails(group)
    }

    override fun onGroupLongClicked(group: Group) {
        view.showDeleteConfirmation(group)
    }
}
