package com.pager.teamapp.repositories

import com.pager.teamapp.BuildConfig
import com.pager.teamapp.models.Update
import com.pager.teamapp.network.StatusService
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*

class StatusRepository {
    fun getUpdates(): Flowable<Update> {
        val scarletInstance = Scarlet.Builder()
                .webSocketFactory(OkHttpClient().newWebSocketFactory(BuildConfig.WS_BASE_URL))
                .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
                .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
                .build()
        val scarletClient = scarletInstance.create<StatusService>()
        return scarletClient
                .openConnection()
                .flatMap { _ -> scarletClient.getStatusUpdates() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun getUpdatesOkHttp(): Flowable<Any> {
        val request = Request.Builder()
                .url(BuildConfig.WS_BASE_URL)
                .build()

        return Observable.create(ObservableOnSubscribe<Any> { emitter ->
            OkHttpClient().newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket?, response: Response?) {
                    super.onOpen(webSocket, response)
                    emitter.onNext("OPEN")
                }

                override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    if (t == null) {
                        emitter.onError(Exception("Websocket closed"))
                    } else {
                        emitter.onError(t)
                    }
                }

                override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
                    super.onClosing(webSocket, code, reason)
                    emitter.onNext("CLOSING")
                }

                override fun onMessage(webSocket: WebSocket?, text: String?) {
                    super.onMessage(webSocket, text)
                    text?.let { it1 -> emitter.onNext(it1) }
                }

                override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
                    super.onClosed(webSocket, code, reason)
                    emitter.onComplete()
                }
            })
        }).toFlowable(BackpressureStrategy.DROP)


    }
}