package FinalYearProject.BandRang

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler()
        handler!!.postDelayed(Runnable {

            val intent = Intent(this@SplashActivity, MainMenu::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}