package com.spider.composes.model.engine

import com.spider.composes.model.Card
import com.spider.composes.model.engine.SpiderSolitaire.State.DrawCardsEvent
import com.spider.composes.model.engine.SpiderSolitaire.State.MoveEvent
import com.spider.composes.model.engine.SpiderSolitaire.State.MoveOutEvent
import com.spider.composes.model.engine.SpiderSolitaire.State.UpdateOpenIndexEvent
import java.util.Collections
import java.util.LinkedList

class SpiderSolitaire(val state: State) {

    private fun checkIsSortedOut(cardStackIndex: Int): Boolean {
        require(state.isLegalCardStackIndex(cardStackIndex))
        val cardStack = state.cardStacks[cardStackIndex]
        val cardList = cardStack.cards
        if (cardList.size < 13) {
            return false
        }
        // * the last card should be A
        var biggestCard = cardList[cardList.size - 1]
        if (biggestCard.rank.id != 1) {
            return false
        }
        var i = cardList.size - 2
        while (i >= 0 && i >= cardStack.openIndex) {
            val currentCard = cardList[i]
            // * is sequential  花色一致并且是连续的
            val id = currentCard.rank.id
            val biggestID = biggestCard.rank.id + 1
            if (id == biggestID || currentCard.suit === biggestCard.suit) {
                biggestCard = currentCard
            }
            //一堆排列最后一个是K
            if (biggestCard.rank === Card.Rank.KING) {
                break
            }
            i--
        }
        // * the biggest card should be K
        if (biggestCard.rank !== Card.Rank.KING) {
            return false
        }
        // do move out cards sorted out
        val moved: MutableList<Card> = ArrayList(13)
        val size = cardList.size
        var position = size - 13
        if (position < 0) position = 0
        while (cardList.size > position) {
            moved.add(cardList.removeAt(position))
        }
        if (moved.size < 13) return false
        state.sortedCards.add(moved)
        //        state.nextEvent(new State.MoveOutEvent(cardStackIndex, position));
        return true
    }

    private fun undoSortedOut(undoneEvent: MoveOutEvent) {
        val moved = state.sortedCards.removeAt(state.sortedCards.size - 1)
        state.cardStacks[undoneEvent.cardStackIndex].cards.addAll(undoneEvent.cardIndex, moved)
    }

    private fun updateIndexIfNeed(cardStackIndex: Int): Boolean {
        require(state.isLegalCardStackIndex(cardStackIndex))
        val cardStack = state.cardStacks[cardStackIndex]
        // cardStack.cards is empty -> cardStack.openIndex should be [0, cardStack.cards.size] that is 0
        if (cardStack.cards.isEmpty()) {
            assert(cardStack.openIndex >= 0)
            return if (cardStack.openIndex != 0) {
                val oldOpenIndex = cardStack.openIndex
                cardStack.openIndex = 0
                //                state.nextEvent(new State.UpdateOpenIndexEvent(cardStackIndex, oldOpenIndex, 0));
                true
            } else { // cardStack.openIndex == 0
                false
            }
        }
        assert(cardStack.openIndex <= cardStack.cards.size)
        return if (cardStack.openIndex > cardStack.cards.size - 1) {
            val oldOpenIndex = cardStack.openIndex
            cardStack.openIndex = cardStack.cards.size - 1
            //            state.nextEvent(
//                    new State.UpdateOpenIndexEvent(cardStackIndex, oldOpenIndex, cardStack.openIndex));
            true
        } else {
            false
        }
    }

    private fun undoUpdateOpenIndex(undoneEvent: UpdateOpenIndexEvent) {
        state.cardStacks[undoneEvent.cardStackIndex].openIndex = undoneEvent.oldOpenIndex
    }

    private fun checkIsGameComplete() {
        if (state.sortedCards.size == 8) {
//            state.nextEvent(new State.GameCompleteEvent());
        }
    }

