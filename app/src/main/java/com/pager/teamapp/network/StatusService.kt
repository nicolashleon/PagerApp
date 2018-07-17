package com.pager.teamapp.network

import com.pager.teamapp.models.StatusUpdate
import com.pager.teamapp.models.TeamMemberUpdate
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface StatusService {
    @Receive
    fun openConnection(): Flowable<WebSocket.Event>

    @Receive
    fun getNewMemberUpdates(): Flowable<TeamMemberUpdate>

    @Receive
    fun getMemberStatusUpdates(): Flowable<StatusUpdate>

    @Send
    fun sendMemberStatusUpdates(statusUpdate: StatusUpdate): Boolean
}