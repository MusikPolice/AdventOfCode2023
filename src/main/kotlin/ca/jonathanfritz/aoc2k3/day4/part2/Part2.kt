package ca.jonathanfritz.aoc2k3.day4.part2

import ca.jonathanfritz.aoc2k3.Utils
import ca.jonathanfritz.aoc2k3.day4.toNumberSet

class Part2 {
    fun scratchcards(lines: List<String>): Int {
        // parse the input into a map of all cards that could be granted, keyed on card id
        val cards = lines.associate { line ->
            val colonPos = line.indexOf(':')
            val cardId = line.subSequence(5, colonPos).toString().trim().toInt()
            val numberLists = line.substring(colonPos + 1).split('|')
            val winningNumbers = numberLists[0].toNumberSet()
            val numbersIHave = numberLists[1].toNumberSet()
            cardId to Card(cardId, winningNumbers, numbersIHave)
        }

        // each card that is granted will be added to the end of this list - we'll start with one of each card
        val toProcess = cards.values.toMutableList()
        var cardPointer = 0
        while (cardPointer < toProcess.size) {
            // find out how many matches there are on the next card in the list
            val card = toProcess[cardPointer]
            val matchingNumbers = card.winningNumbers.intersect(card.numbersIHave).size

            // grant the matching cards
            (card.cardId + 1 .. card.cardId + matchingNumbers).forEach { it ->
                toProcess.add(cards[it]!!)
            }

            // advance to the next card
            cardPointer++
        }

        return toProcess.size
    }

    private data class Card (
        val cardId: Int,
        val winningNumbers: Set<Int>,
        val numbersIHave: Set<Int>
    )
}
fun main() {
    println(Part2().scratchcards(Utils.loadFromFile("Day4.txt")))
}
