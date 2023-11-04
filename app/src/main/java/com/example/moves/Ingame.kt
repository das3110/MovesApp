package com.example.moves

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import java.util.Random
import android.os.CountDownTimer
import android.os.Handler


private lateinit var mediaPlayer: MediaPlayer
class Ingame : AppCompatActivity(), GestureDetector.OnGestureListener, SensorEventListener {
    private lateinit var gestureDetector: GestureDetector
    private lateinit var puntuacionTextView: TextView
    private lateinit var textoAleatorio: TextView
    private var puntuacion = 0
    private val palabras = arrayOf("Arriba", "Abajo","Agitar","Toque")
    private lateinit var countDownTimer: CountDownTimer
    private var tiempoRestanteMillis: Long = 10000
    private val umbralAcelerometro = 10.0
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private var lastAcceleration = 0f
    private var acceleration = 0f
    private var puntuacionMaxima = 0
    private val handler = Handler()
    private lateinit var tiempoRestanteTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingame)
        mediaPlayer = MediaPlayer.create(this, R.raw.ingame )
        mediaPlayer.isLooping = true
        iniciarMusica()
        gestureDetector = GestureDetector(this, this)
        tiempoRestanteTextView = findViewById(R.id.tiempoRestanteTextView)

        puntuacionTextView = findViewById(R.id.puntuacionTextView)
        textoAleatorio = findViewById(R.id.textView2)

        val sharedPref = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE)
        puntuacionMaxima = sharedPref.getInt("puntuacion_maxima", 0)

        actualizarPuntuacion()
        actualizarTextoAleatorio()
        iniciarTemporizador(tiempoRestanteMillis)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        val textoMostrado = textoAleatorio.text.toString()
        if (textoMostrado == "Toque") {
            incrementarPuntuacion()
        } else {
            mostrarPantallaDerrota()
        }
        actualizarTextoAleatorio()
        return true
    }
    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val deltaY = e2.y - e1.y
        val textoMostrado = textoAleatorio.text.toString()
        if (Math.abs(deltaY) < 50) {
            return true
        }
        if (deltaY < 0 && Math.abs(deltaY) > 100 && textoMostrado == "Arriba") {
            incrementarPuntuacion()
        } else if (deltaY > 0 && Math.abs(deltaY) > 100 && textoMostrado == "Abajo") {
            incrementarPuntuacion()
        } else {
            mostrarPantallaDerrota()
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

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return true
    }
    override fun onLongPress(e: MotionEvent) {
    }
    private fun actualizarPuntuacion() {
        val sharedPref = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE)
        puntuacionMaxima = sharedPref.getInt("puntuacion_maxima", 0)

        puntuacionTextView.text = "Puntuación Máxima: $puntuacionMaxima / Puntuación: $puntuacion"
    }
    private fun incrementarPuntuacion() {
        puntuacion++
        if (puntuacion > puntuacionMaxima) {
            puntuacionMaxima = puntuacion

            val sharedPref = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putInt("puntuacion_maxima", puntuacionMaxima)
            editor.apply()
        }
        actualizarPuntuacion()
    }
    private fun actualizarTextoAleatorio() {
        val random = Random()
        val palabraAleatoria = palabras[random.nextInt(palabras.size)]
        textoAleatorio.text = palabraAleatoria
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            actualizarTextoAleatorio()
        }, 3000)
    }
    private fun actualizarTiempoRestante() {
        val segundosRestantes = tiempoRestanteMillis / 1000
        val minutos = segundosRestantes / 60
        val segundos = segundosRestantes % 60
        val tiempoRestanteFormateado = String.format("%02d:%02d", minutos, segundos)
        tiempoRestanteTextView.text = tiempoRestanteFormateado
    }
    private fun mostrarPantallaDerrota() {
        countDownTimer.cancel()
        onDestroy()
        val intent = Intent(this, Derrota::class.java)
        intent.putExtra("puntuacion", puntuacion)
        startActivity(intent)
        finish()
    }
    private fun mostrarPantallaVictoria() {
        countDownTimer.cancel()
        onDestroy()
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
        handler.removeCallbacksAndMessages(null)
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = acceleration
            acceleration = x * x + y * y + z * z

            if (acceleration - lastAcceleration > umbralAcelerometro) {
                if (textoAleatorio.text == "Agitar") {
                    incrementarPuntuacion()
                    actualizarTextoAleatorio()
                }else if (textoAleatorio.text == "Arriba"){

                }else if (textoAleatorio.text == "Abajo"){

                }else if (textoAleatorio.text == "Toque"){

                }
                else {
                    mostrarPantallaDerrota()
                }
            }
        }
    }




}