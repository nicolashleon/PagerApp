package com.pager.teamapp.ui.adapters

import com.pager.teamapp.ui.delegates.TeamMembersDelegateAdapter
import com.pager.teamapp.ui.models.TeamMember

class TeamMembersAdapter : BaseAdapter() {
    init {
        delegateAdapters.put(TeamMember.TEAM_MEMBER_VIEW_TYPE, TeamMembersDelegateAdapter())
    }
}