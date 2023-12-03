package ca.jonathanfritz.aoc2k3.day3.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {
    fun partNumbers(lines: List<String>): Int {
        val numCols = lines[0].length
        val numRows = lines.size

        // make a char matrix of row x col
        val schematic = lines.map { it.toCharArray() }

        // scan for symbols (any char this not a digit and not a .)
        val symbolPositions = (0 until numRows).flatMap { row ->
            (0 until numCols).map { column ->
                row to column
            }
        }.filter { (row, column) ->
            val entry = schematic[row][column]
            !(entry.isDigit() || entry == '.')
        }

        // when found, look in all adjacent positions for a numeral
        // some resulting positions may point to the same number
        val partNumberPositions = symbolPositions.flatMap { (row, column) ->
            val prevRow = maxOf(0, row - 1)
            val nextRow = minOf(numRows - 1, row + 1)
            val prevCol = maxOf(0, column - 1)
            val nextCol = minOf(numCols - 1, column + 1)
            setOf(
                prevRow to prevCol, // top left
                prevRow to column,  // top
                prevRow to nextCol, // top right
                row to nextCol,     // right
                nextRow to nextCol, // bottom right
                nextRow to column,  // bottom
                nextRow to prevCol, // bottom left
                row to prevCol      // left
            ).filter { (row, column) ->
                schematic[row][column].isDigit()
            }
        }.distinct()

        // scan left and right of each part number position to find the entire part number
        // this bounds checking is 🤮
        return partNumberPositions.map { (row, column) ->
            val left = ((column downTo -1).firstOrNull { !schematic[row][maxOf(0, it)].isDigit() } ?: - 1) + 1
            val right = ((column until numCols).firstOrNull { !schematic[row][minOf(numCols - 1, it)].isDigit() } ?: numCols) - 1
            schematic[row].slice(left..right).joinToString("").toInt()
        }.distinct().sum()
    }
}

fun main() {
    // TODO: this works on the sample, but not on the full input :/
    println(Part1().partNumbers(Utils.loadFromFile("Day3.txt")))
}