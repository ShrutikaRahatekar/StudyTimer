package com.example.studytimer

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.widget.Chronometer
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var meter:Chronometer? = null
    var preference: SharedPreferences? = null
    var isWorking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       preference = getPreferences(Context.MODE_PRIVATE)
        meter = findViewById<Chronometer>(R.id.c_meter)

        //access the button using id
//        val btn = findViewById<Button>(R.id.btn)
//        btn?.setOnClickListener(object : View.OnClickListener {
//
//            var isWorking = false
//
//            override fun onClick(v: View) {
//                if (!isWorking) {
//                    meter.start()
//                    isWorking = true
//                } else {
//                    meter.stop()
//                    isWorking = false
//                }
//
//                btn.setText(if (isWorking) R.string.start else R.string.stop)
//
//                Toast.makeText(
//                    this@MainActivity, getString(
//                        if (isWorking)
//                            R.string.working
//                        else
//                            R.string.stopped
//                    ),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
        start.setOnClickListener {
            if (!isWorking) {
                meter?.start()
                isWorking = true
            }
            Toast.makeText(this,"start",Toast.LENGTH_LONG).show()
        }
        pause.setOnClickListener {
            if (isWorking) {
                meter?.stop()
                isWorking = false
            }
            Toast.makeText(this,"pause",Toast.LENGTH_LONG).show()

        }
        save.setOnClickListener {
            saveData()
            textView.text = "you spend ${getData()} time for studing lasttime"
            Toast.makeText(this,"save",Toast.LENGTH_LONG).show()
        }

    }
    fun saveData(){
        val editor=preference?.edit() ?: return
        editor.putString("last_read",meter?.text.toString())
        editor.apply()
    }

    fun getData(): String{
        return preference?.getString("last_read", "00:00") ?: "00:00"
    }
}
