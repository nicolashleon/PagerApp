package com.pager.teamapp.repositories

import com.pager.teamapp.BuildConfig
import com.pager.teamapp.models.Update
import com.pager.teamapp.network.StatusService
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient

class StatusRepository {
    fun getUpdates() : Flowable<Update> {
        val scarletInstance = Scarlet.Builder()
                .lifecycle()
                .webSocketFactory(OkHttpClient().newWebSocketFactory(BuildConfig.WS_BASE_URL))
                .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
                .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
                .build()
        return scarletInstance
                .create(StatusService::class.java)
                .getStatusUpdates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }
}