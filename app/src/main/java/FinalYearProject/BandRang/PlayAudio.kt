package FinalYearProject.BandRang

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log


class PlayAudio : Service() {

    var objPlayer: MediaPlayer? = null
    override fun onCreate() {
        super.onCreate()
        Log.d(LOGCAT, "Service Started!")  //// initializing to media player audio
        objPlayer = MediaPlayer.create(this, R.raw.music)
        objPlayer!!.isLooping = true
    }
////media player start playing
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        objPlayer!!.start()
        Log.d(LOGCAT, "Media Player started!")
        if (objPlayer!!.isLooping != true) {
            Log.d(LOGCAT, "Problem in Playing Audio")
        }
        return 1
    }

    fun onStop() {
        objPlayer!!.stop()
        objPlayer!!.release()
    }

    fun onPause() {
        objPlayer!!.stop()
        objPlayer!!.release()
    }

    override fun onDestroy() {
        objPlayer!!.stop()
        objPlayer!!.release()
    }

    override fun onBind(objIndent: Intent): IBinder? {
        return null
    }

    companion object {
        private val LOGCAT: String? = null
    }
}