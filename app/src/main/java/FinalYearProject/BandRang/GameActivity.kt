package FinalYearProject.BandRang

import FinalYearProject.BandRang.model.*
import FinalYearProject.BandRang.util.PrefsHelper
import FinalYearProject.BandRang.view.CardImageView
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity(), GameAnimation {

    var cardWidth: Int = 0
    var cardHeight: Int = 0

    var hands: Array<MutableList<CardImageView>> = Array(4){ mutableListOf<CardImageView>() }

    var lastCardIndexDealt = 52

    override fun tableComplete(winnerDirection: Direction?, winnerScore: Int, table: List<Card>, isGameOver: Boolean, setOver: Boolean) {
        ////table width height calculation
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels

        val offsetX = 150f
        val marginX = 100f
        val marginY = 100f


        var anSet = AnimatorSet()
        var animList = mutableListOf<Animator>()

        table.forEach {
            var img = findCardImage(it)
            var x = 0f
            var y = 0f
            when(winnerDirection){
                Direction.BOTTOM -> {
                    x = offsetX + (winnerScore - 1) * marginX
                    y = screenHeight - (3 * cardHeight.toFloat())
                }////screen bottom side animation control
                Direction.RIGHT -> {
                    x = screenWidth - (cardWidth.toFloat() + (cardWidth / 2))
                    y = 2 * cardHeight.toFloat() + ((winnerScore - 1) * marginY)
                }////right side animation movement
            }
            val animator = ObjectAnimator.ofFloat(img, "x", x)
            val animator2 = ObjectAnimator.ofFloat(img, "y", y)


            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {

                }

                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {
                    if (img.direction == Direction.RIGHT || img.direction == Direction.LEFT) {
                        img.rotation = 0f
                    }
                    img.setScoreImageSource()
                }

            })

            animList.add(animator)
            animList.add(animator2)


        }


        anSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                if (isGameOver) {

                    Toast.makeText(this@GameActivity, "This game has ended.", Toast.LENGTH_LONG).show()

                    btn_new_game.visibility = View.VISIBLE

                    btn_new_game.setOnClickListener {
                        it.visibility = View.GONE

                        hands.forEach { it.clear() }
                        lastCardIndexDealt = 52

                        ResetCardsInMiddle(setOver)
                        if (setOver) {

                            game = Game(this@GameActivity)
                            game.determineRangMaker()

                        } else {

                            game.newGame()
                            game.dealCards(5)
                        }
                    }
                } else {
                    game.playCard()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}

        })
        anSet.duration = 500
        anSet.startDelay = 1000
        anSet.playTogether(animList)
        anSet.start()
    }
    fun findCardImage(card: Card): CardImageView{
        return allCardsImages.first { it.card == card  }
    }


    override fun cardsDealt(numCards: Int, rangMaker: Direction, hands: Array<MutableList<Card>>, shouldDetermineRang: Boolean, rang: Suit?) {
        dealCards(numCards, rangMaker, hands, shouldDetermineRang, rang)
    }

    fun dealCards(numCards: Int, initialDirection: Direction, nextCards: Array<MutableList<Card>>, shouldDetermineRang: Boolean, rang: Suit? = null){
        var RangMakerDealingAnimations = mutableListOf<Animator>()
        var direction = initialDirection
        //////loop to change card value and image auto
        for(i in 0 .. 3) {
            var cards = getNextCards(nextCards[direction.value], numCards, direction)
            if(direction == Direction.BOTTOM){
                for(cardImage in cards) {
                    addClickListener(cardImage)
                }
            }
            ///////moved cards adding in array list for sort
            hands[i].addAll(cards)
            hands[i] = sortCards(hands[i]).toMutableList()

            val delta = getOffset(cards[0], direction)///cards moved record
            val property = getProperty(direction)

            var rotationAnim = ObjectAnimator()
            var animator: Animator = ObjectAnimator()
            animator.duration = 200

            for((index, card) in cards.withIndex()) {
                animator = ObjectAnimator.ofFloat(card, property, delta)

                var myDir = direction //needed, otherwise directionRangMakerDetermination doesn't change
                animator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onAnimationEnd(animation: Animator?) {
                        if (soundPlayer.isPlaying) {
                            //soundPlayer.pause()
                           // soundPlayer.seekTo(0)
                        }
                        if (index == cards.size - 1) {
                            formHand(hands[i], myDir)// myDir)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {
                        //soundPlayer.start()
                    }

                })
                RangMakerDealingAnimations.add(animator)
            }



            direction = game.getNextDirection(direction)
        }
        var set = AnimatorSet()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                if (rang != null) {
                    setRangImage(rang)
                }
                if (shouldDetermineRang) {
                    showRangDialog()
                } else if (lastCardIndexDealt > 0) {
                    game.dealCards(4)//dealCards(4, initialDirection)
                } else {
                    game.playCard()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}

        })
        set.playSequentially(RangMakerDealingAnimations)///animation in the specific sequence
        set.start()//animation start
    }

    private fun setRangImage(rang: Suit) {
        var rangIcon = when(rang){
            Suit.CLUB -> "clubs1"
            Suit.DIAMOND -> "diamonds1"
            Suit.HEART -> "hearts1"
            Suit.SPADE -> "spades1"
        }
        ////images setup to show
        val identifier = resources.getIdentifier(rangIcon,
                "drawable",
                "FinalYearProject.BandRang")
        Picasso.get().load(identifier).into(img_hokmt)
        Picasso.get().load(identifier).into(img_hokmm)
        Picasso.get().load(identifier).into(img_hokmb)
    }
        //to control width and height of images of hand
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun formHand(cards: List<CardImageView>, direction: Direction, doRotation: Boolean = true){

        if(cardHeight == 0 || cardWidth == 0) {
            cards[0].measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            cardHeight = cards[0].measuredHeight
            cardWidth = cards[0].measuredWidth
        }

        val fullWidth = resources.displayMetrics.widthPixels
        val fullHeight = resources.displayMetrics.heightPixels


    /////movement for bottom value
        if(direction == Direction.BOTTOM) {
            val width = fullWidth/cards.size
            for ((i, card) in cards.withIndex()) {
                card.setImageSource()
                card.x = (i * width).toFloat()
                card.translationZ = i.toFloat()
            }
        }

        else{ //card animation in the specific direction
            for((i, card) in cards.withIndex()){
                card.translationZ = i.toFloat()

                when(direction){
                    Direction.LEFT -> {
                        if (doRotation) {
                            card.rotation = 90f
                        }
                        card.x = (-cardWidth / 6).toFloat()
                        card.y = 320 + (i * (cardWidth / 6)).toFloat()
                    }
                    Direction.RIGHT -> {
                        if (doRotation) {
                            card.rotation = 90f
                        }

                        card.x = fullWidth - (cardWidth / 6).toFloat()
                        card.y = 320 + (i * (cardWidth / 6)).toFloat()
                    }
                    Direction.TOP -> {
                        val width = fullWidth / cards.size
                        card.x = (i * width).toFloat()
                        card.y = -(cardHeight / 6).toFloat()
                    }
                }

            }
        }
    }

    ////method to move next card
    private fun getNextCards(cards: List<Card>, numCards: Int, direction: Direction): List<CardImageView> {
        var nextFiveCards: List<CardImageView> = allCardsImages.filter { cards.contains(it.card) } //allCardsImages.subList(lastCardIndexDealt - numCards, lastCardIndexDealt)
        nextFiveCards.forEach { it.direction = direction }
        nextFiveCards.forEach { it.card.player = cards[0].player }
        var sortedCards = sortCards(nextFiveCards)
        lastCardIndexDealt -= numCards
        return sortedCards
    }
    ////to sort cards this method will call
    private fun sortCards(cards: List<CardImageView>): List<CardImageView>{
        return cards.sortedWith(compareBy({ it.card.suit }, { it.card.value }))
    }
    ////images list of all cards
    var allCardsImages = mutableListOf<CardImageView>()


    lateinit var soundPlayer: MediaPlayer

    private var game =  Game(this)

    private var rangMakerDetermination = RangMakerDetermination()

    ////define rang making method
    override fun oneCardDealtForDeterminingRangMaker(card: Card, direction: Direction) {

        val cardImage = rangMakerDetermination.getTopCardImage()

        ///specify direction
        val delta = getOffset(cardImage, direction)
        val property = getProperty(direction)

        val animator = ObjectAnimator.ofFloat(cardImage, property, delta)

        var j = 0

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                if (soundPlayer.isPlaying) {
                    // soundPlayer.pause()
                   // soundPlayer.seekTo(0)
                }
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

                soundPlayer.start()
                cardImage.setImageSource()
            }

        })
        animator.duration = 200
        rangMakerDetermination.rangMakerDealingAnimations.add(animator)

        rangMakerDetermination.cardZIndex++
        cardImage.translationZ = rangMakerDetermination.cardZIndex//assigning animation to the image

    }

    private fun getOffset(cardImage: CardImageView, direction: Direction): Float{
        cardImage.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        cardHeight = cardImage.measuredHeight
        cardWidth = cardImage.measuredWidth


        val displayMetrics = resources.displayMetrics
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val halfScreenWidth = (displayMetrics.widthPixels / 2).toFloat() /// geting width of screen and making it half
        var halfScreenHeight = (displayMetrics.heightPixels / 2).toFloat()// geting height of screen and makng it half

        val verticalOffset = halfScreenHeight - cardHeight
        val horizontalOffset = halfScreenWidth - cardWidth

        return when (direction) {
            Direction.BOTTOM -> verticalOffset
            Direction.TOP -> -verticalOffset
            Direction.RIGHT -> horizontalOffset
            Direction.LEFT -> -horizontalOffset
        }
    }
