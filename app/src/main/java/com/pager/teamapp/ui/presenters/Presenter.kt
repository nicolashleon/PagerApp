package com.pager.teamapp.ui.presenters

import com.pager.teamapp.ui.views.BaseView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class Presenter<BV : BaseView> {

    var baseView: BV? = null
    var disposables: CompositeDisposable = CompositeDisposable()

    fun attach(view: BV) {
        baseView = view
    }

    fun dettach() {
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