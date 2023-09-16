package com.example.moves

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AcercaDe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acerca_de)
        val buttonok= findViewById<Button>(R.id.buttonOk)

        buttonok.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java )
            startActivity(intent)
            this.finish()
        }
    }
}