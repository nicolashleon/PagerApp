package com.pager.teamapp.ui.adapters

import com.pager.teamapp.ui.delegates.TeamMembersDelegateAdapter
import com.pager.teamapp.ui.models.TeamMember

class TeamMembersAdapter(private val onStatusUpdateListener: OnStatusUpdateListener) : BaseAdapter(), TeamMembersDelegateAdapter.OnItemExpandedListener, TeamMembersDelegateAdapter.OnItemStatusUpdateListener {

    interface OnStatusUpdateListener {
        fun onStatusUpdated(teamMember: TeamMember)
    }

    init {
        delegateAdapters.put(TeamMember.TEAM_MEMBER_VIEW_TYPE, TeamMembersDelegateAdapter(this, this))
    }

    override fun onItemExpanded(itemPos: Int, isExpanded: Boolean) {
        val teamMember = getItem<TeamMember>(itemPos)
        teamMember.isExpanded = isExpanded
        notifyItemChanged(itemPos)
    }

    override fun onStatusUpdated(itemPos: Int) {
        onStatusUpdateListener.onStatusUpdated(getItem(itemPos))
    }
}