package ca.jonathanfritz.aoc2k3.day12.part1

import ca.jonathanfritz.aoc2k3.Utils
import kotlin.math.pow

class Part1 {
    fun springs(lines: List<String>): Int {
        return lines.sumOf { line ->
            val input = line.split(' ')[0].trim()
            val numUnknowns = input.count { it == '?' }
            val upperBound = 2.pow(numUnknowns)
            val arrangements = (0 .. upperBound).map {
                // convert the number into a binary string consisting of . and # characters
                val unknowns = it.toString(2)
                    .replace('0', '.')
                    .replace('1', '#')
                    .padStart(numUnknowns, '.')

                // replace each sequence of ??? chars with a chunk from our binary string
                var offset = 0
                input.map { char ->
                    if (char == '?') {
                        unknowns[offset++]
                    } else {
                        char
                    }
                }.joinToString("")
            }.toSet()

            // debugging
            /*println("Arrangements of $input:")
            arrangements.forEach { println(it) }*/

            // turn the checksum into a regex
            val checksum = line.split(' ')[1].trim().split(',').map { it.toInt() }
            val sb = StringBuilder("^\\.*") // start of string, followed by zero or more . characters
            checksum.forEachIndexed { i, numBrokenSprings ->
                sb.append(
                    if (numBrokenSprings == 1) {
                        "#"                     // one hash character
                    } else {
                        "#{$numBrokenSprings}"  // numBrokenSprings hash characters
                    }
                )
                sb.append(
                    if (i == checksum.size - 1) {
                        "\\.*\$"                // zero or more . characters followed by the end of the string
                    } else {
                        "\\.+"                  // one or more . characters
                    }
                )
            }

            // return the number of arrangements that satisfy the checksum
            val r = Regex(sb.toString())
            val matches = arrangements.count { r.matches(it) }
            println("There are $matches arrangements that match $line")
            matches
        }
    }

    private fun Int.pow(exponent: Int): Int = this.toDouble().pow(exponent).toInt()
}

fun main() {
    println(Part1().springs(Utils.loadFromFile("Day12.txt")))
}