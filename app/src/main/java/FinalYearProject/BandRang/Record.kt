package FinalYearProject.BandRang


import FinalYearProject.BandRang.util.PrefsHelper
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class Record : AppCompatActivity() {

    var backbutton: Button?=null

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        var Win: TextView? = null
        var Lose: TextView? = null
        val Record = PrefsHelper.KEY_TOTAL_SCORE
        lateinit var prefs: SharedPreferences

            Win = findViewById(R.id.win2) as TextView?
            Lose = findViewById(R.id.lose2) as TextView?
        backbutton = findViewById(R.id.backbutton1) as Button?
            prefs = getSharedPreferences( "FinalYearProject.BandRang.util.txt", Context.MODE_PRIVATE)
            val Score = prefs.getInt("HUMAN", 0)
            val AiScore = prefs.getInt("Ai", 0)

            Win!!.text = Score.toString()
            Lose!!.text = AiScore.toString()
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator




        backbutton!!.setOnClickListener {

            val prefs1 = getSharedPreferences("IDvalue", MODE_PRIVATE)
            val switchState: Boolean = prefs1.getBoolean("value", true)

            if (switchState) {
                vibrator.vibrate(100)
            } else {
                vibrator.cancel()

            }

            val intent = Intent(applicationContext, MainMenu::class.java)
            startActivity(intent)
        }

    }
}

