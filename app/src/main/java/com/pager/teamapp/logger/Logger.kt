package com.pager.teamapp.logger

import android.util.Log

class Logger : com.pager.teamapp.logger.Log {

    override fun e(tag: String, message: String, tr: Throwable?) {
        Log.e(tag, message, tr)
    }

    override fun w(tag: String, message: String, tr: Throwable?) {
        Log.w(tag, message, tr)
    }

    override fun v(tag: String, message: String) {
        Log.v(tag, message)
    }

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
}