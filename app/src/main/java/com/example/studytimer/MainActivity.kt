package com.example.studytimer

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var preference: SharedPreferences? = null
    var isWorking = false
    var pauseOffset: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preference = getPreferences(Context.MODE_PRIVATE)

        textView.text = "you spend ${getData()} time for studying last time."


        start.setOnClickListener {
            if (!isWorking) {
                c_meter.base = SystemClock.elapsedRealtime() - pauseOffset
                c_meter.start()
                isWorking = true
            }
            Toast.makeText(this, "start", Toast.LENGTH_LONG).show()
        }
        pause.setOnClickListener {
            if (isWorking) {
                c_meter.stop()
                pauseOffset = SystemClock.elapsedRealtime() - c_meter.base
                Log.d("HELLO: pause", pauseOffset.toString())
                Log.d("HELLO: base", c_meter.base.toString())
                Log.d("HELLO", SystemClock.elapsedRealtime().toString())
                isWorking = false
            }
            Toast.makeText(this, "pause", Toast.LENGTH_LONG).show()

        }
        save.setOnClickListener {
            if (isWorking) {
                c_meter.stop()
                pauseOffset = SystemClock.elapsedRealtime() - (c_meter.base ?: 0)
                isWorking = false
            }
            saveData()
            textView.text = "you spend ${getData()} time for studying last time."
            pauseOffset = 0;
            c_meter.base = SystemClock.elapsedRealtime();
            Toast.makeText(this, "save", Toast.LENGTH_LONG).show()
        }

    }

    private fun saveData() {
        if (isWorking) {
            c_meter.stop()
            pauseOffset = SystemClock.elapsedRealtime() - (c_meter.base ?: 0)
            isWorking = false
        }
        val editor = preference?.edit() ?: return
        editor.putString("last_read", c_meter.text.toString())
        editor.apply()
    }

    private fun getData(): String {
        return preference?.getString("last_read", "00:00") ?: "00:00"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putLong("last_time", SystemClock.elapsedRealtime() - c_meter.base)
            putBoolean("isWorking", isWorking)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        isWorking = savedInstanceState.getBoolean("isWorking")
        c_meter.base = SystemClock.elapsedRealtime() - savedInstanceState.getLong("last_time")
        if (isWorking) {
            c_meter.start()
        }
        super.onRestoreInstanceState(savedInstanceState)
    }
}