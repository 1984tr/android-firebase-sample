package com.tr1984.firebasesample.util

import android.util.Log
import com.tr1984.firebasesample.BuildConfig

object Logger {

    private const val tag = "FirebaseSample"

    fun d(format: String, vararg msg: Any) {
        d(String.format(format, msg))
    }

    fun d(msg: String) {
        log("d", msg)
    }

    fun v(format: String, vararg msg: Any) {
        v(String.format(format, msg))
    }

    fun v(msg: String) {
        log("v", msg)
    }

    fun i(format: String, vararg msg: Any) {
        i(String.format(format, msg))
    }

    fun i(msg: String) {
        log("i", msg)
    }

    fun w(format: String, vararg msg: Any) {
        w(String.format(format, msg))
    }

    fun w(msg: String) {
        log("w", msg)
    }

    fun e(format: String, vararg msg: Any) {
        e(String.format(format, msg))
    }

    fun e(msg: String) {
        log("e", msg)
    }

    private fun log(type: String, msg: String) {
        if (!BuildConfig.DEBUG) {
            return
        }
        when (type) {
            "d" -> Log.d(tag, "😀$msg")
            "v" -> Log.v(tag, "😀$msg")
            "i" -> Log.i(tag, "😀$msg")
            "w" -> Log.w(tag, "😨$msg")
            "e" -> Log.e(tag, "😡$msg")
        }
    }


}