//getting direction of the animation
    private fun getProperty(direction: Direction): String{
        return when (direction) {
            Direction.BOTTOM, Direction.TOP -> "translationY"
            Direction.LEFT, Direction.RIGHT -> "translationX"
        }
    }
//fixing rang of the animation
    override fun RangMakerDetermined() {
        var set = AnimatorSet()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                ResetCardsInMiddle(false)
                game.dealCards(5)

            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })

        set.playSequentially(rangMakerDetermination.rangMakerDealingAnimations)
        set.start()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)



///initializing media player objects
        initMediaPlayer()
/// determination initialization
        init(true)

        game.determineRangMaker()

    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
    }

    fun init(isRangMakerDetermination: Boolean = false){


        val scores = PrefsHelper.getCurrentScore()


        for(card: Card in game.deck.deck){
            var img  = CardImageView(card, this)
            root.addView(img)
            allCardsImages.add(img)
            if(isRangMakerDetermination) {
                rangMakerDetermination.cardsImages.add(img)
            }
            else{

            }
        }
    }
/// already played cards detail
    override fun cardPlayed(card: Card, direction: Direction) {
        val cardImg = findCardImage(card)
        playCardForwardAnim(cardImg, card)
    }
////forward animation of cards
    private fun playCardForwardAnim(img: CardImageView, card: Card){
        if(img.direction == Direction.BOTTOM){
            removeClickListener(img)
        }
        img.setImageSource()
        val displayMetrics = resources.displayMetrics
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val centerX = (displayMetrics.widthPixels / 2).toFloat()
        var centerY = (displayMetrics.heightPixels / 2).toFloat()

        var animSet = AnimatorSet()

/// drawing animation according to screen width height
        val cardWidth = img.measuredWidth
        val cardHeight = img.measuredHeight
        val halfHeight = cardHeight / 2
        val halfWidth = cardWidth / 2

        var x = 0f
        var y = 0f
        when(img.direction){
            Direction.BOTTOM -> {
                x = centerX - halfWidth
                y = centerY - halfHeight
            }
            Direction.TOP -> {
                x = centerX - halfWidth
                y = centerY - cardHeight - halfHeight
            }
            Direction.RIGHT -> {
                x = centerX
                y = centerY - cardHeight
            }

            Direction.LEFT -> {
                x = centerX - cardWidth
                y = centerY - cardHeight
            }
        }
        val animator = ObjectAnimator.ofFloat(img, "x", x)
        val animator2 = ObjectAnimator.ofFloat(img, "y", y)
        animSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                if (soundPlayer.isPlaying) {
                    soundPlayer.pause()
                    soundPlayer.seekTo(0)
                }
                hands[img.direction.value].remove(img)

                game.cardPlayed(card, img.direction)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                soundPlayer.start()
            }

        })
        animSet.playTogether(animator, animator2)
        animSet.duration = 500
        animSet.start()



    }
    fun initMediaPlayer(){
        soundPlayer = MediaPlayer.create(this, R.raw.cardslide7)

    }
    fun ResetCardsInMiddle(isRangMakerDetermination: Boolean) {
        clearAllCardsImages()
        init(isRangMakerDetermination)
    }
