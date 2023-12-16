package ca.jonathanfritz.aoc2k3.day15.part2

import ca.jonathanfritz.aoc2k3.Utils

class Part2 {
    // there are 255 boxes, each starting with an empty collection of lenses
    private val boxes = List(255, init = { Box(it)})

    fun hashmap(lines: List<String>): Int {
        lines.flatMap { it.split(',') }.forEach { operation ->
            val label = operation.split('-', '=')[0]
            val boxId = label.hash()

            if (operation.endsWith('-')) {
                // remove a lens with the specified label from the box with the derived boxId
                boxes[boxId].lenses.removeIf { it.label == label }
            } else {
                val focalLength = operation.split('=')[1].toInt()
                val lensIndex = boxes[boxId].lenses.indexOfFirst { it.label == label }
                if (lensIndex == -1) {
                    // insert a new lens
                    boxes[boxId].lenses.add(Lens(label, focalLength))
                } else {
                    // replace the existing lens with the new one
                    boxes[boxId].lenses.removeAt(lensIndex)
                    boxes[boxId].lenses.add(lensIndex, Lens(label, focalLength))
                }
            }
        }

        return boxes.sumOf { it.focusingPower() }
    }

    // hash algorithm from part 1
    private fun String.hash(): Int {
        var current = 0
        this.toCharArray().forEach { token ->
            current += token.code
            current *= 17
            current %= 256
        }
        return current
    }

    private data class Box (
        val id: Int,
        val lenses: MutableList<Lens> = mutableListOf()
    ) {
        fun focusingPower() = lenses.mapIndexed { i, lens -> lens.focusingPower(id, i) }.sum()
    }

    private data class Lens (
        val label: String,
        val focalLength: Int    // 1..9 inclusive
    ) {
        fun focusingPower(boxId: Int, slot: Int) = (boxId + 1) * (slot + 1) * focalLength
    }
}

fun main() {
    println(Part2().hashmap(Utils.loadFromFile("Day15.txt")))
}