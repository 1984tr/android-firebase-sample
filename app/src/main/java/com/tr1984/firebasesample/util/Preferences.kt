package com.tr1984.firebasesample.util

import android.content.Context
import android.content.SharedPreferences

object Preferences {

    private var pref: SharedPreferences? = null

    fun init(context: Context) {
        pref = context.getSharedPreferences("firebasesample_prefs", Context.MODE_PRIVATE)
    }

    fun put(key: Key, value: String) {
        pref?.run {
            val editor = edit()
            editor.putString(key.name, value)
            editor.commit()
        }
    }

    fun getString(key: Key) = pref?.getString(key.name, "")

    fun put(key: Key, value: Int) {
        pref?.run {
            val editor = edit()
            editor.putInt(key.name, value)
            editor.commit()
        }
    }

    fun getInt(key: Key) = pref?.getInt(key.name, 0)

    fun put(key: Key, value: Long) {
        pref?.run {
            val editor = edit()
            editor.putLong(key.name, value)
            editor.commit()
        }
    }

    fun getLong(key: Key) = pref?.getLong(key.name, 0)

    fun put(key: Key, value: Boolean) {
        pref?.run {
            val editor = edit()
            editor.putBoolean(key.name, value)
            editor.commit()
        }
    }

    fun getBoolean(key: Key) = pref?.getBoolean(key.name, false)

    fun put(key: Key, value: Float) {
        pref?.run {
            val editor = edit()
            editor.putFloat(key.name, value)
            editor.commit()
        }
    }

    fun getFloat(key: Key) = pref?.getFloat(key.name, 0f)

    enum class Key {
        FcmToken, UserToken, CreatedAt, Uid
    }
}