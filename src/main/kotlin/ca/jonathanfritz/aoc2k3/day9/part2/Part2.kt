package ca.jonathanfritz.aoc2k3.day9.part2

import ca.jonathanfritz.aoc2k3.Utils

class Part2 {
    fun triangle(lines: List<String>): Int {
        return lines.sumOf { line ->
            // expand the triangle until the last row is all zeros
            val rows = mutableListOf(line.split(' ').map { it.trim().toInt() }.toMutableList())
            do {
                val prev = rows.last()
                rows.add((1 until prev.size).map { i -> prev[i] - prev[i - 1] }.toMutableList())
            } while (!rows.last().all { it == 0 })

            // step backward through rows to compute the history for the first row
            (rows.size - 3 downTo 0).forEach { rowIndex ->
                // this time around, we want to use the start of each row and do subtraction instead of addition
                rows[rowIndex].add(0, rows[rowIndex].first() - rows[rowIndex + 1].first())
            }

            // the first element of the first row is the history for this triangle
            rows[0].first()
        }
    }
}

fun main() {
    println(Part2().triangle(Utils.loadFromFile("Day9.txt")))
}