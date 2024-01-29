package com.example.busschedule.data

import android.content.Context

interface AppContainer {
    val busScheduleDatabase: BusScheduleDatabase
}

class AppDataContainer(private val context: Context) : AppContainer{
    override val busScheduleDatabase by lazy {
        BusScheduleDatabase.getDatabase(context)
    }
}