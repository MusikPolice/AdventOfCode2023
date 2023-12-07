package ca.jonathanfritz.aoc2k3.day6.part2

import ca.jonathanfritz.aoc2k3.Utils

class Part2 {
    fun boatRace(lines: List<String>): Int {
        val time = 10L //lines[0].tabSeparatedValuesToLong()
        val record = 26L // lines[1].tabSeparatedValuesToLong()

        // TODO: the idea here is to binary search the left bound and the right bound of hold time
        //  when both are found, subtract left from right to get a count of valid solutions
        //  sadly, the binary search below is not working and is returning negative values :(

        // race time is 71530
        // best distance is 940200
        // if our distance is 950,000 then delta is ~10,000 meaning we overshot the left
        val left = (1 until time).toList().binarySearch { holdTime ->
            val distance = holdTime * (time - holdTime)
            val delta = record - distance
            return@binarySearch if (delta == 0L) {
                // we've found a solution
                // if the previous hold time is less than the record, then our solution is the lowest possible hold time
                val prevDistance = (holdTime - 1) * (time - (holdTime - 1))
                val prevDelta = record - prevDistance
                if (prevDelta < 0 ) {
                    0
                } else {
                    -1
                }
            } else if (delta < 0) {
                // our distance is worse than the record, so we undershot the lowest possible hold time that is a solution
                -1
            } else {
                // our distance is better than the record, so we overshot the lowest possible hold time that is a solution
                1
            }
        }
        println(left)

        // TODO: another idea is that distance creates a parabola - if we can find where it intersects with the record
        //  distance, we know how many solutions are in between
        
        return 0
    }
}

fun main() {
    println(Part2().boatRace(Utils.loadFromFile("Day6.txt")))
}