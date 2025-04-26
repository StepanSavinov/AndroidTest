package com.example.fitnessapp.data.model

data class Workout(
    val id: Int,
    val title: String,
    val description: String?,
    val type: Int,
    val duration: String
)
