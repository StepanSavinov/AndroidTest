package com.example.fitnessapp.ui.videoplayer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.network.RetrofitInstance

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var viewModel: VideoPlayerViewModel
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val workoutId = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title") ?: ""
        val duration = intent.getStringExtra("duration") ?: ""
        val description = intent.getStringExtra("description") ?: ""

        viewModel = ViewModelProvider(this).get(VideoPlayerViewModel::class.java)

        findViewById<TextView>(R.id.titleTextView).text = title
        findViewById<TextView>(R.id.durationTextView).text = duration
        findViewById<TextView>(R.id.descriptionTextView).text = description

        viewModel.videoData.observe(this) { video ->
            if (video != null) {
                initializePlayer(video.link)
            }
        }

        viewModel.loadVideo(workoutId)
    }

    private fun initializePlayer(videoUrl: String) {
        val playerView = findViewById<PlayerView>(R.id.playerView)
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            playerView.player = exoPlayer
            val fullUrl = RetrofitInstance.BASE_URL + videoUrl
            val mediaItem = MediaItem.fromUri(fullUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }
}

