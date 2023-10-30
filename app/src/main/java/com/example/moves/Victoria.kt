package com.example.moves

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

private lateinit var mediaPlayer: MediaPlayer
class Victoria : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_victoria)
        val puntuacion = intent.getIntExtra("puntuacion", 0)

        val puntuacionTextView = findViewById<TextView>(R.id.puntuacionDerrotatxt)
        puntuacionTextView.text = "Puntuación: $puntuacion"
        val buttonOk = findViewById<Button>(R.id.button)

        buttonOk.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java )
            startActivity(intent)
            finish()
            onDestroy()
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.victoria )
        mediaPlayer.isLooping = true
        iniciarMusica()
    }
    private fun iniciarMusica() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}