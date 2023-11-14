package com.example.moves

import android.content.Context
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
    private lateinit var user: TextView
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("user_name", null)

        if (GlobalVariables.userName == "" && userName.isNullOrEmpty()) {
            introducirNombre()
        } else {

            user = findViewById(R.id.txtUser)
            user.text = userName ?: GlobalVariables.userName
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.menu)
        mediaPlayer.isLooping = true
        iniciarMusica()

        val buttonAcercade = findViewById<Button>(R.id.button_acercade)
        val buttonPreferencias = findViewById<Button>(R.id.button_preferencias)
        val buttonSalir = findViewById<Button>(R.id.button_salir)
        val buttonJugar = findViewById<Button>(R.id.button_jugar)

        buttonJugar.setOnClickListener {
            val intent = Intent(this, Ingame::class.java)
            startActivity(intent)
            finish()
        }

        buttonAcercade.setOnClickListener {
            val intent = Intent(this, AcercaDe::class.java)
            startActivity(intent)
            finish()
        }

        buttonPreferencias.setOnClickListener {
            val intent = Intent(this, Preferencias::class.java)
            startActivity(intent)
            finish()
        }

        buttonSalir.setOnClickListener {
            finish()
        }
    }
    private fun introducirNombre() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_introducir_nombre, null)
        dialogBuilder.setView(dialogView)

        val editTextNombre = dialogView.findViewById<EditText>(R.id.editTextNombre)

        dialogBuilder.setTitle("Ingresa tu nombre")
        dialogBuilder.setPositiveButton("Aceptar") { _, _ ->
            val nombreIngresado = editTextNombre.text.toString()

            val editor = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE).edit()
            editor.putString("user_name", nombreIngresado)
            editor.apply()
            user.text = nombreIngresado
            GlobalVariables.userName=nombreIngresado
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
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