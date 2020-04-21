package com.example.studytimer

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
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
                meter?.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                meter?.start()
                isWorking = true
            }
            Toast.makeText(this,"start",Toast.LENGTH_LONG).show()
        }
        pause.setOnClickListener {
            if (isWorking) {
                meter?.stop()
                pauseOffset = SystemClock.elapsedRealtime() - meter?.getBase()!!
                isWorking = false
            }
            Toast.makeText(this,"pause",Toast.LENGTH_LONG).show()

        }
        save.setOnClickListener {
            saveData()
            textView.text = "you spend ${getData()} time for studing lasttime"
            meter?.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        st = meter?.text.toString()

        outState.putString("last_time",st)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        meter?.setText(savedInstanceState.getString("last_time"))
    }
}
