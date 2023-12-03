package ca.jonathanfritz.aoc2k3.day2.part1

import ca.jonathanfritz.aoc2k3.Utils
import ca.jonathanfritz.aoc2k3.day2.RGB
import ca.jonathanfritz.aoc2k3.day2.toGame

class Part1 {

    // we know the max allowed number of each colour of cube
    private val maxCubeColours = RGB(12,13,14)

    fun cubes(lines: List<String>): Int {
        return lines.map { line ->
            line.toGame()
        }.filter {
            // only games where the max number of coloured cubes shown in any round was less than the known max
            // allowed number of cubes of that colour are valid
            val c = it.maxCubeColours()
            c.red <= maxCubeColours.red && c.green <= maxCubeColours.green && c.blue <= maxCubeColours.blue
        }.sumOf { it.id }
    }
}

fun main() {
    println(Part1().cubes(Utils.loadFromFile("Day2.txt")))
}