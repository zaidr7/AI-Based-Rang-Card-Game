package FinalYearProject.BandRang.model

class Card(val suit: Suit, val value: Int) {

    //Initializing the cards
    val TWO = 2
    val THREE = 3
    val FOUR = 4
    val FIVE = 5
    val SIX = 6
    val SEVEN = 7
    val EIGHT = 8
    val NINE = 9
    val TEN = 10
    val JACK = 11
    val QUEEN = 12
    val KING = 13

//Giving the Min and Max Values of the cards.
    companion object {
        val MINVALUE = 2
        val MAXVALUE = 14
        val ACE = 14
    }

    lateinit var player: Player
// Joining Image with the Numbers.
    fun determineImageName(): String{
        var valName = ""
        when(this.value) {
            TWO -> valName = "two"
            THREE -> valName = "three"
            FOUR -> valName = "four"
            FIVE -> valName = "five"
            SIX -> valName = "six"
            SEVEN -> valName = "seven"
            EIGHT -> valName = "eight"
            NINE -> valName = "nine"
            TEN -> valName = "ten"
            JACK -> valName = "jack"
            QUEEN -> valName = "queen"
            KING -> valName = "king"
            ACE -> valName = "ace"
        }
// Determining the Suit Name
        var suitName = ""
        suitName = when(this.suit){
            Suit.CLUB -> "c"
            Suit.DIAMOND -> "d"
            Suit.HEART -> "h"
            Suit.SPADE -> "s"
        }

        val res = valName + suitName

        return res

    }

    override fun equals(obj: Any?): Boolean {  /////this method will check availability of card it will return Boolean true or false
        if (obj !is Card) {
            return false
        }
        val thatCard = obj as Card?
        return this.suit == thatCard?.suit && this.value == thatCard.value
    }
}