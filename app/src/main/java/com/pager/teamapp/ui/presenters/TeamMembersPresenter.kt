package com.pager.teamapp.ui.presenters

import android.util.Log
import com.pager.teamapp.repositories.TeamRepository
import com.pager.teamapp.repositories.TeamStatusRepository
import com.pager.teamapp.ui.models.Status
import com.pager.teamapp.ui.models.TeamMember
import com.pager.teamapp.ui.views.TeamMembersView
import io.reactivex.observers.DisposableObserver
import io.reactivex.subscribers.DisposableSubscriber

class TeamMembersPresenter : Presenter<TeamMembersView>() {

    companion object {
        private const val TAG = "TeamMembersPresenter"
    }


    fun getTeamMembers() {
        baseView?.showLoader(true)
        addDisposable(TeamRepository().getTeam().subscribeWith(object : DisposableObserver<List<TeamMember>>() {
            override fun onComplete() {
                baseView?.showLoader(false)
            }

            override fun onNext(t: List<TeamMember>) {
                baseView?.apply {
                    showLoader(false)
                    showTeamMembers(t.sortedWith(TeamMember.getNameComparator()))
                }
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "Error getting the teams", e)
                baseView?.apply {
                    showLoader(false)
                    showError()
                }
            }

        }))
    }

    fun getStatusUpdates() {
        addDisposable(TeamStatusRepository().getMemberStatusUpdates().subscribeWith(object : DisposableSubscriber<Status>() {
            override fun onComplete() {
                Log.d(TAG, "Team's status updates subscription complete")
            }

            override fun onNext(status: Status?) {
                if (status != null) {
                    baseView?.updateTeamMemberStatus(status)
                }
            }

            override fun onError(t: Throwable?) {
                Log.e(TAG, "Error getting the team's status updates", t)
                baseView?.showStatusUpdatesError()
            }

        }))

    }

    fun getNewMemberUpdates() {
        addDisposable(TeamStatusRepository().getNewMemberUpdates().subscribeWith(object : DisposableSubscriber<TeamMember>() {
            override fun onComplete() {
                Log.d(TAG, "New member updates subscription complete")
            }

            override fun onNext(t: TeamMember?) {
                if (t != null) {
                    baseView?.addNewTeamMember(t)
                }
            }

            override fun onError(t: Throwable?) {
                Log.e(TAG, "Error getting the new member updates", t)
                baseView?.showNewTeamMembersError()
            }

        }))

    }

    fun sendStatusUpdate(status: String, teamMember: TeamMember) {
        addDisposable(TeamStatusRepository().updateMemberStatus(status, teamMember.github).subscribeWith(object : DisposableSubscriber<Boolean>() {
            override fun onComplete() {
                Log.d(TAG, "status update completed")
            }

            override fun onNext(t: Boolean?) {
                Log.d(TAG, "update sent :" + t.toString())
            }

            override fun onError(t: Throwable?) {
                Log.d(TAG, "Failed to send status update", t)
            }
        }))
    }

    fun showUpdateStatusDialog(teamMember: TeamMember) {
        baseView?.showStatusUpdateDialog(teamMember)
    }
}