package com.example.fitnessapp.ui.workoutlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.data.model.Workout
import com.example.fitnessapp.data.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutListViewModel : ViewModel() {
    private val repo = WorkoutRepository()

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadWorkouts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _workouts.value = repo.getWorkouts()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }
}
