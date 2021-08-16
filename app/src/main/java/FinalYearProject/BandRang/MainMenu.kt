package FinalYearProject.BandRang

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.widget.Button


class MainMenu : AppCompatActivity() {

    var StartGameButton: Button? = null
    var RulesButton: Button? = null
    var OptionsButton: Button? = null
    var RecordButton: Button? = null
    var QuitButton: Button? = null
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)



        StartGameButton = findViewById(R.id.button1) as Button
        RulesButton = findViewById(R.id.button2) as Button
        OptionsButton = findViewById(R.id.button3) as Button
        RecordButton = findViewById(R.id.button4) as Button
        QuitButton = findViewById(R.id.button5) as Button



        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        RulesButton!!.setOnClickListener {
            val prefs = getSharedPreferences("IDvalue", MODE_PRIVATE)
            val switchState: Boolean = prefs.getBoolean("value", true)

            if (switchState) {
                vibrator.vibrate(100)
            } else {
                vibrator.cancel()

            }
            val intent = Intent(applicationContext, Rules::class.java)
            startActivity(intent)
        }


        OptionsButton!!.setOnClickListener {
            val prefs = getSharedPreferences("IDvalue", MODE_PRIVATE)
            val switchState: Boolean = prefs.getBoolean("value", true)

            if (switchState) {
                vibrator.vibrate(100)
            } else {
                vibrator.cancel()

            }
            val intent = Intent(applicationContext, Options::class.java)
            startActivity(intent)
        }


        StartGameButton!!.setOnClickListener {
            val prefs = getSharedPreferences("IDvalue", MODE_PRIVATE)
            val switchState: Boolean = prefs.getBoolean("value", true)

            if (switchState) {
                vibrator.vibrate(100)
            } else {
                vibrator.cancel()

            }
            val intent = Intent(applicationContext, GameActivity::class.java)
            startActivity(intent)
        }


        RecordButton!!.setOnClickListener {
            val prefs = getSharedPreferences("IDvalue", MODE_PRIVATE)
            val switchState: Boolean = prefs.getBoolean("value", true)

            if (switchState) {
                vibrator.vibrate(100)
            } else {
                vibrator.cancel()

            }
            val intent = Intent(applicationContext, Record::class.java)
            startActivity(intent)

        }


        QuitButton!!.setOnClickListener {
            val prefs = getSharedPreferences("IDvalue", MODE_PRIVATE)
            val switchState: Boolean = prefs.getBoolean("value", true)

            if (switchState) {
                vibrator.vibrate(100)
            } else {
                vibrator.cancel()

            }
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
        }
    }
}