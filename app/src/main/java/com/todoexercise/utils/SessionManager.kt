package com.todoexercise.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.securepreferences.SecurePreferences

object SessionManager {
    private const val KEY_LAST_ID             = "UserSessionManager.KEY_LAST_ID"

    public val triggerRefresh = SingleLiveEvent<Boolean>()

    private lateinit var sharedPref: SecurePreferences

    fun init(context: Context) {
        sharedPref = SecurePreferences(context)
    }

    var lastId: Int
        get() = sharedPref.getInt(KEY_LAST_ID, 0) ?: 0
        set(value) {
            sharedPref.edit { it.putInt(KEY_LAST_ID, value) }
        }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    fun clearData() {
        sharedPref.edit().clear()
    }

}