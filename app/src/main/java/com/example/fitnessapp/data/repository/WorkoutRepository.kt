package com.example.fitnessapp.data.repository

import com.example.fitnessapp.data.network.RetrofitInstance

class WorkoutRepository {
    suspend fun getWorkouts() = RetrofitInstance.api.getWorkouts()
}