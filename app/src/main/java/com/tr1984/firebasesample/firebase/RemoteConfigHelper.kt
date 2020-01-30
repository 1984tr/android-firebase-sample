package com.tr1984.firebasesample.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson

class RemoteConfigHelper private constructor() {

    private object Holder {
        val INSTANCE = RemoteConfigHelper()
    }

    companion object {
        val instance: RemoteConfigHelper by lazy { Holder.INSTANCE }
        private val minimumFetchIntervalInSeconds = 2 * 60 * 60L // second
    }

    private val remoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(minimumFetchIntervalInSeconds)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(Key.defaults())
    }

    fun fetchAndActivate(completion: (Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion.invoke(true)
                } else {
                    completion.invoke(false)
                }
            }
    }

    fun getBoolean(key: Key): Boolean? = remoteConfig.getBoolean(key.name)

    fun getLong(key: Key): Long? = remoteConfig.getLong(key.name)

    fun getString(key: Key): String? = remoteConfig.getString(key.name)

    fun <T> get(key: Key, clazz: Class<T>): T {
        val jsonString = remoteConfig.getString(key.name)
        return Gson().fromJson(jsonString, clazz)
    }

    enum class Key {
        MINIMUM_VERSION;

        companion object {
            fun defaults(): HashMap<String, Any> {
                return hashMapOf("MINIMUM_VERSION" to "0.0.1")
            }
        }
    }
}