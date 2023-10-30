package com.example.moves

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import java.util.Random
import android.os.CountDownTimer


private lateinit var mediaPlayer: MediaPlayer
class Ingame : AppCompatActivity(), GestureDetector.OnGestureListener {
    private lateinit var gestureDetector: GestureDetector
    private lateinit var puntuacionTextView: TextView
    private lateinit var textoAleatorio: TextView
    private var puntuacion = 0

    private val palabras = arrayOf("Arriba", "Abajo")
    private lateinit var countDownTimer: CountDownTimer
    private var tiempoRestanteMillis: Long = 10000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingame)
        mediaPlayer = MediaPlayer.create(this, R.raw.ingame )
        mediaPlayer.isLooping = true
        iniciarMusica()
        gestureDetector = GestureDetector(this, this)
        puntuacionTextView = findViewById(R.id.puntuacionTextView)
        textoAleatorio = findViewById(R.id.textView2)

        actualizarPuntuacion()
        actualizarTextoAleatorio()
        iniciarTemporizador(tiempoRestanteMillis)
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val deltaY = e2.y - e1.y
        val textoMostrado = textoAleatorio.text.toString()

        if (deltaY < 0 && Math.abs(deltaY) > 100 && textoMostrado == "Arriba") {
            incrementarPuntuacion()
        } else if (deltaY > 0 && Math.abs(deltaY) > 100 && textoMostrado == "Abajo") {
            incrementarPuntuacion()
        } else {

            mostrarPantallaDerrota()
            onDestroy()
            return true
        }
        actualizarTextoAleatorio()

        return true
    }
    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent) {
    }
    private fun actualizarPuntuacion() {
        puntuacionTextView.text = "Puntuación: $puntuacion"
    }

    private fun incrementarPuntuacion() {
        puntuacion++
        actualizarPuntuacion()
    }
    private fun actualizarTextoAleatorio() {
        val random = Random()
        val palabraAleatoria = palabras[random.nextInt(palabras.size)]
        textoAleatorio.text = palabraAleatoria
    }
    private fun actualizarTiempoRestante() {

        val segundosRestantes = tiempoRestanteMillis / 1000
        val minutos = segundosRestantes / 60
        val segundos = segundosRestantes % 60
        val tiempoRestanteFormateado = String.format("%02d:%02d", minutos, segundos)
        val tiempoRestanteTextView = findViewById<TextView>(R.id.tiempoRestanteTextView)
        tiempoRestanteTextView.text = tiempoRestanteFormateado
    }
    private fun mostrarPantallaDerrota() {
        countDownTimer.cancel()
        val intent = Intent(this, Derrota::class.java)
        intent.putExtra("puntuacion", puntuacion)
        startActivity(intent)
        finish()
    }
    private fun mostrarPantallaVictoria() {

        countDownTimer.cancel()
        val intent = Intent(this, Victoria::class.java)
        intent.putExtra("puntuacion", puntuacion)
        startActivity(intent)
        finish()

    }
    private fun iniciarTemporizador(tiempoInicialMillis: Long) {
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }

        countDownTimer = object : CountDownTimer(tiempoInicialMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tiempoRestanteMillis = millisUntilFinished
                actualizarTiempoRestante()
            }
            override fun onFinish() {
                mostrarPantallaVictoria()
                onDestroy()
            }
        }.start()
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