package ca.jonathanfritz.aoc2k3.day1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {

    fun trebuchet(lines: List<String>) = lines.sumOf { line ->
        val first = line.first { it.isDigit() }
        val last = line.last { it.isDigit() }
        "$first$last".toInt()
    }
}

fun main() {
    println(Part1().trebuchet(Utils.loadFromFile("day1/Trebuchet.txt")))
}