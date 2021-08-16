package FinalYearProject.BandRang.model


import FinalYearProject.BandRang.util.PrefsHelper

import android.util.Log



class  Game(val animation: GameAnimation) {



    lateinit var state: Any
    var deck = Deck()
    lateinit var RangMaker: Direction
    lateinit var tableDir: Direction
    lateinit var Rang: Suit
    var directionRangMakerDetermination: Direction = Direction.BOTTOM
    var hands: Array<MutableList<Card>> = Array(4){ mutableListOf<Card>() }
    var players: MutableList<Player> = mutableListOf()
    var teams: MutableList<Team> = mutableListOf()
    var table: MutableList<Card> = mutableListOf()
    var tableHistory: MutableList<MutableList<Card>> = mutableListOf()
    var lastIndex = deck.deck.lastIndex+1

    fun newGame(){/// when starting the new game it will clear the all memory
        this.hands.forEach { it.clear() }
        this.players.clear()
        this.teams.clear()
        this.table.clear()
        this.tableHistory.clear()
        lastIndex = deck.deck.lastIndex+1
        getNewDeck()
    }

    companion object {
        ///it will make the table for all winners and their records
        fun getTableWinner(table: MutableList<Card>, rang: Suit, teams: MutableList<Team>): Player {
            val sameSuit =  table.all { it.suit == table[0].suit } // if all are of the same suit
            if(sameSuit) {
                return getWinner(table, teams)
            }
            else{
                val rangCards = table.filter { it.suit == rang}
                if(rangCards.any()){
                    return getWinner(rangCards, teams)
                }
                else{
                    // high card with the same suit as the first card wins.
                    var sameSuitAsFirstCard = table.filter { it.suit == table[0].suit }
                    return getWinner(sameSuitAsFirstCard, teams)
                }
            }
        }
///// get the winners list
        private fun getWinner(cards: List<Card>, teams: MutableList<Team>): Player{
            val winnerCard = cards.maxBy { it.value }
            val winnerPlayer = winnerCard?.player!!
            Log.e("WINNER ", winnerPlayer.name)
            return winnerPlayer
        }




    }

    fun determineRangMaker(){
        var j = 0
        var card: Card = deck.getTopCard()
        animation.oneCardDealtForDeterminingRangMaker(card, directionRangMakerDetermination)


        while (card.value != Card.ACE){
            j++
            card = deck.getTopCard()
            Log.i("CARD", "card " + card.value + " " + card.suit + " " + j)
            directionRangMakerDetermination = getNextDirection(directionRangMakerDetermination)
            animation.oneCardDealtForDeterminingRangMaker(card, directionRangMakerDetermination)
        }
        RangMaker = directionRangMakerDetermination
        tableDir = directionRangMakerDetermination
        animation.RangMakerDetermined()
        getNewDeck()
        Log.i("ACE", "card ace" + card.value + " " + card.suit + " " + j)
    }

    fun getNextDirection(direction: Direction): Direction{//// direction handling method on four sides movement
        return when(direction){
            Direction.BOTTOM -> Direction.RIGHT
            Direction.RIGHT -> Direction.TOP
            Direction.TOP -> Direction.LEFT
            Direction.LEFT -> Direction.BOTTOM
        }
    }

    fun isValidCard(card: Card): Boolean{ ////is card value true it will return true for last card
        if(tableDir != Direction.BOTTOM){
            return false
        }
        if(table.size == 0){
            return true
        }

        val hasSameSuit = players[Direction.BOTTOM.value].hand.any { it.suit == table[0].suit }
        if(!hasSameSuit){
            return true
        }

        if(table[0].suit == card.suit){
            return true
        }
        return false
    }


