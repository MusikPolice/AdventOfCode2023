package ca.jonathanfritz.aoc2k3.day10.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {
    fun pipes(lines: List<String>): Int {
        // parse pipes into a map of row:column
        val map: List<List<Node>> = lines.mapIndexed { row, line ->
            line.toCharArray().mapIndexed { col, char ->
                Node(row, col, PipeType.values().first { it.char == char })
            }
        }
        val start = map.flatten().first { it.pipe == PipeType.START }

        // starting at S, treat it as each type of pipe and trace the path looking for a loop
        // there is no branching, so we should just be able to follow the path until we hit either S or a dead end
        val paths: Map<PipeType, List<Node>> = PipeType.values().filterNot { it == PipeType.START || it == PipeType.NONE }.associate { sPipeType ->
            val path: MutableList<Node> = mutableListOf(start)
            var prev: Node? = null
            var cur = start.copy(pipe = sPipeType)
            do {
                // where we go depends on where we came from and what kind of pipe we're on
                val next: Node = when (cur.pipe) {

                    PipeType.NORTH_SOUTH -> if (prev.isNorthOf(cur)) {
                        cur.goSouth(map, sPipeType)
                    } else {
                        cur.goNorth(map, sPipeType)
                    }

                    PipeType.EAST_WEST -> if (prev.isEastOf(cur)) {
                        cur.goWest(map, sPipeType)
                    } else {
                        cur.goEast(map, sPipeType)
                    }

                    PipeType.NORTH_EAST -> if (prev.isNorthOf(cur)) {
                        cur.goEast(map, sPipeType)
                    } else if (prev.isEastOf(cur)) {
                        cur.goNorth(map, sPipeType)
                    } else {
                        null    // this pipe only goes north and east
                    }

                    PipeType.NORTH_WEST -> if (prev.isNorthOf(cur)) {
                        cur.goWest(map, sPipeType)
                    } else if (prev.isWestOf(cur)) {
                        cur.goNorth(map, sPipeType)
                    } else {
                        null    // this pipe only goes north and west
                    }

                    PipeType.SOUTH_EAST -> if (prev.isSouthOf(cur)) {
                        cur.goEast(map, sPipeType)
                    } else if (prev.isEastOf(cur)) {
                        cur.goSouth(map, sPipeType)
                    } else {
                        null    // this pipe only goes south and east
                    }

                    PipeType.SOUTH_WEST -> if (prev.isSouthOf(cur)) {
                        cur.goWest(map, sPipeType)
                    } else if (prev.isWestOf(cur)) {
                        cur.goSouth(map, sPipeType)
                    } else {
                        null    // this pipe only goes south and west
                    }

                    PipeType.NONE -> null   // cur isn't a pipe, this is a dead end
                    PipeType.START -> cur   // we found the start!

                } ?: return@associate sPipeType to emptyList<Node>()  // if null, this path is a dead end

                path.add(next)
                prev = cur
                cur = next

            } while (cur.pipe != PipeType.START)

            sPipeType to path
        }.filterNot {
            // empty paths don't go anywhere
            it.value.isEmpty()
        }

        // println("Found ${paths.size} paths through the maze:")
        // paths.forEach { println(it) }

        // the point farthest from the starting point must be exactly halfway through the loop
        return paths.maxOf { it.value.size } / 2
    }

    data class Node(
        val row: Int,
        val col: Int,
        val pipe: PipeType
    ) {
        // these return null if the pipe at the next coordinate is out of bounds, or if it doesn't accept from source direction
        // we need special handling if next is the start node, because we're guessing at the pipe type of that node
        fun goNorth(map: List<List<Node>>, startPipeType: PipeType): Node? {
            return map.getOrNull(this.row - 1)?.getOrNull(this.col)?.let {
                if (it.pipe.acceptsFromSouth() || (it.pipe == PipeType.START && startPipeType.acceptsFromSouth())) { it } else { null }
            }
        }
        fun goSouth(map: List<List<Node>>, startPipeType: PipeType): Node? {
            return map.getOrNull(this.row + 1)?.getOrNull(this.col)?.let {
                if (it.pipe.acceptsFromNorth() || (it.pipe == PipeType.START && startPipeType.acceptsFromNorth())) { it } else { null }
            }
        }
        fun goWest(map: List<List<Node>>, startPipeType: PipeType): Node? {
            return map.getOrNull(this.row)?.getOrNull(this.col - 1)?.let {
                if (it.pipe.acceptsFromEast() || (it.pipe == PipeType.START && startPipeType.acceptsFromEast())) { it } else { null }
            }
        }
        fun goEast(map: List<List<Node>>, startPipeType: PipeType): Node? {
            return map.getOrNull(this.row)?.getOrNull(this.col + 1)?.let {
                if (it.pipe.acceptsFromWest() || (it.pipe == PipeType.START && startPipeType.acceptsFromWest())) { it } else { null }
            }
        }
    }

    // if previous node is null, we're at the start so prefer to move in a clockwise direction (i.e. north or east)
    private fun Node?.isNorthOf(other: Node) = this?.let { this.row == other.row - 1 && this.col == other.col } ?: true
    private fun Node?.isEastOf(other: Node) = this?.let { this.row == other.row && this.col == other.col + 1 } ?: true
    private fun Node?.isSouthOf(other: Node) = this?.let { this.row == other.row + 1 && this.col == other.col } ?: true
    private fun Node?.isWestOf(other: Node) = this?.let { this.row == other.row && this.col == other.col - 1 } ?: true

    enum class PipeType(
        val char: Char
    ) {
        NORTH_SOUTH('|'),   // is a vertical pipe connecting north and south
        EAST_WEST('-'),     // is a horizontal pipe connecting east and west
        NORTH_EAST('L'),    // is a 90-degree bend connecting north and east
        NORTH_WEST('J'),    // is a 90-degree bend connecting north and west
        SOUTH_WEST('7'),    //is a 90-degree bend connecting south and west
        SOUTH_EAST('F'),    // is a 90-degree bend connecting south and east
        NONE('.'),          // is ground; there is no pipe in this tile,
        START('S');         // is the starting position of the animal

        fun acceptsFromSouth() = this == NORTH_SOUTH || this == SOUTH_WEST || this == SOUTH_EAST || this == NONE
        fun acceptsFromNorth() = this == NORTH_SOUTH || this == NORTH_EAST || this == NORTH_WEST || this == NONE
        fun acceptsFromEast() = this == EAST_WEST || this == NORTH_EAST || this == SOUTH_EAST || this == NONE
        fun acceptsFromWest() = this == EAST_WEST || this == NORTH_WEST || this == SOUTH_WEST || this == NONE
    }
}

fun main() {
    println(Part1().pipes(Utils.loadFromFile("Day10.txt")))
}