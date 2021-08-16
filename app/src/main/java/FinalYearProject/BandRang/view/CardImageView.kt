package FinalYearProject.BandRang.view

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.widget.RelativeLayout
import FinalYearProject.BandRang.R
import FinalYearProject.BandRang.model.Card
import FinalYearProject.BandRang.model.Direction
import FinalYearProject.BandRang.util.PrefsHelper
import com.squareup.picasso.Picasso

class CardImageView : AppCompatImageView {

    var card: Card
    var direction: Direction = Direction.TOP

    constructor(card: Card, context: Context?) : super(context) {
        this.card = card
        setLayoutParams()
    }

    private fun setLayoutParams(){
     var params =
             RelativeLayout.LayoutParams(
                     RelativeLayout.LayoutParams.WRAP_CONTENT,
                     RelativeLayout.LayoutParams.WRAP_CONTENT)
     params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
     this.layoutParams = params
     setCardBackIcon()
 }



    fun setImageSource(){
        val identifier = resources.getIdentifier(card.determineImageName(),
                "drawable",
                "FinalYearProject.BandRang")
        Picasso.get().load(identifier).into(this)
    }

    fun setScoreImageSource(){
        Picasso.get().load(R.drawable.card_score).into(this)
    }

    private fun setCardBackIcon(){
        val identifier = resources.getIdentifier(PrefsHelper.getCardBackIcon(),
                "drawable",
                "FinalYearProject.BandRang")
        Picasso.get().load(identifier).into(this)
    }
}