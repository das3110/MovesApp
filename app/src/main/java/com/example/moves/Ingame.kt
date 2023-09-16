package com.example.moves

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Ingame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingame)
        val botoningame= findViewById<Button>(R.id.buttonIngame)

        botoningame.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java )
            startActivity(intent)
            this.finish()
        }
    }
}