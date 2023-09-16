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

class MainActivity : AppCompatActivity() {
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
        buttonJugar.setOnClickListener {
            val intent= Intent(this,Ingame::class.java )
            startActivity(intent)
            finish()
        }
        buttonAcercade.setOnClickListener{
            val intent= Intent(this,AcercaDe::class.java )
            startActivity(intent)
            finish()
        }
        buttonPreferencias.setOnClickListener {
            val intent= Intent(this,Preferencias::class.java )
            startActivity(intent)
            finish()
        }
        buttonSalir.setOnClickListener{
            finish()
        }
    }



}