package com.example.betterorioks

import android.app.Application
import com.example.betterorioks.data.AppContainer
import com.example.betterorioks.data.DefaultAppContainer

class BetterOrioksApplication:Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}