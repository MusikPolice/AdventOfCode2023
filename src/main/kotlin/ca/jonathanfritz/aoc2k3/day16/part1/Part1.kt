package ca.jonathanfritz.aoc2k3.day16.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {
    fun mirrors(lines: List<String>): Int {
        val contraption = lines.map { line -> line.toCharArray() }
        val height = contraption.size
        val width = contraption[0].size

        // beam enters the contraption in the north-west corner, coming from the west
        val queue: MutableList<Beam> = mutableListOf(Beam(0, 0, Direction.WEST))
        val energizedTiles: MutableSet<Tile> = mutableSetOf()

        while (queue.isNotEmpty()) {
            val beam = queue.removeAt(0)
            if (beam.destRow < 0 || beam.destRow >= height || beam.destCol < 0 || beam.destCol >= width) {
                // beam went out of bounds
                continue;
            }

            // a beam enters a tile, energizing it before passing on to the next tile
            energizedTiles.add(beam.toTile())
            when (contraption[beam.destRow][beam.destCol]) {
                // empty tile
                '.' -> queue.add(
                    when (beam.fromDir) {
                        Direction.NORTH -> beam.goSouth()
                        Direction.WEST -> beam.goEast()
                        Direction.SOUTH -> beam.goNorth()
                        Direction.EAST -> beam.goWest()
                    }
                )

                // vertical splitter
                '|' -> queue.addAll(
                    when (beam.fromDir) {
                        Direction.NORTH -> listOf(beam.goSouth())
                        Direction.WEST, Direction.EAST -> listOf(beam.goNorth(), beam.goSouth())
                        Direction.SOUTH -> listOf(beam.goNorth())
                    }
                )

                // horizontal splitter
                '-' -> queue.addAll(
                    when (beam.fromDir) {
                        Direction.NORTH, Direction.SOUTH -> listOf(beam.goEast(), beam.goWest())
                        Direction.WEST -> listOf(beam.goEast())
                        Direction.EAST -> listOf(beam.goWest())
                    }
                )

                // mirror
                '\\' -> queue.add(
                    when (beam.fromDir) {
                        Direction.NORTH -> beam.goEast()
                        Direction.WEST -> beam.goSouth()
                        Direction.SOUTH -> beam.goWest()
                        Direction.EAST -> beam.goNorth()
                    }
                )

                // other mirror
                '/' -> queue.add(
                    when (beam.fromDir) {
                        Direction.NORTH -> beam.goWest()
                        Direction.WEST -> beam.goNorth()
                        Direction.SOUTH -> beam.goEast()
                        Direction.EAST -> beam.goSouth()
                    }
                )
            }
        }

        (0 until height).map { row ->
            (0 until width).map { col ->
                if (contraption[row][col] != '.') {
                    print(contraption[row][col])
                } else {
                    if (energizedTiles.contains(Tile(row, col))) {
                        print('#')
                    } else {
                        print('.')
                    }
                }
                if (col == width - 1) {
                    println()
                }
            }
        }

        // energizedTiles is a set, so tiles with two or more beams passing through them are only counted once
        return energizedTiles.size
    }

    private data class Tile(
        val row: Int,
        val col: Int
    )

    private data class Beam(
        val destRow: Int,
        val destCol: Int,
        val fromDir: Direction
    ) {
        fun goNorth() = Beam(destRow - 1, destCol, Direction.SOUTH)
        fun goWest() = Beam(destRow, destCol - 1, Direction.EAST)
        fun goSouth() = Beam(destRow + 1, destCol, Direction.NORTH)
        fun goEast() = Beam(destRow, destCol + 1, Direction.WEST)

        fun toTile(): Tile = Tile(destRow, destCol)
    }

    private enum class Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
}

fun main() {
    println(Part1().mirrors(Utils.loadFromFile("Day16.txt")))
}