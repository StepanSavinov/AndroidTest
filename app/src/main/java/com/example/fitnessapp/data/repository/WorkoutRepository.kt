package com.example.fitnessapp.data.repository

import com.example.fitnessapp.data.network.ApiService
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getWorkouts() = apiService.getWorkouts()
}