package com.pager.teamapp.network

import com.pager.teamapp.models.Update
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.reactivex.Flowable

interface StatusService {
    @Receive
    fun openConnection(): Flowable<WebSocket.Event>

    @Receive
    fun getStatusUpdates(): Flowable<Update>
}