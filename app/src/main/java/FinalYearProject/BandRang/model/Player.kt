package FinalYearProject.BandRang.model

abstract class  Player {
    val name: String
    val hand: MutableList<Card> = mutableListOf()
    lateinit var team: Team

    internal abstract var direction: Direction


    constructor(name: String, hand: MutableList<Card>) {
        this.name = name

        addHand(hand)
    }

     fun addHand(newHand: MutableList<Card>) {
         hand.clear()

         hand.addAll(newHand)
    }

    abstract fun play(table: MutableList<Card>,///play method will start the playing
                      tableHistory: MutableList<MutableList<Card>>,
                      teams: MutableList<Team>,
                      rang: Suit): Card
    fun determineRang(): Suit { ////it will set rang
        var groupWithCounts = hand.groupingBy { it.suit }.eachCount()///add groups of hands count or add group of total count
       var sortedHandByCount = groupWithCounts.toList().sortedByDescending { (key, value) -> value }

       if(sortedHandByCount[0].second >= 3){ ///if counted hands are greater than  than add sorted hands in list
           return sortedHandByCount[0].first
       }
        if(sortedHandByCount[0].second > sortedHandByCount[1].second){///if sorted hand first is greater than second it will swap to first
            return sortedHandByCount[0].first
        }
        else{ ///if not greater than add values of cards in first and second  place
             val first= hand.filter { it.suit == sortedHandByCount[0].first }.maxBy { it.value }
            val second = hand.filter { it.suit == sortedHandByCount[1].first }.maxBy { it.value }
            return if(first?.value!! > second?.value!!){
                sortedHandByCount[0].first
            } else{
                sortedHandByCount[1].first
            }
        }
    }


}