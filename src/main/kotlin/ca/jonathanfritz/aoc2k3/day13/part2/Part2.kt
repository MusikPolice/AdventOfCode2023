package ca.jonathanfritz.aoc2k3.day13.part2

import ca.jonathanfritz.aoc2k3.Utils

class Part2 {
    fun smudges(lines: List<String>): Int {
        val fields = lines.toFields()

        fields.forEach { field ->
            println(field)
            // find the original line of reflection - only one of these will be non-zero
            val initialCols = field.numColsLeftOfReflection()
            val initialRows = field.numRowsAboveReflection()

            (0 until field.height).map { row ->
                (0 until field.width).map { col ->
                    // flip the bit at (row, col)
                    val updated = field.copy(points = field.points.mapIndexed { r, fieldRow ->
                        fieldRow.mapIndexed{ c, entry ->
                            if (r == row && c == col) {
                                !entry
                            } else {
                                entry
                            }
                        }
                    })

                    // TODO: one of these should be zero, right?
                    val updatedCols = updated.numColsLeftOfReflection()
                    val updatedRows = updated.numRowsAboveReflection()
                    val updatedScore = updatedCols + (updatedRows * 100)

                    // TODO: not sure why two valid solutions are found for each input field... could be the mirrors of one another?
                    //  maybe just take the first and sum up the scores?
                    if (updatedScore > 0 && (updatedCols != initialCols || updatedRows != initialRows)) {
                        println("Flipping bit ($row, $col) results in score $updatedScore with $updatedCols cols and $updatedRows rows")
                    }
                }
            }

        }

        return 0
    }

    data class Field(val points: List<List<Boolean>>) {
        val width = points[0].size
        val height = points.size

        // store columns and rows as binary-encoded integers allowing us to do quick equality checks
        private val columns = (0 until width).map { col ->
            points.map { if (it[col]) '1' else '0' }.joinToString("").toInt(2)
        }
        private val rows = points.map { row -> row.map { if (it) '1' else '0' }.joinToString("").toInt(2) }

        // returns the number of columns to the left of the vertical line of reflection, or zero if no vertical line found
        fun numColsLeftOfReflection(): Int = (0 until width - 1).filter { col ->
            // find the left index of any columns that are beside one another
            columns[col] == columns[col + 1]
        }.filter {col ->
            // scan left and right to determine if the field is mirrored along the invisible line between left and right
            var left = col
            var right = col + 1
            do {
                // yuck
                if (left < 0 || right >= width) return@filter true
                if (columns[left] != columns[right]) return@filter false
                left -= 1
                right += 1
            } while (left > 0 || right < width)

            // must be a mirror
            true
        }.firstOrNull()?.let { it + 1 } ?: 0

        // returns the number of columns above the horizontal line of reflection, or zero if no horizontal line found
        fun numRowsAboveReflection(): Int = (0 until height - 1).filter { row ->
            // find the upper index of any rows that are beside one another
            rows[row] == rows[row + 1]
        }.filter { row ->
            // scan up and down to determine if the field is mirrored along the invisible line between top and bottom
            var top = row
            var bottom = row + 1
            do {
                // also yuck
                if (top < 0 || bottom >= height) return@filter true
                if (rows[top] != rows[bottom]) return@filter false
                top -= 1
                bottom += 1
            } while (top > 0 || bottom < height)

            // must be a mirror
            true
        }.firstOrNull()?.let { it + 1 } ?: 0
    }

    private fun List<String>.toFields(): List<Field> {
        val fields: MutableList<Field> = mutableListOf()
        val field: MutableList<String> = mutableListOf()
        this.forEach {
            if (it.isNotBlank()) {
                field.add(it)
            } else {
                fields.add(field.toField())
                field.clear()
            }
        }
        fields.add(field.toField())
        return fields.toList()
    }

    private fun MutableList<String>.toField(): Field = Field(this.map { line -> line.map { it == '#' } })
}

fun main() {
    println(Part2().smudges(Utils.loadFromFile("Day13.txt")))
}