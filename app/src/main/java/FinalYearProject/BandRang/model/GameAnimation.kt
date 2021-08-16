package FinalYearProject.BandRang.model

interface GameAnimation{
    fun oneCardDealtForDeterminingRangMaker(card: Card, direction: Direction)
    fun RangMakerDetermined()

    fun cardsDealt(numCards: Int, rangMaker: Direction, hands: Array<MutableList<Card>>, shouldDetermineRang: Boolean, rang: Suit?=null)
    fun cardPlayed(card: Card, direction: Direction)
    fun tableComplete(winnerDirection: Direction?, winnerScore: Int, tableCards: List<Card>, isGameOver: Boolean, setOver: Boolean)
}