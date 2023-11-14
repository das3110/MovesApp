package com.example.moves

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

private const val PREF_NAME = "Preferencias"
private const val PREF_DIFFICULTY = "pref_difficulty"

class Preferencias : AppCompatActivity() {


    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferencias)

        val botonok= findViewById<Button>(R.id.buttonOk2)
        val user= findViewById<TextView>(R.id.txtUsername)
        val ptj = findViewById<TextView>(R.id.textView6)

        val sharedPref = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE)

        val maxScore = obtenerMaximoPuntaje()
        val userWithMaxScore = obtenerUsuarioConMaximoPuntaje()

        ptj.text="$userWithMaxScore con $maxScore pts"
        user.text=sharedPref.getString("user_name", null)


        val btnResetPuntaje = findViewById<Button>(R.id.button2)
        btnResetPuntaje.setOnClickListener {
            resetearPuntaje()
            Toast.makeText(this, "Puntaje máximo restablecido", Toast.LENGTH_SHORT).show()
        }

        botonok.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java )
            startActivity(intent)
            this.finish()
        }
        val botonsplash=findViewById<Button>(R.id.buttonsplashscreen)
        botonsplash.setOnClickListener{
            showChangeTime()
        }
        val botondificultad=findViewById<Button>(R.id.buttonDificultad)
        botondificultad.setOnClickListener{
            showChangeDifficulty()
        }
        val botonActualizar=findViewById<Button>(R.id.buttonNombre)
        botonActualizar.setOnClickListener{
            ActualizarNombre()
        }
        val switch = findViewById<Switch>(R.id.switch1)
        val sharedPreferences= getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val nightMode = sharedPreferences.getBoolean("night", false)
        if(nightMode){
            switch.isChecked=true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        switch.setOnCheckedChangeListener{ buttonview, isChecked->
            if(!isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("night", false)
                editor.apply()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("night", true)
                editor.apply()
            }
        }
    }
    private fun resetearPuntaje() {
        val sharedPref = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt("puntuacion_maxima", 0)
        editor.putString("user_with_max_score","Nadie")
        editor.apply()
    }
    private fun ActualizarNombre() {
        val inputUsername = findViewById<EditText>(R.id.txtIngresarNombre).text.toString()

        val sharedPref = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("user_name", inputUsername)
        editor.apply()

        GlobalVariables.userName = inputUsername

        recreate()
    }
    private fun showChangeTime(){
        val listTimes = arrayOf ("1","2","3","4","5")

        val xBuilder = AlertDialog.Builder( this@Preferencias)
        xBuilder.setTitle("Seleccione el tiempo deseado")
        xBuilder.setSingleChoiceItems(listTimes, -1){ dialog, wich ->
            if (wich==0){
                actualizarTiempo(1000)
                recreate()
            }else if(wich==1){
                actualizarTiempo(2000)
                recreate()
            }else if(wich==2){
                actualizarTiempo(3000)
                recreate()
            }else if(wich==3){
                actualizarTiempo(4000)
                recreate()
            }else if(wich==4){
                actualizarTiempo(5000)
                recreate()
            }
            dialog.dismiss()

        }
        val xDialog = xBuilder.create()
        xDialog.show()

    }
    private fun actualizarTiempo(dato:Long ){
        GlobalVariables.splashTime= dato
    }
    private fun showChangeDifficulty() {
        val difficultyLevels = arrayOf("Fácil", "Normal", "Difícil")

        val builder = AlertDialog.Builder(this@Preferencias)
        builder.setTitle("Seleccione el nivel de dificultad")

        builder.setSingleChoiceItems(difficultyLevels, obtenerDificultadGuardada().ordinal) { dialog, which ->
            val selectedDifficulty = when (which) {
                0 -> Dificultad.FACIL
                1 -> Dificultad.NORMAL
                2 -> Dificultad.DIFICIL
                else -> Dificultad.FACIL
            }

            guardarDificultad(selectedDifficulty)
            Toast.makeText(this, "Dificultad guardada: ${selectedDifficulty.name}", Toast.LENGTH_SHORT).show()

            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun guardarDificultad(dificultad: Dificultad) {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(PREF_DIFFICULTY, dificultad.ordinal)
        editor.apply()
    }
    private fun obtenerDificultadGuardada(): Dificultad {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val ordinal = sharedPreferences.getInt(PREF_DIFFICULTY, Dificultad.FACIL.ordinal)
        return Dificultad.values().getOrElse(ordinal) { Dificultad.FACIL }
    }
    private fun obtenerMaximoPuntaje(): Int {
        val sharedPref = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE)
        return sharedPref.getInt("puntuacion_maxima", 0)
    }
    private fun obtenerUsuarioConMaximoPuntaje(): String? {
        val sharedPref = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE)
        return sharedPref.getString("user_with_max_score", null)
    }
}