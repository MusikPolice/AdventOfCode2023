package ca.jonathanfritz.aoc2k3.day8.part2

import ca.jonathanfritz.aoc2k3.Utils

class Part2 {
    fun ghostNetwork(lines: List<String>): Int {
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

        // we proceed simultaneously from every node whose identifier ends with 'A'
        var currentNodes = nodes.keys.filter { it.endsWith('A') }
        // println("Step 0: You are at $currentNodes")
        do {
            val instruction = instructions[ip % instructions.size]
            currentNodes = currentNodes.map { currentNode ->
                if (instruction == 'L') {
                    nodes[currentNode]!!.first
                } else {
                    nodes[currentNode]!!.second
                }
            }
            ip++
            // println("Step $ip: You choose all of the $instruction paths, leading you to $currentNodes")
        } while (!currentNodes.all { it.endsWith('Z') })

        // return the number of steps we took to get to the end
        return ip
    }
}

fun main() {
    println(Part2().ghostNetwork(Utils.loadFromFile("Day8.txt")))
}