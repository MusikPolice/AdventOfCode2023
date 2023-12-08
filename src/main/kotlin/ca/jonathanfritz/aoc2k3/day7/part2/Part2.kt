package ca.jonathanfritz.aoc2k3.day7.part2

import ca.jonathanfritz.aoc2k3.Utils

class Part2 {
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
        private val cardCounts: Map<Char, Int> = cards.toCharArray().filterNot { it == 'J' }.groupBy { it }.map { it.key to it.value.size }.toMap(),
        private val jokers: Int = cards.toCharArray().count { it == 'J' },
    ): Comparable<Hand> {

        // compute this hand's rank once on initialization instead of every time it is passed to the comparator
        val rank: Int
        init {
            rank = rank()
        }

        private fun isFiveOfAKind() = cardCounts.values.contains(5)
        private fun isFourOfAKind() = cardCounts.values.contains(4)
        private fun isFullHouse() = cardCounts.values.contains(3) && cardCounts.values.contains(2)
        private fun isThreeOfAKind() = cardCounts.values.contains(3)
        private fun isTwoPair() = cardCounts.values.count { it == 2 } == 2
        private fun isOnePair() = cardCounts.values.count { it == 2 } == 1

        // returns a number between 1 and 7, where 7 is the best hand (five of a kind) and 1 is the worst (high card)
        private fun rank(): Int {
            return if (isFiveOfAKind() || (isFourOfAKind() && jokers == 1) || (isThreeOfAKind() && jokers == 2) || (isOnePair() &&  jokers == 3) || jokers >= 4) {
                7   // five of a kind
            } else if (isFourOfAKind() || (isThreeOfAKind() && jokers == 1) || (isOnePair() && jokers == 2) || jokers == 3) {
                6   // four of a kind
            } else if (isFullHouse() || (isTwoPair() && jokers == 1) || (isThreeOfAKind() && jokers == 2)) {
                5   // full house
            } else if (isThreeOfAKind() || (isOnePair() && jokers == 1) || jokers == 2) {
                4   // three of a kind
            } else if (isTwoPair()) {
                3   // two pair
            } else if (isOnePair() || jokers == 1) {
                2   // one pair
            } else {
                1   // high card
            }
        }

        override fun compareTo(other: Hand): Int {
            // start by comparing hand ranks
            val handDelta = rank.compareTo(other.rank)
            return if (handDelta != 0) {
                handDelta
            } else {
                // in case of a tie, loop through each hand, comparing card ranks until we find two that are different
                val myCards = cards.toCharArray().map { it.toCardRank() }
                val theirCards = other.cards.toCharArray().map { it.toCardRank() }

                (0 until 5).forEach { i ->
                    val cardDelta = myCards[i].compareTo(theirCards[i])
                    if (cardDelta != 0) {
                        return cardDelta
                    }
                }

                // every card is equal, so the hands are identical
                return 0
            }
        }

        private fun Char.toCardRank() = when (this) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'T' -> 10
            'J' -> 1    // when comparing individual cards, Joker is now worth 1 point
            else -> "$this".toInt()
        }
    }
}

fun main() {
    println(Part2().camelCards(Utils.loadFromFile("Day7.txt")))
}