    fun dealCards(numCards: Int){

        var nextCards: Array<MutableList<Card>> = Array(4){ mutableListOf<Card>() } /// list of upcoming cards

        var dir = RangMaker
        for(i in 0 .. 3){
            nextCards[dir.value].addAll(deck.deck.subList(lastIndex - numCards, lastIndex))
            hands[dir.value].addAll(deck.deck.subList(lastIndex - numCards, lastIndex))
            lastIndex -= numCards
            dir = getNextDirection(dir)
        }

        val isHumanRangMaker = numCards == 5 && RangMaker == Direction.BOTTOM

        var rang : Suit? = null
        if(numCards == 5){
            var humanPlayer = PlayerHuman("Zaid", hands[Direction.BOTTOM.value])
            var rightAI = PlayerAI("right", Direction.RIGHT, hands[Direction.RIGHT.value])
            var topAI = PlayerAI("top", Direction.TOP, hands[Direction.TOP.value])
            var leftAI = PlayerAI("left", Direction.LEFT, hands[Direction.LEFT.value])

            players.add(Direction.BOTTOM.value, humanPlayer)/// it will add players direction to according to movement
            players.add(Direction.RIGHT.value, rightAI)
            players.add(Direction.TOP.value, topAI)
            players.add(Direction.LEFT.value, leftAI)

            teams.add(0, Team(players[Direction.BOTTOM.value], players[Direction.TOP.value]))/// it will add team direction to according to movement
            teams.add(1, Team(players[Direction.RIGHT.value], players[Direction.LEFT.value]))

            players[Direction.BOTTOM.value].team = teams[0]
            players[Direction.TOP.value].team = teams[0]
            players[Direction.RIGHT.value].team = teams[1]
            players[Direction.LEFT.value].team = teams[1]

            if(!isHumanRangMaker){
                rang = players[RangMaker.value].determineRang()
                this.Rang = rang
            }

        }
        else { ////if rang is not true add rang other wise add player direction
            players[Direction.BOTTOM.value].addHand(hands[Direction.BOTTOM.value])
            players[Direction.RIGHT.value].addHand(hands[Direction.RIGHT.value])
            players[Direction.TOP.value].addHand(hands[Direction.TOP.value])
            players[Direction.LEFT.value].addHand(hands[Direction.LEFT.value])
        }

        addPlayerToCard(hands[dir.value], players[dir.value])


        animation.cardsDealt(numCards, RangMaker, nextCards, isHumanRangMaker, rang)
    }
////method will add player to card who is on position
    private fun addPlayerToCard(cards: MutableList<Card>, player: Player) {
        hands.forEachIndexed { index, hand -> hand.forEach { it.player = players[index] }}

    }

    fun playCard(){ ////play card method will initiate play position of card
        var direction = tableDir
        if(direction == Direction.BOTTOM)
            return

        val cardToPlay = players[direction.value].play(table, tableHistory, teams, Rang)
        animation.cardPlayed(cardToPlay, direction)
    }

    private fun getNewDeck(){
        deck = Deck()
    }

    fun humanSpecifiedRang(rang: Suit){
        this.Rang = rang
        dealCards(4)
    }





    fun cardPlayed(card: Card, direction: Direction){  ///in table array it will add played card
        players[direction.value].hand.remove(card)
        table.add(card)
        tableDir = getNextDirection(direction)

        if(table.size == 4){  ///if table size is 4 it will declare winner
            val winnerPlayer = getTableWinner(table, Rang, teams)

            val winnerTeam = winnerPlayer.team
            winnerTeam.score++

            tableDir = winnerPlayer.direction ////in table directory it will get winner value and will show
            PrefsHelper.saveTotalScore(winnerTeam.team1 is PlayerHuman , winnerPlayer.name)
            val isGameOver = winnerTeam.score == 7
            var wholeSetOver = false
            if(isGameOver){ /// if game is over it will show winner
                if(winnerTeam != players[RangMaker.value].team){
                    RangMaker = getNextDirection(RangMaker)
                    tableDir = RangMaker
                }
            }
////it will animate game ending and table winner will show
            animation.tableComplete(winnerTeam.team1?.direction, winnerTeam.score, table, isGameOver, wholeSetOver)
            tableHistory.add(table)
            table.clear()
        }
        else{   ///if not over it will play card
            playCard()
        }

    }


    fun getwinnerVa(cards: List<Card>, teams: MutableList<Team>) : Player{ //it will return winner
        val winnerCard = cards.maxBy { it.value }
        val winnerPlayer = winnerCard?.player!!
        Log.e("WINNER ", winnerPlayer.name)
        return winnerPlayer
    }


}





