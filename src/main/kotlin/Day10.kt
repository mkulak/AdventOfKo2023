fun main() {
    val input = readInputAsText("Day10")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Int = findLoop(input).first.size / 2

private fun part2(input: String): Int {
    val (loop, w) = findLoop(input)
    val modified = input.indices.map { i -> if (i in loop) input[i] else '.' }.joinToString("")
    return modified.indices.count { i ->
        modified[i] == '.' && modified.substring(i + 1, ((i + 1) / w + 1) * w - 1).calcIntersections() % 2 == 1
    }
}

private fun findLoop(input: String): Pair<Set<Int>, Int> {
    val w = input.indexOf('\n') + 1
    val start = input.indexOf('S')
    val cur = listOf(start - 1, start + 1, start - w, start + w)
        .find { p -> p in input.indices && start in connected(input, p, w) }!!
    return generateSequence(linkedSetOf(start, cur)) { set ->
        if (set.addAll(connected(input, set.last, w))) set else null
    }.last() to w
}

fun connected(input: String, i: Int, w: Int): List<Int> = when (input[i]) {
    '|' -> listOf(i - w, i + w)
    '-' -> listOf(i - 1, i + 1)
    'L' -> listOf(i - w, i + 1)
    'J' -> listOf(i - w, i - 1)
    '7' -> listOf(i + w, i - 1)
    'F' -> listOf(i + w, i + 1)
    else -> emptyList()
}.filter { it in input.indices && ((it - i) in setOf(w, -w) || (i / w) == (it / w)) }

private fun String.calcIntersections(): Int = "(\\||L(-)*7|F(-)*J)".toRegex().findAll(this).count()
