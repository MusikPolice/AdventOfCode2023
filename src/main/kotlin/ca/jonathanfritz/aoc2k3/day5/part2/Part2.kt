package ca.jonathanfritz.aoc2k3.day5.part2

import ca.jonathanfritz.aoc2k3.Utils

class Part2 {
    fun almanac(lines: List<String>): Long? {
        // seeds are now expressed in ranges
        // in each pair of long integers, the first element is the start of the range and the second is the length
        // we can iterate over those pairs to turn them back into a list of seed numbers
        val seedRanges = lines[0]
            .substring(7)
            .split(' ')
            .map { it.trim().toLong() }
            .chunked(2)
            .map { Range(it[0], it[0] + it[1]) }

        val seedToSoilMap = lines.parseRangeMap("seed-to-soil")
        val soilToFertilizerMap = lines.parseRangeMap("soil-to-fertilizer")
        val fertilizerToWaterMap = lines.parseRangeMap("fertilizer-to-water")
        val waterToLightMap = lines.parseRangeMap("water-to-light")
        val lightToTemperatureMap = lines.parseRangeMap("light-to-temperature")
        val temperatureToHumidityMap = lines.parseRangeMap("temperature-to-humidity")
        val humidityToLocationMap = lines.parseRangeMap("humidity-to-location")

        // chain conversion of each seed number through all the ranges
        // we want the lowest location number
        var minLocation = Long.MAX_VALUE
        seedRanges.forEach{ range ->
            // TODO: this approach avoids the out of memory exception, but the runtime is shit
            //  maybe cache results to take advantage of overlapping seed ranges?
            //  or reduce the pipeline into a single conversion if possible and cache that?
            println("Computing min location for range $range")
            val location = (range.start .. range.end).minOf {seed ->
                val soil = seedToSoilMap.map(seed)
                val fertilizer = soilToFertilizerMap.map(soil)
                val water = fertilizerToWaterMap.map(fertilizer)
                val light = waterToLightMap.map(water)
                val temperature = lightToTemperatureMap.map(light)
                val humidity = temperatureToHumidityMap.map(temperature)
                val location = humidityToLocationMap.map(humidity)
                location
            }
            if (location < minLocation) {
                minLocation = location
                println("Updated minLocation to $minLocation")
            }
        }

        // TODO: looks like the wrong answer is returned. Prev output:
        //  Computing min location for range Range(start=1636419363, end=2245243552)
        //  Updated minLocation to 262293103
        //  Computing min location for range Range(start=3409451394, end=3636923144)
        //  Computing min location for range Range(start=12950548, end=104417251)
        //  Computing min location for range Range(start=1003260108, end=1228133811)
        //  Computing min location for range Range(start=440703838, end=631952315)
        //  Updated minLocation to 54576670
        //  Computing min location for range Range(start=634347552, end=909612057)
        //  Computing min location for range Range(start=3673953799, end=3741793473)
        //  Computing min location for range Range(start=2442763622, end=2679835231)
        //  Computing min location for range Range(start=3766524590, end=4192869421)
        //  Updated minLocation to 50716417
        //  Computing min location for range Range(start=1433781343, end=1587503765)
        //  50716417
        return minLocation
    }

    private fun List<String>.parseRangeMap(identifier: String): Map<Range, Range> {
        // line breaks denote the end of each input map
        val breakPositions = this.mapIndexed { i, line -> i to line.isBlank() }
            .filter { (_, isBlank) -> isBlank }
            .map { (i, _) -> i }

        // loop over the lines that comprise the target map
        val start = this.indexOfFirst { it.startsWith(identifier) }
        return (start + 1 until (breakPositions.firstOrNull { it > start } ?: this.size)).associate { lineNum ->
            val tokens = this[lineNum].split(' ').map { it.trim().toLong() }
            val sourceRange = Range(tokens[1], tokens[1] + tokens[2])
            val destinationRange = Range(tokens[0], tokens[0] + tokens[2])
            sourceRange to destinationRange
        }
    }

    private data class Range (
        val start: Long,
        val end: Long
    ) {
        fun contains(value: Long): Boolean = value in start..end
    }

    private fun Map<Range, Range>.map(value: Long): Long {
        return this.keys.firstOrNull { sourceRange -> sourceRange.contains(value) }?.let { sourceRange ->
            val destinationRange = this[sourceRange]!!     // a destination exists for every source
            val delta = value - sourceRange.start
            destinationRange.start + delta
        } ?: value  // if the provided value does not exist in any of the source ranges, it maps to itself
    }
}

fun main() {
    println(Part2().almanac(Utils.loadFromFile("Day5.txt")))
}