///clear all images from memory
    private fun clearAllCardsImages(){
        for(image in allCardsImages){
            root.removeView(image)
        }
        allCardsImages.clear()
    }

    private fun setListenerForCard(card: CardImageView){
        card.setOnClickListener({

        })
    }

    private fun showRangDialog(){
        val singleChoiceItems = resources.getStringArray(R.array.dialog_rang)
        var itemSelected = 0
        var rang = Suit.HEART
        AlertDialog.Builder(this)
                .setTitle("Select Rang")
                .setSingleChoiceItems(singleChoiceItems, itemSelected,
                        DialogInterface.OnClickListener { dialogInterface, selectedIndex ->
                            when (selectedIndex) {
                                0 -> rang = Suit.HEART
                                1 -> rang = Suit.SPADE
                                2 -> rang = Suit.DIAMOND
                                3 -> rang = Suit.CLUB
                            }

                        })
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    setRangImage(rang)
                    game.humanSpecifiedRang(rang)
                })

                .show()
    }

    private fun addClickListener(cardImage: CardImageView){
        cardImage.setOnClickListener {
            if (game.isValidCard(cardImage.card)) {
                playCardForwardAnim(it as CardImageView, cardImage.card)
            } else {
                Toast.makeText(GameActivity@ this, "not valid", Toast.LENGTH_SHORT).show()
            }
        }
    }
///removing listeners
    private fun removeClickListener(cardImage: CardImageView){
        cardImage.setOnClickListener(null)
    }






}
