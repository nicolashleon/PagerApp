package com.pager.teamapp

import android.support.annotation.NonNull
import android.util.Log
import com.pager.teamapp.repositories.TeamRepository
import com.pager.teamapp.ui.models.TeamMember
import com.pager.teamapp.ui.presenters.TeamMembersPresenter
import com.pager.teamapp.ui.views.TeamMembersView
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class TeamMembersPresenterTest {

    @Mock
    var teamRepository: TeamRepository? = null

    @Mock
    var view: TeamMembersView? = null

    var presenter: TeamMembersPresenter? = null

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
        Mockito.mock(Log::class.java)
        `when`(teamRepository!!.getTeam()).thenReturn(Observable.just(ArrayList<TeamMember>()))
        presenter = TeamMembersPresenter()
        presenter!!.attach(this!!.view!!)
    }

    @Test
    fun testDisplayCalled() {
        presenter!!.getTeamMembers()
        verify(view)!!.showTeamMembers(ArrayList<TeamMember>())
    }
}
