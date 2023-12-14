package ca.jonathanfritz.aoc2k3.day13.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {
    fun mirrors(lines: List<String>): Int {
        val fields = lines.toFields()
        return fields.sumOf { field ->
            val leftCount = (0 until field.width - 1).filter { col ->
                // find the left index of any columns that are beside one another
                field.columns[col] == field.columns[col + 1]
            }.filter {col ->
                // scan left and right to determine if the field is mirrored along the invisible line between left and right
                var left = col
                var right = col + 1
                do {
                    // yuck
                    if (left < 0 || right >= field.width) return@filter true
                    if (field.columns[left] != field.columns[right]) return@filter false
                    left -= 1
                    right += 1
                } while (left > 0 || right < field.width)

                // must be a mirror
                true
            }.firstOrNull()?.let { it + 1 } ?: 0

            val upperCount = (0 until field.height - 1).filter { row ->
                // find the upper index of any rows that are beside one another
                field.rows[row] == field.rows[row + 1]
            }.filter { row ->
                // scan up and down to determine if the field is mirrored along the invisible line between top and bottom
                var top = row
                var bottom = row + 1
                do {
                    // also yuck
                    if (top < 0 || bottom >= field.height) return@filter true
                    if (field.rows[top] != field.rows[bottom]) return@filter false
                    top -= 1
                    bottom += 1
                } while (top > 0 || bottom < field.height)

                // must be a mirror
                true
            }.firstOrNull()?.let { it + 1 } ?: 0

            // add up the score for this field
            leftCount + (upperCount * 100)
        }
    }

    private class Field(lines: List<String>) {
        val width = lines[0].length
        val height = lines.size
        val points: List<List<Boolean>> = lines.map { line -> line.map { it == '#' } }  // TODO: eliminate points?

        // store columns and rows as binary-encoded integers allowing us to do quick equality checks
        val columns = (0 until width).map { col ->
            points.map { if (it[col]) '1' else '0' }.joinToString("").toInt(2)
        }
        val rows = points.map { row -> row.map { if (it) '1' else '0' }.joinToString("").toInt(2) }
    }

    private fun List<String>.toFields(): List<Field> {
        val fields: MutableList<Field> = mutableListOf()
        val field: MutableList<String> = mutableListOf()
        this.forEach {
            if (it.isNotBlank()) {
                field.add(it)
            } else {
                fields.add(Field(field))
                field.clear()
            }
        }
        fields.add(Field(field))
        return fields.toList()
    }
}

fun main() {
    println(Part1().mirrors(Utils.loadFromFile("Day13.txt")))
}