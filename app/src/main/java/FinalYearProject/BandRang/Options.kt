package FinalYearProject.BandRang

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Switch

@Suppress("DEPRECATION")
class Options : AppCompatActivity() {

    var switch1: Switch? = null
    var switch2: Switch? = null
    var back: Button? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        switch1 = findViewById(R.id.sw1) as Switch?
        switch2 = findViewById(R.id.sw2) as Switch?
        back = findViewById(R.id.backbutton) as Button?

        val settings: SharedPreferences = getSharedPreferences("save", MODE_PRIVATE)
        switch1!!.isChecked = settings.getBoolean("value1", true)

        switch1!!.setOnCheckedChangeListener { buttonView, isChecked ->

            if (switch1!!.isChecked) {
                val objIntent = Intent(this@Options, PlayAudio::class.java)
                startService(objIntent)

                val editor: SharedPreferences.Editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value1", true)
                editor.apply()
                switch1!!.isChecked = true

            } else {

                val objIntent = Intent(this@Options, PlayAudio::class.java)
                stopService(objIntent)
                val editor: SharedPreferences.Editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value1", false)
                editor.apply()
                switch1!!.isChecked = false
            }
        }
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val settings2 = getSharedPreferences("IDvalue", 0)

        switch2!!.isChecked = settings2.getBoolean("value", true)
        switch2!!.setOnCheckedChangeListener { buttonView, isChecked ->


            if (switch2!!.isChecked) {

                vibrator.vibrate(400)
                val editor: SharedPreferences.Editor = getSharedPreferences("IDvalue", MODE_PRIVATE).edit()
                editor.putBoolean("value", true)
                editor.apply()

                switch2!!.isChecked = true
            }
            else {
                vibrator.cancel()

                val editor: SharedPreferences.Editor = getSharedPreferences("IDvalue", MODE_PRIVATE).edit()
                editor.putBoolean("value", false)
                editor.apply()
                switch2!!.isChecked = false
            }
        }

        back!!.setOnClickListener {
            val prefs = getSharedPreferences("IDvalue", MODE_PRIVATE)
            val switchState: Boolean = prefs.getBoolean("value", true)

            if (switchState) {
                vibrator.vibrate(100)
            } else {
                vibrator.cancel()

            }
            val intent = Intent(getApplicationContext(), MainMenu::class.java)
            startActivity(intent)
        }
    }
}