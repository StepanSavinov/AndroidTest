package com.example.fitnessapp.ui.videoplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.data.network.RetrofitInstance
import kotlinx.coroutines.launch

data class VideoWorkout(val id: Int, val duration: String, val link: String)

class VideoPlayerViewModel : ViewModel() {
    private val _videoData = MutableLiveData<VideoWorkout?>()
    val videoData: LiveData<VideoWorkout?> = _videoData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadVideo(id: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val result = RetrofitInstance.api.getVideo(id)
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
