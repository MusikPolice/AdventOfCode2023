package ca.jonathanfritz.aoc2k3.day2.part2

import ca.jonathanfritz.aoc2k3.Utils
import ca.jonathanfritz.aoc2k3.day2.toGame

class Part2 {
    fun powers(lines: List<String>): Int {
        return lines.map { line ->
            line.toGame()
        }.sumOf {
            // the power of each game is the minimum number of cubes of each colour that it could have been
            // played with, all multiplied together
            val c = it.maxCubeColours()
            c.red * c.blue * c.green
        }
    }
}

fun main() {
    println(Part2().powers(Utils.loadFromFile("Day2.txt")))
}