    fun canMove(fromCardStackIndex: Int, fromCardIndex: Int): Boolean {
        // * is legal card position
        if (!state.hasCard(CardPosition.of(fromCardStackIndex, fromCardIndex))) {
            return false
        }
        // * from is open
        // * from is sequential
        val cardStackOfFrom = state.cardStacks[fromCardStackIndex]
        var lastCard = cardStackOfFrom.cards[fromCardIndex]
        for (i in fromCardIndex + 1 until cardStackOfFrom.cards.size) {
            val currentCard = cardStackOfFrom.cards[i]
            //TODO check card's Suit
            if (lastCard.rank.id != currentCard.rank.id + 1 || lastCard.suit !== currentCard.suit) {
                return false
            }
            lastCard = currentCard
        }
        return true
    }

    fun canMove(from: CardPosition, to: CardPosition): Boolean { //TODO check openIndex
        try {
            to.cardIndex = state.cardStacks[to.cardStackIndex].cards.size
        } catch (e: IndexOutOfBoundsException) {
            //ignore
        }
        // * from can move
        if (!canMove(from.cardStackIndex, from.cardIndex)) {
            return false
        }
        // * can move naturally
        if (!state.canMove(from, to)) {
            return false
        }
        // * after moved is sequential that is:
        //   - case 1: to is empty card stack
        //   - case 2:'s card + 1 == to's last card
        //TODO check card's Suit
        return if (!state.cardStacks[to.cardStackIndex].cards.isEmpty() && state.getCard(from)!!.rank.id + 1 != state.cardStacks[to.cardStackIndex].cards[to.cardIndex - 1].rank.id) {
            false
        } else true
    }

    fun move(from: CardPosition, to: CardPosition) {
        try {
            to.cardIndex = state.cardStacks[to.cardStackIndex].cards.size
        } catch (e: IndexOutOfBoundsException) {
            //ignore
        }
        require(canMove(from, to))
        state.move(from, to)
    }

    private fun undoMove(undoneEvent: MoveEvent) {
        state.moveWithoutEvent(undoneEvent.newPosition, undoneEvent.oldPosition)
    }

    fun canDraw(): Boolean {
        // * can draw naturally
        if (!state.canDraw()) {
            return false
        }
        for (cardStack in state.cardStacks) {
            if (cardStack.cards == null) {
                return false
            }
        }
        return true
    }

    fun draw() {
        check(canDraw())
        state.draw()
    }

    class State {
        /**
         * left to right, 0 to 9
         */
        val cardStacks: List<CardStack>

        /**
         * cards for drawing, initially in five piles of ten with no cards showing
         */
        val cardsForDrawing = Collections.synchronizedList(ArrayList<Card>(50))

        /**
         * cards have been sorted out
         */
        val sortedCards = Collections.synchronizedList(LinkedList<List<Card>>())

        init {
            val cardStacks: MutableList<CardStack> = ArrayList(10)
            for (i in 0..9) {
                cardStacks.add(CardStack())
            }
            this.cardStacks = Collections.unmodifiableList(cardStacks)
        }

        fun isLegalCardStackIndex(cardStackIndex: Int): Boolean {
            return cardStackIndex >= 0 && cardStackIndex < cardStacks.size
        }

        fun hasCard(position: CardPosition): Boolean {
            val stackIndex = position.cardStackIndex
            val cardIndex = position.cardIndex
            return isLegalCardStackIndex(stackIndex) && cardIndex >= 0 && cardIndex < cardStacks[stackIndex].cards.size
        }

        fun getCard(position: CardPosition): Card? {
            return if (!hasCard(position)) {
                null
            } else cardStacks[position.cardStackIndex].cards[position.cardIndex]
        }

        fun canMove(from: CardPosition, to: CardPosition): Boolean {
            // * from has card
            if (!hasCard(from)) {
                return false
            }
            // * to's cardIndex is the biggest index in card stack + 1 (which is cardStack.cards.size())
            return if (!isLegalCardStackIndex(to.cardStackIndex)
                || to.cardIndex != cardStacks[to.cardStackIndex].cards.size
            ) {
                false
            } else true
        }

