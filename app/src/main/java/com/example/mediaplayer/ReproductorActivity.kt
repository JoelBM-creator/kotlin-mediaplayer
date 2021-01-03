package com.example.mediaplayer

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class ReproductorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Llamamos el metodo fullScreen
        setFullScreenMetodo()
        setContentView(R.layout.activity_reproductor)
        // Ocutaltamos el ActionBar.
        supportActionBar?.hide()

        // Creamos el PlayerView
        val playerView: PlayerView = findViewById(R.id.exoplayer_video)
        // Obtenemos el path del intent
        val path = intent.getStringExtra("path")
        // Si el path no es nulo, crearemos el productor de ExoPlayer.
        if(path != null) {
            val uri = Uri.parse(path)
            val simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
            val factory = DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "My App"))
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(factory).createMediaSource(uri)
            playerView.player = simpleExoPlayer
            playerView.keepScreenOn = true
            simpleExoPlayer.prepare(mediaSource)
            simpleExoPlayer.playWhenReady = true
        }
    }

    // Metodo para crear el reproductor en FullScreen (Si se gira la pantalla).
    private fun setFullScreenMetodo() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}