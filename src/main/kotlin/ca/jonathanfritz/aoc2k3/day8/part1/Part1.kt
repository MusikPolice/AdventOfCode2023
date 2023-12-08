package ca.jonathanfritz.aoc2k3.day8.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {
    fun theNetwork(lines: List<String>): Int {
        // treat instructions as a ring buffer with an index pointer that we always access modulo buffer length
        val instructions = lines[0].toCharArray()
        var ip = 0

        // to avoid building a binary tree, nodes will be a simple map with the children represented as a pair
        val nodes: Map<String, Pair<String, String>> = lines.drop(2).associate { line ->
            val equals = line.indexOf('=')
            val key = line.substring(0, equals - 1).trim()
            val (left, right) = line.replace("(", "")
                .replace(")", "")
                .substring(equals + 1)
                .split(',')
            key to (left.trim() to right.trim())
        }

        // traverse the tree iteratively, starting at AAA and looking for ZZZ
        var currentNode = "AAA"
        do {
            val prevNode = currentNode  // debugging
            val instruction = instructions[ip % instructions.size]
            when (instruction) {
                'L' -> currentNode = nodes[currentNode]!!.first
                'R' -> currentNode = nodes[currentNode]!!.second
            }
            println("Step 0: $prevNode -> $currentNode ($instruction)")
            ip++
        } while (currentNode != "ZZZ")

        // return the number of steps we took to get to the end
        return ip
    }
}

fun main() {
    println(Part1().theNetwork(Utils.loadFromFile("Day8.txt")))
}