package FinalYearProject.BandRang.model

class PlayerHuman(name: String, cards: MutableList<Card>): Player(name, cards) {
    override fun play(table: MutableList<Card>,
                      tableHistory: MutableList<MutableList<Card>>,
                      teams: MutableList<Team>,
                      rang: Suit): Card {
        TODO("not implemented")
    }

    override var direction: Direction = Direction.BOTTOM ///show direction of bottom
}