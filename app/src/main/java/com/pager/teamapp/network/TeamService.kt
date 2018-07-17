package com.pager.teamapp.network

import com.pager.teamapp.models.Person
import io.reactivex.Observable
import retrofit2.http.GET

interface TeamService {
    @GET("/team")
    fun getTeam(): Observable<List<Person>>

    @GET("/roles")
    fun getRoles(): Observable<Map<String, String>>
}