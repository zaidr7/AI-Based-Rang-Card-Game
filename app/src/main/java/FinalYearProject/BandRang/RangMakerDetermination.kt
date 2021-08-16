package FinalYearProject.BandRang

import FinalYearProject.BandRang.view.CardImageView
import android.animation.Animator

class RangMakerDetermination(var cardsImages: MutableList<CardImageView> = mutableListOf(),
                             var rangMakerDealingAnimations: MutableList<Animator> = mutableListOf(),
                             var cardZIndex: Float = 1f){



    fun getTopCardImage(): CardImageView {
        val topCard = cardsImages.last()
        cardsImages.removeAt(cardsImages.lastIndex)
        return topCard
    }

}