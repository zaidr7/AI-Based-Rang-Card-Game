package FinalYearProject.BandRang.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences



object PrefsHelper {

    private const val KEY_BACKGROUND_ICON = "background_icon"
    private const val KEY_CARD_BACK_ICON = "card_back_icon"
    const val KEY_TOTAL_SCORE = "totalScore"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        if (context !is Application) {
            throw IllegalArgumentException("This should be the application context. Not the activity context")
        }
        prefs = context.getSharedPreferences( "FinalYearProject.BandRang.util.txt", Context.MODE_PRIVATE);
    }

    fun saveBackgroundIcon(iconName: String) {
        prefs.edit()
                .putString(KEY_BACKGROUND_ICON, iconName)
                .apply()
    }

    fun getCardBackIcon(): String {
        return prefs.getString(KEY_CARD_BACK_ICON, "card_back_black")
    }

    fun saveCardBackIcon(iconName: String) {
        prefs.edit()


                .putString(KEY_CARD_BACK_ICON, iconName)
                .apply()
    }

    fun getBackgroundIcon(): String {
        return prefs.getString(KEY_BACKGROUND_ICON, "background")
    }

    fun saveTotalScore(humanWon: Boolean , s: String ){ ////method is used to save the total scores

        val scores = getScores()
        var scoreHuman = scores.first
        var scoreAI = scores.second
        if(humanWon){
            scoreHuman++
            // human Score++ ;

            setHummanPlayer("")

        }
        else{


            scoreAI++
            setAiPlayer(s)
        }

        //
        val score = scoreHuman.toString() + "-" + scoreAI.toString()
        val totalScore = getTotalScore()
        if(shouldUpdateScore(totalScore)){
            totalScore.minus(totalScore.last())
        }

        val newScore = totalScore.plus(score)

        prefs.edit()
                .putStringSet(KEY_TOTAL_SCORE, newScore)
                .apply()

        // here is score save i have to get it//

    }

    fun getTotalScore(): Set<String> { /// this method is used to get the total scores
        return prefs.getStringSet(KEY_TOTAL_SCORE, setOf())
    }

    fun getCurrentScore(): Pair<Int, Int>{///this method is used to get current scores
        return getScores()//TODO
    }

    private fun shouldUpdateScore(scores: Set<String>): Boolean{  ////this method is used to update scores
        if(scores.isEmpty()){
            return false
        }
        val lastScore = scores.last()
        val parts = lastScore.split('-')
        val humanScore = parts[0].toInt()
        val aiScore = parts[1].toInt()
        if(humanScore != 7 && aiScore != 7){
            return true
        }
        return false
    }

    private fun getScores():Pair<Int,Int>{ //// used for getting score
        if(getTotalScore().isEmpty()){
            return Pair(0,0)
        }
        val lastScore = getTotalScore().last()
        val parts = lastScore.split('-')
        val humanScore = parts[0].toInt()
        val aiScore = parts[1].toInt()
        return Pair(humanScore, aiScore)
    }

    fun setHummanPlayer(s: String) {
        var  i =  0 ;

        var v = prefs.getInt("HUMAN" , 0);

        i++;
        v = v + i ;
        prefs.edit()
                .putInt("HUMAN", v)
                .apply();
    }

    fun setAiPlayer(s: String) {

        if(s.equals("top")){
            return;
        }

        var  i =  0 ;
        var v = prefs.getInt("Ai" , 0);


        i++;

        v = v + i ;

        prefs.edit()
                .putInt("Ai", Integer.valueOf(v))
                .apply();
    }
}