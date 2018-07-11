package com.pager.teamapp.ui.adapters

import com.pager.teamapp.ui.delegates.TeamMembersDelegateAdapter
import com.pager.teamapp.ui.models.TeamMember

class TeamMembersAdapter : BaseAdapter(), TeamMembersDelegateAdapter.OnItemExpandedListener {
    init {
        delegateAdapters.put(TeamMember.TEAM_MEMBER_VIEW_TYPE, TeamMembersDelegateAdapter(this))
    }

    override fun onItemExpanded(itemPos: Int, isExpanded: Boolean) {
        val teamMember = getItem<TeamMember>(itemPos)
        teamMember.isExpanded = isExpanded
        notifyItemChanged(itemPos)
    }
}