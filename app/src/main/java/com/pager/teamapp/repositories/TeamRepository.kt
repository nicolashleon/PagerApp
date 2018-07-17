package com.pager.teamapp.repositories

import com.pager.teamapp.BuildConfig
import com.pager.teamapp.extensions.EMPTY_STRING
import com.pager.teamapp.models.Person
import com.pager.teamapp.network.TeamService
import com.pager.teamapp.ui.models.TeamMember
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TeamRepository {
    private val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.APP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    private val service = retrofit.create(TeamService::class.java)

    fun getTeam(): Observable<List<TeamMember>> {

        return service.getTeam().flatMap({
            service.getRoles()
        }, { persons: List<Person>, roles: Map<String, String> ->
            var members: List<TeamMember> = ArrayList()
            persons.forEach {
                members = members.plus(TeamMember(
                        it.name,
                        it.avatar,
                        roles.getOrDefault(it.role.toString(), String.EMPTY_STRING()),
                        it.languages,
                        it.tags,
                        it.location,
                        github = it.github))
            }
            members
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}