package ca.jonathanfritz.aoc2k3.day15.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {
    fun hash(lines: List<String>): Int {
        return lines.flatMap { it.split(',') }.sumOf { operation ->
            var current = 0
            operation.toCharArray().forEach { token ->
                current += token.code
                current *= 17
                current %= 256
            }
            current
        }
    }
}

fun main() {
    println(Part1().hash(Utils.loadFromFile("Day15.txt")))
}