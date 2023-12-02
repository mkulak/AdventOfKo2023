import kotlin.math.max

fun main() {
    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int = input.mapIndexed { i, line ->
    if (parseRounds(line).all { (r, g, b) -> r < 13 && g < 14 && b < 15 }) (i + 1) else 0
}.sum()

private fun part2(input: List<String>): Int = input.sumOf { line ->
    val balls = IntArray(3)
    parseRounds(line).forEach { (r, g, b) ->
        balls[0] = max(balls[0], r)
        balls[1] = max(balls[1], g)
        balls[2] = max(balls[2], b)
    }
    balls[0] * balls[1] * balls[2]
}

private fun parseRounds(line: String): List<IntArray> = line.drop(line.indexOf(":") + 1).split(";").map(::parseRound)

private fun parseRound(round: String): IntArray {
    val balls = IntArray(3)
    round.split(",").forEach { part ->
        val (value, color) = part.drop(1).split(" ")
        balls[color2index(color)] = value.toInt()
    }
    return balls
}

private fun color2index(color: String): Int = when (color) {
    "red" -> 0
    "green" -> 1
    else -> 2
}
