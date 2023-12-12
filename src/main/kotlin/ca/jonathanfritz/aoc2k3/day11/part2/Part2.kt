package ca.jonathanfritz.aoc2k3.day11.part2

import ca.jonathanfritz.aoc2k3.Utils
import kotlin.math.abs

class Part2 {

    private val multiplier = 2

    fun galaxies(lines: List<String>): Int {
        // find the indices of the rows and columns that will need to be expanded
        val rowsToExpand = lines.mapIndexedNotNull { row, line ->
            if (line.all { it == '.' }) { row } else { null }
        }
        val colsToExpand = lines.mapIndexedNotNull { col, _ ->
            if (lines.map { it[col] }.all { it == '.' }) { col } else { null }
        }
        println("rowsToExpand: $rowsToExpand")
        println("colsToExpand: $colsToExpand")

        // find all the galaxies by iterating over the original input matrix, tracking our actual position in the
        // expanded universe and using it as the position of each found galaxy
        // TODO: something is wrong with offset row
        val galaxies: MutableList<Galaxy> = mutableListOf()
        (lines.indices).forEach { row ->
            var rowOffset = 0
            var colOffset = 0
            (0 until lines[0].length).forEach { col ->
                if (rowsToExpand.contains(row + rowOffset)) { rowOffset += multiplier - 1 }
                if (colsToExpand.contains(col + colOffset)) { colOffset += multiplier - 1 }

                if (lines[row][col] == '#') {
                    println("Galaxy at ($row, $col) expands to (${row + rowOffset}, ${col + colOffset})")
                    galaxies.add(Galaxy(galaxies.size, row + rowOffset, col + colOffset))
                }
            }
        }
        println("Expanded galaxies:")
        galaxies.forEach { println(it) }

        // generate the list of pairs of galaxies (cartesian product?)
        return galaxies.flatMap { g1 ->
            galaxies.map { g2 ->
                // enclosing each pair in a set lets use .distinct() to de-dupe the pairs
                setOf(g1, g2)
            }.filterNot {
                // galaxies paired with themselves result in a set with one element
                // the distance between a galaxy and itself is zero, so there's no need to include this pair
                it.size == 1
            }
        }.distinct().map { pair ->
            pair.toList()[0] to pair.toList()[1]
        }.sumOf { (g1, g2) ->
            // shortest path between each pair is the manhattan distance
            g1.manhattanDistance(g2)
        }
    }

    data class Galaxy(
        val id: Int,
        val row: Int,
        val col: Int
    ) {
        fun manhattanDistance(other: Galaxy): Int {
            return abs(row - other.row) + abs(col - other.col)
        }
    }
}

fun main() {
    println(Part2().galaxies(Utils.loadFromFile("Day11.txt")))
}