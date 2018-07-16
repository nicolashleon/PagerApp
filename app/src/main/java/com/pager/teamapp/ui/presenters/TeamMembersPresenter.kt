package com.pager.teamapp.ui.presenters

import com.pager.teamapp.logger.Log
import com.pager.teamapp.logger.Logger
import com.pager.teamapp.repositories.TeamRepository
import com.pager.teamapp.repositories.TeamStatusRepository
import com.pager.teamapp.ui.models.Status
import com.pager.teamapp.ui.models.TeamMember
import com.pager.teamapp.ui.views.TeamMembersView
import io.reactivex.observers.DisposableObserver
import io.reactivex.subscribers.DisposableSubscriber

class TeamMembersPresenter() : Presenter<TeamMembersView>() {

    companion object {
        private const val TAG = "TeamMembersPresenter"
    }

    private var teamRepository: TeamRepository = TeamRepository()
    private var statusRepository: TeamStatusRepository = TeamStatusRepository()
    private var log: Log = Logger()

    constructor(teamRepository: TeamRepository, statusRepository: TeamStatusRepository, logger: Log) : this() {
        this.teamRepository = teamRepository
        this.statusRepository = statusRepository
        this.log = logger
    }

    fun getTeamMembers() {
        baseView?.showLoader(true)
        addDisposable(teamRepository.getTeam().subscribeWith(object : DisposableObserver<List<TeamMember>>() {
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
                log.e(TAG, "Error getting the teams", e)
                baseView?.apply {
                    showLoader(false)
                    showError()
                }
            }

        }))
    }

    fun getStatusUpdates() {
        addDisposable(statusRepository.getMemberStatusUpdates().subscribeWith(object : DisposableSubscriber<Status>() {
            override fun onComplete() {
                log.d(TAG, "Team's status updates subscription complete")
            }

            override fun onNext(status: Status?) {
                if (status != null) {
                    baseView?.updateTeamMemberStatus(status)
                }
            }

            override fun onError(t: Throwable?) {
                log.e(TAG, "Error getting the team's status updates", t)
                baseView?.showStatusUpdatesError()
            }

        }))

    }

    fun getNewMemberUpdates() {
        addDisposable(statusRepository.getNewMemberUpdates().subscribeWith(object : DisposableSubscriber<TeamMember>() {
            override fun onComplete() {
                log.d(TAG, "New member updates subscription complete")
            }

            override fun onNext(t: TeamMember?) {
                if (t != null) {
                    baseView?.addNewTeamMember(t)
                }
            }

            override fun onError(t: Throwable?) {
                log.e(TAG, "Error getting the new member updates", t)
                baseView?.showNewTeamMembersError()
            }

        }))

    }

    fun sendStatusUpdate(status: String, teamMember: TeamMember) {
        addDisposable(statusRepository.updateMemberStatus(status, teamMember.github).subscribeWith(object : DisposableSubscriber<Boolean>() {
            override fun onComplete() {
                log.d(TAG, "status update completed")
            }

            override fun onNext(t: Boolean?) {
                log.d(TAG, "update sent :" + t.toString())
            }

            override fun onError(t: Throwable?) {
                log.e(TAG, "Failed to send status update", t)
            }
        }))
    }

    fun showUpdateStatusDialog(teamMember: TeamMember) {
        baseView?.showStatusUpdateDialog(teamMember)
    }
}