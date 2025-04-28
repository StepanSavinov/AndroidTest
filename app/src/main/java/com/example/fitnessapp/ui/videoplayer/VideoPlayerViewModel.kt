package com.example.fitnessapp.ui.videoplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.data.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VideoWorkout(val id: Int, val duration: String, val link: String)

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val apiService: ApiService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _videoData = MutableLiveData<VideoWorkout?>()
    val videoData: LiveData<VideoWorkout?> = _videoData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        val workoutId = savedStateHandle.get<Int>("id") ?: -1
        if (workoutId != -1) {
            loadVideo(workoutId)
        }
    }

    fun loadVideo(id: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val result = apiService.getVideo(id)
                _videoData.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
