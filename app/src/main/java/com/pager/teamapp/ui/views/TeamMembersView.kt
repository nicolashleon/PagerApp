package com.pager.teamapp.ui.views

import com.pager.teamapp.ui.models.Status
import com.pager.teamapp.ui.models.TeamMember

interface TeamMembersView : BaseView {
    fun showLoader(isVisible: Boolean)

    fun showTeamMembers(members: List<TeamMember>)
    fun addNewTeamMember(teamMember: TeamMember)
    fun updateTeamMemberStatus(status: Status)
    fun showStatusUpdateDialog(teamMember: TeamMember)

    fun showError()
    fun showNewTeamMembersError()
    fun showStatusUpdatesError()
}