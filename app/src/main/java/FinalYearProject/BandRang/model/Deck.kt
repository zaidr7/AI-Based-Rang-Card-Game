package FinalYearProject.BandRang.model

class Deck {

    //Initializing the Deck.
    var deck: MutableList<Card> = mutableListOf()
    init{
        for(suit in Suit.values()) {
            for (value in Card.MINVALUE .. Card.MAXVALUE) {
                deck.add(Card(suit, value))
            }
        }

        deck.shuffle()  //////it will shuffle cards in swap
    }

    fun getTopCard(): Card{ ////it will get most top listed card value
        val topCard = deck.last()
        deck.removeAt(deck.lastIndex)
        return topCard
    }

}