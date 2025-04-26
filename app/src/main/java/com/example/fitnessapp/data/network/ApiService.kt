package com.example.fitnessapp.data.network

import com.example.fitnessapp.data.model.Workout
import com.example.fitnessapp.ui.videoplayer.VideoWorkout
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/get_workouts")
    suspend fun getWorkouts(): List<Workout>

    @GET("/get_video")
    suspend fun getVideo(@Query("id") id: Int): VideoWorkout

}
