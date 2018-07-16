package com.pager.teamapp.ui.presenters

import com.pager.teamapp.ui.views.BaseView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class Presenter<BV : BaseView> {

    internal var baseView: BV? = null

    private var disposables: CompositeDisposable = CompositeDisposable()

    fun attach(view: BV) {
        baseView = view
    }

    fun detach() {
        disposables.dispose()
        baseView = null
    }

    protected fun addDisposable(disposable: Disposable) {
        if (disposables.isDisposed) {
            disposables = CompositeDisposable()
        }
        disposables.add(disposable)
    }
}