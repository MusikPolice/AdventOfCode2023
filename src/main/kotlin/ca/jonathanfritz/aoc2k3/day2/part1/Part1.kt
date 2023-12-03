package ca.jonathanfritz.aoc2k3.day2.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {

    // we know the max allowed number of each colour of cube
    private val maxCubeColours = RGB(12,13,14)

    fun cubes(lines: List<String>): Int {
        return lines.map { line ->
            // parse each line into a game with an id and a list of rounds; each round has cube colour counts
            val parts = line.split(':',';').map { it.trim() }
            val id = parts[0].replace("Game ", "").trim().toInt()
            val rounds = parts.subList(1, parts.size).map { it.toRGB() }
            Game(id, rounds)
        }.filter {
            // only games where the max number of coloured cubes shown in any round was less than the known max
            // allowed number of cubes of that colour are valid
            val c = it.maxCubeColours()
            c.red <= maxCubeColours.red && c.green <= maxCubeColours.green && c.blue <= maxCubeColours.blue
        }.sumOf { it.id }
    }

    private data class Game (
        val id: Int,
        val rounds: List<RGB>
    ) {
        fun maxCubeColours(): RGB = RGB (
            rounds.maxOf { it.red },
            rounds.maxOf { it.green },
            rounds.maxOf { it.blue }
        )
    }

    private data class RGB (
        val red: Int,
        val blue: Int,
        val green: Int
    )

    private fun String.toRGB(): RGB {
        val colours = this.split(',').map { it.trim() }
        return RGB(
            colours.extractColourCount("red"),
            colours.extractColourCount("blue"),
            colours.extractColourCount("green")
        )
    }

    private fun List<String>.extractColourCount(colour: String) =
        this.firstOrNull { it.endsWith(colour) }?.split(' ')?.get(0)?.toInt() ?: 0
}

fun main() {
    println(Part1().cubes(Utils.loadFromFile("Day2.txt")))
}