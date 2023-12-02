package ca.jonathanfritz.aoc2k3.day1.part2

import ca.jonathanfritz.aoc2k3.Utils

class Part2 {

    private val map = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )
    private val tokens = map.keys + map.values.map { "$it" }

    fun trebuchet(lines: List<String>) = lines.map { line ->
        val firstStr = line.findAnyOf(tokens)?.second ?: throw IllegalArgumentException("Failed to find first token in $line")
        val lastStr = line.findLastAnyOf(tokens)?.second ?: throw IllegalArgumentException("Failed to find last token in $line")
        val first = toInt(firstStr)
        val last = toInt(lastStr)
        "$first$last".toInt()
    }.sum()

    private fun toInt(s: String) =
        if (s.length == 1) { s.toInt() } else { map[s] } ?: throw IllegalArgumentException("$s is not a number")
}

fun main() {
    println(Part2().trebuchet(Utils.loadFromFile("Day1.txt")))
}
