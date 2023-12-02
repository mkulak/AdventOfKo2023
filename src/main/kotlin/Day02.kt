import kotlin.math.max

fun main() {
    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int = input.mapIndexed { i, line ->
    val tries = line.drop(line.indexOf(":") + 1).split(";").map { sub ->
        val balls = IntArray(3)
        sub.split(",").map { part ->
            val spacePos = part.indexOf(" ", 1)
            val index = when (part[spacePos + 1]) {
                'r' -> 0
                'g' -> 1
                else -> 2
            }
            balls[index] = part.substring(1, spacePos).toInt()
        }
        balls
    }
    if (tries.all { it[0] < 13 && it[1] < 14 && it[2] < 15 }) (i + 1) else 0
}.sum()

private fun part2(input: List<String>): Int = input.sumOf { line ->
    val balls = IntArray(3)
    line.drop(line.indexOf(":") + 1).split(";").map { sub ->
        sub.split(",").map { part ->
            val spacePos = part.indexOf(" ", 1)
            val value = part.substring(1, spacePos).toInt()
            val index = when (part[spacePos + 1]) {
                'r' -> 0
                'g' -> 1
                else -> 2
            }
            balls[index] = max(balls[index], value)
        }
    }
    balls[0] * balls[1] * balls[2]
}
