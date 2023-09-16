package com.example.moves

import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi

class Bienvenida : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)


        Handler(Looper.getMainLooper()).postDelayed({

            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },GlobalVariables.splashTime)
    }

}