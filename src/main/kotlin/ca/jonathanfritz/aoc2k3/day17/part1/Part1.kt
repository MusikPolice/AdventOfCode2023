package ca.jonathanfritz.aoc2k3.day17.part1

import ca.jonathanfritz.aoc2k3.Utils
import kotlin.math.abs

class Part1 {
    fun path(lines: List<String>) {
        val map = lines.mapIndexed { row, line ->
            line.mapIndexed { col, char ->
                Node(row, col, char.digitToInt())
            }
        }
        val height = map.size
        val width = map[0].size
        val targetNode = map[height - 1][width - 1]

        // start in the top-left corner, doing a breadth first search for all valid paths
        val inProgress: MutableList<Path> = mutableListOf(Path(mutableListOf(map[0][0])))
        var shortestPath: Path? = null
        while (inProgress.isNotEmpty()) {
            val current = inProgress.removeAt(0)
            if (current.position == targetNode) {
                // if the lowest cost path has reached the end, store it in our set of complete paths
                // the first path that we find seems to be the shortest
                shortestPath = current
                break
            } else {
                // prune out any other paths that take a longer route to the current node
                inProgress.removeAll { it.position == current.position && it.cost > current.cost }

                // expand the current shortest path into all paths that could result from applying the rules to this position
                inProgress.addAll(current.move(map))
            }

            // bubble the lowest cost path up to the top
            // println("${inProgress.size} paths in progress, ${complete.size} complete")
            inProgress.reheapify(targetNode)
        }

        println("Shortest path with ${shortestPath?.nodes?.size} nodes and cost ${shortestPath?.cost} is ${shortestPath?.nodes}")
        map.map { row ->
            row.map { node ->
                if (shortestPath?.nodes?.contains(node) == true) {
                    print('x')
                } else {
                    print(node.cost)
                }
            }
            println()
        }
    }

    private fun MutableList<Path>.reheapify(target: Node) {
        this.sortBy { source ->
            // sort of an A* algorithm here where we use manhattan distance to target as a way to break cost ties
            source.cost + source.position.manhattanDistance(target)
        }
    }

    private data class Path (
        val nodes: List<Node>,                          // where we've been
        var orientation: Direction = Direction.EAST,    // where we're going
        var straightCount: Int = 0                      // how many consecutive times we've gone straight
    ) {
        val position = nodes.last()
        val cost = nodes.drop(1).sumOf { it.cost }  // cost ignores the first node (i.e. the top-left corner of map)

        // returns a list of all paths that could be derived from the current state
        fun move(map: List<List<Node>>): List<Path> {
            return Direction.values().associateWith { direction ->
                // all adjacent cells that we could move to
                when (direction) {
                    Direction.NORTH -> map.getOrNull(position.row - 1)?.getOrNull(position.col)
                    Direction.EAST -> map.getOrNull(position.row)?.getOrNull(position.col + 1)
                    Direction.SOUTH -> map.getOrNull(position.row + 1)?.getOrNull(position.col)
                    Direction.WEST -> map.getOrNull(position.row)?.getOrNull(position.col - 1)
                }
            }.filter { (_, node) ->
                // can't move to an out-of-bounds node
                node != null
            }.filterNot { (_, node) ->
                // can't move backwards or do loopity loops
                nodes.contains(node)
            }.filter { (direction, _) ->
                // after going straight on three consecutive moves, we have to turn either left or right
                if (straightCount >= 3) { direction != orientation} else true
            }.map { (direction, node) ->
                Path(nodes + node!!, direction, if (direction == orientation) straightCount + 1 else 0)
            }
        }
    }

    private data class Node (
        val row: Int,
        val col: Int,
        val cost: Int
    ) {
        fun manhattanDistance(other: Node) = abs(row - other.row) + abs(col - other.col)
    }

    private enum class Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }
}

fun main() {
    println(Part1().path(Utils.loadFromFile("Day17.txt")))
}