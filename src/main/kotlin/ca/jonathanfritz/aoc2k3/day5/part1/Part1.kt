package ca.jonathanfritz.aoc2k3.day5.part1

import ca.jonathanfritz.aoc2k3.Utils

class Part1 {
    fun almanac(lines: List<String>): Long {
        // parse the input
        val seeds = lines[0].substring(7).split(' ').map { it.trim().toLong() }

        val seedToSoilMap = lines.parseRangeMap("seed-to-soil")
        val soilToFertilizerMap = lines.parseRangeMap("soil-to-fertilizer")
        val fertilizerToWaterMap = lines.parseRangeMap("fertilizer-to-water")
        val waterToLightMap = lines.parseRangeMap("water-to-light")
        val lightToTemperatureMap = lines.parseRangeMap("light-to-temperature")
        val temperatureToHumidityMap = lines.parseRangeMap("temperature-to-humidity")
        val humidityToLocationMap = lines.parseRangeMap("humidity-to-location")

        // chain conversion of each seed number through all the ranges
        val seedLocations = seeds.associateWith { seed ->
            val soil = seedToSoilMap.map(seed)
            val fertilizer = soilToFertilizerMap.map(soil)
            val water = fertilizerToWaterMap.map(fertilizer)
            val light = waterToLightMap.map(water)
            val temperature = lightToTemperatureMap.map(light)
            val humidity = temperatureToHumidityMap.map(temperature)
            val location = humidityToLocationMap.map(humidity)
            location
        }

        // we want the lowest location number
        return seedLocations.values.min()
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
        } ?: value
    }
}

fun main() {
    println(Part1().almanac(Utils.loadFromFile("Day5.txt")))
}