package com.spider.composes.model

class Card(val suit: Suit, val rank: Rank) {

    enum class Suit {
        /** ♠  */
        SPADE,

        /** ♣  */
        CLUB,

        /** ♥  */
        HEART,

        /** ♦  */
        DIAMOND
    }

    enum class Rank(val id: Int) {
        ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5),
        SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13);

        companion object {
            fun fromId(id: Int): Rank {
                return values()[id - 1]
            }
        }
    }
}