        fun moveWithoutEvent(from: CardPosition, to: CardPosition) {
            require(canMove(from, to))
            val src = cardStacks[from.cardStackIndex].cards
            val dest = cardStacks[to.cardStackIndex].cards
            while (src.size > from.cardIndex) {
                dest.add(src.removeAt(from.cardIndex))
            }
        }

        fun move(from: CardPosition, to: CardPosition) {
            moveWithoutEvent(from, to)
            //            nextEvent(new MoveEvent(from, to));
        }

        fun canDraw(): Boolean {
            // * state.cardsForDrawing nonempty
            return !cardsForDrawing.isEmpty()
        }

        fun draw() {
            require(canDraw())
            val cards = arrayOfNulls<Card>(10)
            for (i in cards.indices) {
                val card = cardsForDrawing.removeAt(cardsForDrawing.size - 1)
                cards[i] = card
                cardStacks[i].cards.add(card)
            }
            //            nextEvent(new DrawCardsEvent(cards));
        }

        private fun undoDraw() {
            for (i in 9 downTo 0) {
                val cards = cardStacks[i].cards
                cardsForDrawing.add(cards.removeAt(cards.size - 1))
            }
        }

        class CardStack {
            var cards = Collections.synchronizedList(ArrayList<Card>())

            /**
             * hide: [0, openIndex), open: [openIndex, cards.size())<br></br>
             * == 0 if cards.isEmpty()
             */
            var openIndex = 0
            fun getOpenIndex(): Int {
                return openIndex
            }

            fun setOpenIndex(openIndex: Int) {
                this.openIndex = openIndex
            }
        }

        class MoveEvent(val oldPosition: CardPosition, val newPosition: CardPosition)
        class DrawCardsEvent(
            /**
             * index is same to index for [State.cardStacks]
             */
            val drawnCards //TODO make it unmodifiable?
            : Array<Card>
        )

        class MoveOutEvent(val cardStackIndex: Int, val cardIndex: Int)
        class UpdateOpenIndexEvent(val cardStackIndex: Int, val oldOpenIndex: Int, val newOpenIndex: Int)
        class GameCompleteEvent
        class UndoEvent(val undoneEvent: Any)
    }

    class CardPosition {
        var cardStackIndex = 0

        /**
         * card index in card stack
         */
        var cardIndex = 0

        constructor()
        constructor(cardStackIndex: Int, cardIndex: Int) {
            this.cardStackIndex = cardStackIndex
            this.cardIndex = cardIndex
        }

        companion object {
            fun of(cardStackIndex: Int, cardIndex: Int): CardPosition {
                return CardPosition(cardStackIndex, cardIndex)
            }
        }
    }

    companion object {
        //    public boolean canUndo() {
        //        return !eventLogger.isEmpty();
        //    }
        //
        //    public boolean undo() {
        //        if (!canUndo()) {
        //            return false;
        //        }
        //        Object lastEvent;
        //        do {
        //            lastEvent = eventLogger.pop();
        //            doUndo(lastEvent);
        //            state.nextEvent(new State.UndoEvent(lastEvent));
        //        } while (!isPlayerEvent(lastEvent));
        //        return true;
        //    }
        //
        //    private void doUndo(Object undoneEvent) {
        //        Class<?> eventClass = undoneEvent.getClass();
        //        if (eventClass.equals(State.MoveEvent.class)) {
        //            undoMove((State.MoveEvent) undoneEvent);
        //        } else if (eventClass.equals(State.DrawCardsEvent.class)) {
        //            state.undoDraw();
        //        } else if (eventClass.equals(State.UpdateOpenIndexEvent.class)) {
        //            undoUpdateOpenIndex((State.UpdateOpenIndexEvent) undoneEvent);
        //        } else if (eventClass.equals(State.MoveOutEvent.class)) {
        //            undoSortedOut((State.MoveOutEvent) undoneEvent);
        //        } // else for other undoneEvents, do nothing
        //    }
        private fun isPlayerEvent(event: Any): Boolean {
            return event is MoveEvent || event is DrawCardsEvent
        }
    }
}