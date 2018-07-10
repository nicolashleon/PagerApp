package com.pager.teamapp.ui.models

import com.pager.teamapp.extensions.EMPTY_STRING

data class TeamMember(var name: String = String.EMPTY_STRING(),
                      var picture: String = String.EMPTY_STRING(),
                      var role: String = String.EMPTY_STRING(),
                      var languages: List<String> = ArrayList(),
                      var skills: List<String> = ArrayList(),
                      var location: String = String.EMPTY_STRING(),
                      var status: String = String.EMPTY_STRING()) : DelegateUIModel {
    companion object {
        private const val TEAM_MEMBER_VIEW_TYPE = 1
    }

    override val viewType: Int = TEAM_MEMBER_VIEW_TYPE

}