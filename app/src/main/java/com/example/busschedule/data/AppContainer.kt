package com.example.busschedule.data

import android.content.Context

interface AppContainer {
    val busSchedulesRepository: BusSchedulesRepository
}

class AppDataContainer(private val context: Context) : AppContainer{
    override val busSchedulesRepository by lazy {
        OfflineBusSchedulesRepository(BusScheduleDatabase.getDatabase(context).busScheduleDao())
    }
}