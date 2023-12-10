package ca.jonathanfritz.aoc2k3.day9.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {
    fun triangle(lines: List<String>): Int {
        // this can probably be expressed as some kind of binomial expansion, but math is hard so this is an iterative solution
        return lines.sumOf { line ->
            // expand the triangle until the last row is all zeros
            val rows = mutableListOf(line.split(' ').map { it.trim().toInt() }.toMutableList())
            do {
                val prev = rows.last()
                rows.add((1 until prev.size).map { i -> prev[i] - prev[i - 1] }.toMutableList())
            } while (!rows.last().all { it == 0 })

            // step backward through rows to compute the history for the first row
            (rows.size - 3 downTo 0).forEach { rowIndex ->
                rows[rowIndex].add(rows[rowIndex].last() + rows[rowIndex + 1].last())
            }

            // the last element of the first row is the history for this triangle
            rows[0].last()
        }
    }
}

fun main() {
    println(Part1().triangle(Utils.loadFromFile("Day9.txt")))
}