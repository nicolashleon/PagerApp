package com.pager.teamapp

import android.support.annotation.NonNull
import com.pager.teamapp.logger.Log
import com.pager.teamapp.logger.UnitTestingLogger
import com.pager.teamapp.repositories.TeamRepository
import com.pager.teamapp.repositories.TeamStatusRepository
import com.pager.teamapp.ui.models.Status
import com.pager.teamapp.ui.models.TeamMember
import com.pager.teamapp.ui.presenters.TeamMembersPresenter
import com.pager.teamapp.ui.views.TeamMembersView
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


class TeamMembersPresenterTest {

    @Mock
    private var teamRepository: TeamRepository? = null

    @Mock
    private var statusRepository: TeamStatusRepository? = null

    @Mock
    private var view: TeamMembersView? = null

    private var logger: Log = UnitTestingLogger()

    private var presenter: TeamMembersPresenter? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {

        val immediate = object : Scheduler() {
            override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Scheduler.Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }


        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }

        MockitoAnnotations.initMocks(this)
        presenter = TeamMembersPresenter(teamRepository!!, statusRepository!!, logger)
        presenter!!.attach(view!!)
    }

    @Test
    fun testTeamMembersDisplayed() {
        `when`(teamRepository!!.getTeam()).thenReturn(Observable.just(ArrayList()))
        presenter!!.getTeamMembers()
        verify(view)!!.showTeamMembers(ArrayList())
    }

    @Test
    fun testUpdateStatusCalled() {
        val status = Status("nicolashleon", "new status")
        `when`(statusRepository!!.getMemberStatusUpdates()).thenReturn(Flowable.just(status))
        presenter!!.getStatusUpdates()
        verify(view)!!.updateTeamMemberStatus(status)
    }

    @Test
    fun testNewTeamMemberCalled() {
        val teamMember = TeamMember()
        `when`(statusRepository!!.getNewMemberUpdates()).thenReturn(Flowable.just(teamMember))
        presenter!!.getNewMemberUpdates()
        verify(view)!!.addNewTeamMember(teamMember)
    }
}
