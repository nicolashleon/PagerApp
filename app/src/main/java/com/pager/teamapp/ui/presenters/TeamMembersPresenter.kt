package com.pager.teamapp.ui.presenters

import android.util.Log
import com.pager.teamapp.TeamRepository
import com.pager.teamapp.ui.models.TeamMember
import com.pager.teamapp.ui.views.TeamMembersView
import io.reactivex.observers.DisposableObserver

class TeamMembersPresenter : Presenter<TeamMembersView>() {
    fun getTeamMembers() {
        baseView?.showLoader(true)
        addDisposable(TeamRepository().getTeam().subscribeWith(object : DisposableObserver<List<TeamMember>>() {
            override fun onComplete() {
                baseView?.showLoader(false)
                Log.d("TeamMembersPresenter", "No Team members to display")
            }

            override fun onNext(t: List<TeamMember>) {
                baseView?.apply {
                    showLoader(false)
                    showTeamMembers(t.sortedWith(TeamMember.getNameComparator()))
                }
            }

            override fun onError(e: Throwable) {
                Log.e("TeamMembersPresenter", "Error getting the teams", e)
                baseView?.apply {
                    showLoader(false)
                    showError()
                }
            }

        }))
    }
}