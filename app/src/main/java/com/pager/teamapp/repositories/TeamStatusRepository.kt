package com.pager.teamapp.repositories

import com.pager.teamapp.BuildConfig
import com.pager.teamapp.extensions.EMPTY_STRING
import com.pager.teamapp.models.StatusUpdate
import com.pager.teamapp.models.TeamMemberUpdate
import com.pager.teamapp.network.StatusService
import com.pager.teamapp.network.TeamService
import com.pager.teamapp.ui.models.Status
import com.pager.teamapp.ui.models.TeamMember
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TeamStatusRepository {

    private val scarletInstance: Scarlet = Scarlet.Builder()
            .webSocketFactory(OkHttpClient().newWebSocketFactory(BuildConfig.WS_BASE_URL))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.APP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    private val scarletClient = scarletInstance.create<StatusService>()

    private val teamService = retrofit.create(TeamService::class.java)

    fun getNewMemberUpdates(): Flowable<TeamMember> {

        return scarletClient.openConnection()
                .flatMap { scarletClient.getNewMemberUpdates() }
                .zipWith(teamService.getRoles().toFlowable(BackpressureStrategy.ERROR))
                .flatMap { data: Pair<TeamMemberUpdate, Map<String, String>> ->
                    data.first.user.let {
                        Flowable.just(TeamMember(
                                it.name,
                                it.avatar,
                                data.second.getOrDefault(it.role.toString(), String.EMPTY_STRING()),
                                it.languages,
                                it.tags,
                                it.location,
                                github = it.github))
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun getMemberStatusUpdates(): Flowable<Status> {
        return scarletClient
                .openConnection()
                .flatMap { scarletClient.getMemberStatusUpdates() }
                .flatMap { statusUpdate: StatusUpdate ->
                    Flowable.just(Status(statusUpdate.user, statusUpdate.state))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun updateMemberStatus(status: String, githubId: String): Flowable<Boolean> {
        return scarletClient.openConnection()
                .flatMap {
                    Flowable.just(scarletClient.sendMemberStatusUpdates(StatusUpdate(user = githubId, state = status)))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }


}