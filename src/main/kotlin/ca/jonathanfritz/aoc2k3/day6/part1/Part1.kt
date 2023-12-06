package ca.jonathanfritz.aoc2k3.day6.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {
    fun boatRace(lines: List<String>): Int {
        val times = lines[0].tabSeparatedValuesToLongList()
        val distances = lines[1].tabSeparatedValuesToLongList()
        val numRaces = times.size

        return (0 until numRaces).map { race ->
            val solutionCount = (1 until distances[race]).map { holdTime ->
                // holding the button for 1ms increases speed by 1mm/ms, and reduces race time by 1ms
                val duration = times[race] - holdTime
                holdTime * duration
            }.count {
                // we want the number of ways to beat the current record
                it > distances[race]
            }

            println("In Race $race, there are $solutionCount ways to beat the record")
            solutionCount
        }.reduce(Int::times)
    }

    private fun String.tabSeparatedValuesToLongList(): List<Long> =
        this.split(' ').map { it.trim() }.filter { it.isNotBlank() }.drop(1).map { it.toLong() }
}

fun main() {
    println(Part1().boatRace(Utils.loadFromFile("Day6.txt")))
}