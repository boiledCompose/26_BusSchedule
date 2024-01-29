package com.example.busschedule.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface BusSchedulesRepository {
    fun getAllBusSchedules(): Flow<List<BusSchedule>>
    fun getBusScheduleByStopName(stopName: String): Flow<List<BusSchedule>>
}

class OfflineBusSchedulesRepository(private val busScheduleDao: BusScheduleDao): BusSchedulesRepository{
    override fun getAllBusSchedules(): Flow<List<BusSchedule>> =
        busScheduleDao.getAllBusSchedules()

    override fun getBusScheduleByStopName(stopName: String): Flow<List<BusSchedule>> =
        busScheduleDao.getBusScheduleByStopName(stopName)

}

