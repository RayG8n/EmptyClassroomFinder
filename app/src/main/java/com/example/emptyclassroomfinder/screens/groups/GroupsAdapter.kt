package com.example.emptyclassroomfinder.screens.groups

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.emptyclassroomfinder.R
import com.example.emptyclassroomfinder.data.Group

class GroupsAdapter(private val context: Context, private val groups: MutableList<Group>) : BaseAdapter() {

    override fun getCount(): Int = groups.size
    override fun getItem(position: Int): Any = groups[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_group, parent, false)
        
        val group = groups[position]
        
        val nameText = view.findViewById<TextView>(R.id.textGroupName)
        val ownerText = view.findViewById<TextView>(R.id.textOwner)
        val memberCountText = view.findViewById<TextView>(R.id.textMemberCount)
        
        nameText.text = group.name
        ownerText.text = "Owner: ${group.owner}"
        memberCountText.text = "${group.members.size} members"
        
        return view
    }
}
