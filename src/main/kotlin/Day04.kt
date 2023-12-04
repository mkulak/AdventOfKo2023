import kotlin.math.pow

fun main() {
    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int = input.sumOf { line ->
    val (win, has) = parseCards(line)
    2.0.pow(has.count { it in win } - 1).toInt()
}

private fun part2(input: List<String>): Int {
    val points = input.map { line ->
        val (win, has) = parseCards(line)
        has.count { it in win }
    }
    val cards = IntArray(points.size) { 1 }
    for (i in cards.indices) {
        for (j in 1..points[i]) {
            cards[i + j] = cards[i + j] + cards[i]
        }
    }
    return cards.sum()
}

private fun parseCards(line: String) = line.replace("Card(\\s+)(\\d+):".toRegex(), "").split("|").map { part ->
    part.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }.toSet()
}

