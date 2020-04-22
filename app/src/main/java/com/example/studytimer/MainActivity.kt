package com.example.studytimer

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var meter:Chronometer? = null
    var preference: SharedPreferences? = null
    var isWorking = false
    public var pauseOffset: Long=0
    var st:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       preference = getPreferences(Context.MODE_PRIVATE)
        meter = findViewById(R.id.c_meter)

        start.setOnClickListener {
            if (!isWorking) {
                meter?.base = SystemClock.elapsedRealtime() - pauseOffset
                meter?.start()
                isWorking = true
            }
            Toast.makeText(this,"start",Toast.LENGTH_LONG).show()
        }
        pause.setOnClickListener {
            if (isWorking) {
                meter?.stop()
                pauseOffset = SystemClock.elapsedRealtime() - (meter?.base ?: 0)
                isWorking = false
            }
            Toast.makeText(this,"pause",Toast.LENGTH_LONG).show()

        }
        save.setOnClickListener {
            saveData()
            textView.text = "you spend ${getData()} time for studing lasttime"
            meter?.base = SystemClock.elapsedRealtime();
            pauseOffset = 0;
            Toast.makeText(this,"save",Toast.LENGTH_LONG).show()
        }

    }
    private fun saveData(){
        val editor=preference?.edit() ?: return
        editor.putString("last_read",meter?.text.toString())
        editor.apply()
    }

    private fun getData(): String{
        return preference?.getString("last_read", "00:00") ?: "00:00"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            pauseOffset = SystemClock.elapsedRealtime() - (meter?.base ?: 0)

            putLong("last_time", pauseOffset)
            putBoolean("isWorking", isWorking)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        pauseOffset = savedInstanceState.getLong("last_time")
        meter?.base = SystemClock.elapsedRealtime() - pauseOffset
        isWorking = savedInstanceState.getBoolean("isWorking")
        if(isWorking){
            meter?.start()
        }
        super.onRestoreInstanceState(savedInstanceState)
    }
}
