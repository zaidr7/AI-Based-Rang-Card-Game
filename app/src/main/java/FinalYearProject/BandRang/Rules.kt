package FinalYearProject.BandRang

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import FinalYearProject.BandRang.R

class Rules : AppCompatActivity() {

    var RulesTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        RulesTextView = findViewById(R.id.RulesTextView) as TextView
        val para = """
            1. The player to dealer's right leads any card to the first trick. Players must follow suit if possible: if unable they may play any card. 

            2. When all four players have contributed a card the player of the highest card of the suit that was led wins the trick unless one or more cards of the trump suit were played, in which case the highest trump wins. 

            3. The player who won the trick leads any card to the next trick.

            4. Completed tricks are stacked neatly face down in front of one of the players of the team who won them, so that everyone can see how many tricks each team has won.

            5. A player who revokes by failing to follow suit when able to may apologise and correct the error without penalty, provided that this is done before the trick (hand) has been completed and turned face down.

            6. After the revoke is corrected, any players who played after the incorrect play have the option, in turn, to take back the card they played and play a different one. 

            7. If a revoke is detected after the trick is complete (for example a player plays a different suit on a heart lead and later plays a heart), then the play ends and the opposing team immediately scores a court.

           
    """.trimIndent()
        RulesTextView!!.text = para
        RulesTextView!!.movementMethod = ScrollingMovementMethod()
    }
}