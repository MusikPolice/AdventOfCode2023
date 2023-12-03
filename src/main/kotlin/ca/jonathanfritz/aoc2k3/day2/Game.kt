package ca.jonathanfritz.aoc2k3.day2

data class Game (
    val id: Int,
    val rounds: List<RGB>
) {
    fun maxCubeColours(): RGB = RGB(
        rounds.maxOf { it.red },
        rounds.maxOf { it.green },
        rounds.maxOf { it.blue }
    )
}