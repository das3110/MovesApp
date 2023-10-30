package com.example.moves

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlin.system.exitProcess
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAcercade = findViewById<Button>(R.id.button_acercade)
        val buttonPreferencias =findViewById<Button>(R.id.button_preferencias)
        val buttonSalir = findViewById<Button>(R.id.button_salir)
        val buttonJugar = findViewById<Button>(R.id.button_jugar)
        val user= findViewById<TextView>(R.id.txtUser)
        user.setText(GlobalVariables.userName)

        mediaPlayer = MediaPlayer.create(this, R.raw.menu)
        mediaPlayer.isLooping = true
        iniciarMusica()
        buttonJugar.setOnClickListener {
            val intent= Intent(this,Ingame::class.java )
            startActivity(intent)
            finish()
            onDestroy()
        }
        buttonAcercade.setOnClickListener{
            val intent= Intent(this,AcercaDe::class.java )
            startActivity(intent)
            finish()
            onDestroy()
        }
        buttonPreferencias.setOnClickListener {
            val intent= Intent(this,Preferencias::class.java )
            startActivity(intent)
            finish()
            onDestroy()
        }
        buttonSalir.setOnClickListener{
            finish()
        }
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