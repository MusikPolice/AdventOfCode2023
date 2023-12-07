package ca.jonathanfritz.aoc2k3.day7.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {
    fun camelCards(lines: List<String>): Long {
        return lines.asSequence().map { it.split(' ') }
            .map { Hand(it[0], it[1].toInt()) }
            .sortedDescending()
            .mapIndexed{ i, hand ->
                val rank = lines.size - i
                println("${hand.cards} has score ${hand.bid} * $rank = ${hand.bid*rank}")
                (hand.bid * rank).toLong()
            }.sum()
    }

    private data class Hand(
        val cards:String,
        val bid:Int,
        private val cardCounts: Map<Char, Int> = cards.toCharArray().groupBy { it }.map { it.key to it.value.size }.toMap()
    ): Comparable<Hand> {

        // returns a number between 1 and 7, where 7 is the best hand (five of a kind) and 1 is the worst (high card)
        private fun rank(): Int {
            return if (cardCounts.size == 1 && cardCounts.values.contains(5)) {
                7   // five of a kind
            } else if (cardCounts.size == 2 && cardCounts.values.contains(4)) {
                6   // four of a kind
            } else if (cardCounts.size == 2 && cardCounts.values.contains(3) && cardCounts.values.contains(2)) {
                5   // full house
            } else if (cardCounts.size == 3 && cardCounts.values.contains(3)) {
                4   // three of a kind
            } else if (cardCounts.size == 3 && cardCounts.values.count { it == 2 } == 2) {
                3   // two pair
            } else if (cardCounts.size == 4 && cardCounts.values.count { it == 2 } == 1) {
                2   // one pair
            } else {
                1   // high card
            }
        }

        override fun compareTo(other: Hand): Int {
            val handDelta = rank().compareTo(other.rank())
            return if (handDelta != 0) {
                handDelta
            } else {
                val myCards = cards.toCharArray().map { it.toCardRank() }
                val theirCards = other.cards.toCharArray().map { it.toCardRank() }

                // loop through each hand, comparing card ranks until we find two that are different
                (0 until 5).forEach { i ->
                    val cardDelta = myCards[i].compareTo(theirCards[i])
                    if (cardDelta != 0) {
                        return cardDelta
                    }
                }

                // every card is equal - the hands are the same
                return 0
            }
        }

        private fun Char.toCardRank() = when (this) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 11
            'T' -> 10
            else -> "$this".toInt()
        }
    }
}

fun main() {
    println(Part1().camelCards(Utils.loadFromFile("Day7.txt")))
}