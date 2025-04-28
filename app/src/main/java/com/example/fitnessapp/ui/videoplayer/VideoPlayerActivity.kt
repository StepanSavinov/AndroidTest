package com.example.fitnessapp.ui.videoplayer

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import com.example.fitnessapp.R
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class VideoPlayerActivity : AppCompatActivity() {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var exoPlayer: ExoPlayer

    private val viewModel: VideoPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val workoutId = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title") ?: ""
        val duration = intent.getStringExtra("duration") ?: ""
        val description = intent.getStringExtra("description") ?: ""

        findViewById<TextView>(R.id.titleTextView).text = title
        findViewById<TextView>(R.id.durationTextView).text = duration
        findViewById<TextView>(R.id.descriptionTextView).text = description

        val playerView = findViewById<PlayerView>(R.id.playerView)
        playerView.player = exoPlayer

        viewModel.videoData.observe(this) { video ->
            if (video != null) {
                initializePlayer(video.link)
            }
        }

        viewModel.loadVideo(workoutId)
    }

    private fun initializePlayer(videoUrl: String) {

        val fullUrl = retrofit.baseUrl().host() + videoUrl
        val mediaItem = MediaItem.fromUri(fullUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

    }

    override fun onStop() {
        super.onStop()
        exoPlayer.release()
    }
}

