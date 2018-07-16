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
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

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


        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }

        MockitoAnnotations.initMocks(this)
        presenter = TeamMembersPresenter(teamRepository!!, statusRepository!!, logger)
        presenter!!.attach(view!!)
    }

    @Test
    fun testPrimaryConstructor() {
        val presenter = TeamMembersPresenter(teamRepository!!, statusRepository!!, logger)
        assertNull(presenter.baseView)
    }

    @Test
    fun testSecondaryConstructor() {
        val presenter = TeamMembersPresenter(teamRepository!!, statusRepository!!, logger)
        assertNull(presenter.baseView)
    }

    @Test
    fun testAttachView() {
        presenter!!.attach(view!!)
        assertNotNull(presenter!!.baseView)
        assertEquals(view!!, presenter!!.baseView,
                "the view associated with the presenter is not the same as the one used" +
                        " on the init process")
    }

    @Test
    fun testDetachView() {
        presenter!!.detach()
        assertNull(presenter!!.baseView)
    }

    @Test
    fun testTeamMembersCalled() {
        `when`(teamRepository!!.getTeam()).thenReturn(Observable.just(ArrayList()))
        presenter!!.getTeamMembers()
        verify(view)!!.showLoader(true)
        verify(view)!!.showTeamMembers(ArrayList())
        verify(view, atLeastOnce())!!.showLoader(false)
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

    @Test
    fun testTeamMembersDisplayError() {
        `when`(teamRepository!!.getTeam()).thenReturn(Observable.error(Exception()))
        presenter!!.getTeamMembers()
        verify(view)!!.showLoader(true)
        verify(view, atLeastOnce())!!.showLoader(false)
        verify(view)!!.showError()
    }

    @Test
    fun testNewTeamMembersDisplayError() {
        `when`(statusRepository!!.getNewMemberUpdates()).thenReturn(Flowable.error(Exception()))
        presenter!!.getNewMemberUpdates()
        verify(view)!!.showNewTeamMembersError()
    }

    @Test
    fun testStatusUpdateDisplayError() {
        `when`(statusRepository!!.getMemberStatusUpdates()).thenReturn(Flowable.error(Exception()))
        presenter!!.getStatusUpdates()
        verify(view)!!.showStatusUpdatesError()
    }

    @Test
    fun testNewTeamMembersNoResult() {
        `when`(statusRepository!!.getNewMemberUpdates()).thenReturn(Flowable.never())
        presenter!!.getNewMemberUpdates()
        verify(view, never())!!.showNewTeamMembersError()
    }

    @Test
    fun testStatusUpdateNoResult() {
        `when`(statusRepository!!.getMemberStatusUpdates()).thenReturn(Flowable.never())
        presenter!!.getStatusUpdates()
        verify(view, never())!!.showStatusUpdatesError()
    }
}
