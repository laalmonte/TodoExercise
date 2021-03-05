package com.todoexercise

import android.app.Application
import com.todoexercise.utils.SessionManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ToDoExercise : Application(){
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
    }
}