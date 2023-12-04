package ca.jonathanfritz.aoc2k3.day3.part1.day4.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {
    fun scratchcards(lines: List<String>) = lines.sumOf { line ->
        val colonPos = line.indexOf(':')
        val cardId = line.subSequence(5, colonPos)
        val numberLists = line.substring(colonPos + 1).split('|')
        val winingNumbers = numberLists[0].toNumberSet()
        val numbersIHave = numberLists[1].toNumberSet()
        val matches = winingNumbers.intersect(numbersIHave)
        val score = if (matches.isNotEmpty()) {
            var s = 1
            (0 until matches.size - 1).forEach { _ ->
                s *= 2
            }
            s
        } else {
            0
        }
        println("Card $cardId score is $score")
        score
    }

    private fun String.toNumberSet(): Set<Int> = this.split(' ')
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .toSet()
}

fun main() {
    println(Part1().scratchcards(Utils.loadFromFile("Day4.txt")))
}