package ca.jonathanfritz.aoc2k3.day4.part1

import ca.jonathanfritz.aoc2k3.Utils
import ca.jonathanfritz.aoc2k3.day4.toNumberSet

class Part1 {
    fun scratchcards(lines: List<String>) = lines.sumOf { line ->
        // parse the card
        val colonPos = line.indexOf(':')
        val numberLists = line.substring(colonPos + 1).split('|')
        val winningNumbers = numberLists[0].toNumberSet()
        val numbersIHave = numberLists[1].toNumberSet()

        // compute the score
        val matches = winningNumbers.intersect(numbersIHave)
        val score = if (matches.isNotEmpty()) {
            var s = 1
            (0 until matches.size - 1).forEach { _ ->
                s *= 2
            }
            s
        } else {
            0
        }
        score
    }
}

fun main() {
    println(Part1().scratchcards(Utils.loadFromFile("Day4.txt")))
}