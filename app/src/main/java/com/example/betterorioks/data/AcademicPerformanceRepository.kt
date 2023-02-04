package com.example.betterorioks.data

import com.example.betterorioks.model.Subject
import com.example.betterorioks.network.AcademicPerformanceApiService

interface AcademicPerformanceRepository{
    suspend fun getAcademicPerformance():List<Subject>
}

class NetworkAcademicPerformanceRepository(private val academicPerformanceApiService: AcademicPerformanceApiService, private val token:String):AcademicPerformanceRepository
{
    override suspend fun getAcademicPerformance():List<Subject> = academicPerformanceApiService.getAcademicPerformance(token = token)
}