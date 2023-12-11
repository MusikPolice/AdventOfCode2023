package ca.jonathanfritz.aoc2k3.day11.part1

import ca.jonathanfritz.aoc2k3.Utils
import kotlin.math.abs

class Part1 {
    fun galaxies(lines: List<String>): Int {
        // start by expanding the universe so that we can get accurate positions for each galaxy
        val expandedLines: MutableList<MutableList<Char>> = lines.map { it.toCharArray().toMutableList() }.toMutableList()
        var rowOffset = 0
        (0 until expandedLines.size).mapNotNull { row ->
            if (expandedLines[row].all { it == '.' }) { row } else { null }
        }.forEach { row ->
            // duplicate every row that consists entirely of . characters
            expandedLines.add(row + rowOffset, "".padEnd(expandedLines[0].size, '.').toMutableList())

            // when adding a row, offset the remaining rows to add by one because everything shifts down
            rowOffset++
        }
        var colOffset = 0
        (0 until expandedLines[0].size).mapNotNull { col ->
            if (expandedLines.map { it[col] }.all { it == '.' }) { col } else { null }
        }.forEach { col ->
            // duplicate every column that consists entirely of . characters
            (0 until expandedLines.size).forEach { row ->
                expandedLines[row].add(col + colOffset, '.')
            }

            // when adding a column, offset the remaining columns to add by one because everything shifts right
            colOffset++
        }

        //expandedLines.forEach { println(it.joinToString( separator = "" )) }

        // enumerate the list of galaxies and their positions in the expanded universe
        var galaxyCount = 0
        val galaxies = expandedLines.flatMapIndexed { row, chars ->
            chars.mapIndexedNotNull { col, char ->
                if (char == '#') { Galaxy(galaxyCount++, row, col) } else { null }
            }
        }

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



/*
    fun Pair<Galaxy, Galaxy>.equals(other: Any?): Boolean {
        val cast = other as Pair<Galaxy, Galaxy>
        return setOf(this.first, this.second) == setOf(cast.first, cast.second)
    }*/
}

fun main() {
    println(Part1().galaxies(Utils.loadFromFile("Day11.txt")))
}