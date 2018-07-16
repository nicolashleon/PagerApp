package com.pager.teamapp.logger

import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException

class UnitTestingLogger : Log {

    override fun e(tag: String, message: String, tr: Throwable?) {
        println(tag, message + '\n'.toString() + getStackTraceString(tr))
    }

    override fun w(tag: String, message: String, tr: Throwable?) {
        println(tag, message + '\n'.toString() + getStackTraceString(tr))
    }

    override fun v(tag: String, message: String) {
        println(tag, message)
    }

    override fun d(tag: String, message: String) {
        println(tag, message)
    }

    fun println(vararg messages: String?) {
        System.out.println(messages.joinToString())
    }

    fun getStackTraceString(tr: Throwable?): String {
        if (tr == null) {
            return ""
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        var t = tr
        while (t != null) {
            if (t is UnknownHostException) {
                return ""
            }
            t = t.cause
        }

        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}