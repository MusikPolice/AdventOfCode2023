package ca.jonathanfritz.aoc2k3.day4

fun String.toNumberSet(): Set<Int> = this.split(' ')
    .map { it.trim() }
    .filter { it.isNotEmpty() }
    .map { it.toInt() }
    .toSet()