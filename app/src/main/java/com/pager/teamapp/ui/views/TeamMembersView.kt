package com.pager.teamapp.ui.views

import com.pager.teamapp.ui.models.TeamMember

interface TeamMembersView : BaseView {
    fun showLoader(isVisible: Boolean)
    fun showError()
    fun showTeamMembers(members: List<TeamMember>)
}