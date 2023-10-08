package com.spider.composes.model.engine

import com.spider.composes.model.Card
import com.spider.composes.model.Card.Rank
import com.spider.composes.model.Card.Suit

/**
 * @Time: 2021/5/18 12:00 下午
 * @Version: 1.0.0  数据构建model
 * <lp>
 * @UpdateUser: SpiderSolitaires
 * @UpdateDate: 2021/5/18 12:00 下午
 * @UpdateRemark: SpiderSolitaires
</lp> */
object SpiderSolitaires {

    private const val MAX_CARDS = 52

    private fun newCards(decks: Int, suit: Array<Suit>): MutableList<Card> {
        val typeCount = 4
        val result: MutableList<Card> = ArrayList(decks * MAX_CARDS)
        for (rank in Rank.values()) {
            for (i in 0 until typeCount * decks) {
                result.add(Card(suit[i % typeCount], rank))
            }
        }
        return result
    }

    fun newGameState(level: Int): SpiderSolitaire.State {
        // DES：使用2副牌
        val decks = 2
        // DES：1个花色 2个 4个
        val cards = when (level) {
            1 -> {
                val suit = Suit.entries.shuffled().first()
                newCards(decks, arrayOf(suit, suit, suit, suit))
            }

            2 -> newCards(decks, arrayOf(Suit.HEART, Suit.SPADE, Suit.HEART, Suit.SPADE))
            4 -> newCards(decks, Suit.entries.toTypedArray())
            else -> emptyList()
        }.shuffled().toMutableList()

        val result = SpiderSolitaire.State()
        for (i in 0..53) {
            result.cardStacks[i % 10].cards.add(cards.removeAt(cards.size - 1))
        }
        for (stack in result.cardStacks) {
            stack.openIndex = stack.cards.size - 1
        }
        result.cardsForDrawing.addAll(cards)
        return result
    }
}