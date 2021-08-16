package FinalYearProject.BandRang.model

import android.os.SystemClock
import java.util.*
/////it will judge the player movement and using direction and events
class PlayerAI(name: String, direction: Direction, cards: MutableList<Card>): Player(name, cards) {
    override var direction: Direction = direction



    override fun play(table: MutableList<Card>, ///in play method it will initiate player to play
                      tableHistory: MutableList<MutableList<Card>>,
                      teams: MutableList<Team>,
                      rang: Suit): Card{
        if(!table.any()){// this player must play the first card for this round
/// if table have no value it will add first card
                val notRangs = hand.filter { it.suit != rang }
                return  if(notRangs.any()) notRangs.maxBy { it.value }!! else hand[0]

        }

        var sameSuitCards = hand.filter { it.suit == table[0].suit }
        ///it will add suit card if not available then will move form first card
        if(sameSuitCards.any()){// must play one of the same suit cards
            sameSuitCards = sameSuitCards.sortedWith(compareBy{it.value})
            if(table.size == 1){//if the table size is one it will add first card

                return sameSuitCards.first()// if this is the second player, return the lowest card
            }
            if(table.size == 2) { ///if the table size is 2  it add team and winner in game ad show suit card

                val ourTeam = this.team
                val winnerPlayer = Game.getTableWinner(table, rang, teams)
                if (winnerPlayer.team == ourTeam) {// we are this round's winner, so play card
                    val teamMateVal = table[0].value
                    if (teamMateVal == 12 || teamMateVal == 13 || teamMateVal == 14) {

                        return sameSuitCards.first()
                    }
                }
                return sameSuitCards.last()
            }
            if(table.size == 3) {

                val ourTeam = this.team
                val winnerPlayer = Game.getTableWinner(table, rang, teams)
                if(winnerPlayer.team == ourTeam){// we are this round's winner, so play the lowest card

                    return sameSuitCards.first()
                }
                else{

                    // if we can win with the highest card, play it, otherwise return the lowest card
                    val tableHighestCard = table.maxBy { it.value }

                    val highestValue = tableHighestCard?.value ?: 0
                    if(sameSuitCards.last().value > highestValue){

                        return sameSuitCards.last()
                    }
                    return sameSuitCards.first()
                }
            }
        }

///if table size is not two set suit card to recent
        else{// no same suit card

            if(table.size == 1){

                if(table[0].suit == rang){

                    return MinRang(rang)!!
                }
                else{

                    return MaxRang(rang)!!
                }
            }

            if(table.size == 2){

                if(table[0].suit == rang){
                // must play rang
                    return MinRang(rang)!!
                }
                else{

                    val ourTeam = this.team
                    val winnerPlayer = Game.getTableWinner(table, rang, teams)
                    if (winnerPlayer.team == ourTeam) {
                      // we are this round's winner, so play rang
                        val teamMateVal = table[0].value


                        return MaxRang(rang)!!
                    }
                    else{

                        val maxRangOnTable = table.filter { it.suit == rang }.maxBy { it.value }
                        val myMaxRang = hand.filter { it.suit == rang }.maxBy { it.value }

                        if(maxRangOnTable == null){

                            return MaxRang(rang)!!
                        }

                        if(myMaxRang != null && myMaxRang.value > maxRangOnTable.value){

                            return myMaxRang

                        }

                        return MaxRang(rang)!!
                    }
                }
            }
///if table first value is not greater than rang or not equal it will add team and will show winner for display
            if(table.size == 3){

                if(table[0].suit == rang){

                    return MinRang(rang)!!
                }
                else {

                    val ourTeam = this.team
                    val winnerPlayer = Game.getTableWinner(table, rang, teams)
                    if (winnerPlayer.team == ourTeam) {// we are this round's winner, so play same rang

                        return MaxRang(rang)!!
                    } else {

                        val maxRangOnTable = table.filter { it.suit == rang }.maxBy { it.value }
                        val myMaxRang = hand.filter { it.suit == rang }.maxBy { it.value }

                        if (maxRangOnTable == null) {

                            return MaxRang(rang)!!
                        }
                        else {
                            return MaxRang(rang)!!
                        }
                        if (myMaxRang != null && myMaxRang.value > maxRangOnTable!!.value) {

                            return myMaxRang
                        }

                        return MinRang(rang)!!

                    }
                }
            }
        }

        return hand[Random().nextInt(hand.size)]
    }

    private fun MinRang(rang: Suit): Card?{ ///this method will set minimum rang
        return hand.filter { it.suit != rang }.minBy { it.value }
    }

    private fun MaxRang(rang: Suit): Card?{ ///this method will set maximum rang
       val rangCards = hand.filter { it.suit == rang }
        if(rangCards.any()){
            return rangCards.maxBy { it.value }
        }
        return MinRang(rang)
    }


}