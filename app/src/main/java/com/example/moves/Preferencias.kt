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

class Preferencias : AppCompatActivity() {


    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_preferencias)

        val botonok= findViewById<Button>(R.id.buttonOk2)
        val user= findViewById<TextView>(R.id.txtUsername)
        user.setText(GlobalVariables.userName)

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
        editor.apply()
    }
    private fun ActualizarNombre(){
        val inputUsername =findViewById<EditText>(R.id.txtIngresarNombre).text.toString()
        GlobalVariables.userName=inputUsername
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


}