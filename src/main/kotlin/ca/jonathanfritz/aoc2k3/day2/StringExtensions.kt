package ca.jonathanfritz.aoc2k3.day2

fun String.toGame(): Game {
    // parse each line into a game with an id and a list of rounds; each round has cube colour counts
    val parts = this.split(':',';').map { it.trim() }
    val id = parts[0].replace("Game ", "").trim().toInt()
    val rounds = parts.subList(1, parts.size).map { it.toRGB() }
    return Game(id, rounds)
}

fun String.toRGB(): RGB {
    val colours = this.split(',').map { it.trim() }
    return RGB(
        colours.extractColourCount("red"),
        colours.extractColourCount("blue"),
        colours.extractColourCount("green")
    )
}

fun List<String>.extractColourCount(colour: String) =
    this.firstOrNull { it.endsWith(colour) }?.split(' ')?.get(0)?.toInt() ?: 0