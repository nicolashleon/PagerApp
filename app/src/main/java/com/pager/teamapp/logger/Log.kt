package com.pager.teamapp.logger

interface Log {

    fun e(tag: String, message: String, tr: Throwable? = null)

    fun w(tag: String, message: String, tr: Throwable? = null)

    fun v(tag: String, message: String)

    fun d(tag: String, message: String)
}