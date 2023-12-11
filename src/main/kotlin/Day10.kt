fun main() {
    val input = readInputAsText("Day10")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Int = findLoop(input).first.size / 2

private fun part2(input: String): Int {
    val (loop, w) = findLoop(input)
    val map = input.indices.map { i -> if (i in loop) input[i] else '.' }.joinToString("")
    return map.indices.count { i -> map[i] == '.' && map.drop(i).take(w - (i % w)).inter() % 2 == 1 }
}

private fun findLoop(input: String): Pair<Set<Int>, Int> {
    val w = input.indexOf('\n') + 1

    fun connected(i: Int) = when (input[i]) {
        '|' -> listOf(i - w, i + w)
        '-' -> listOf(i - 1, i + 1)
        'L' -> listOf(i - w, i + 1)
        'J' -> listOf(i - w, i - 1)
        '7' -> listOf(i + w, i - 1)
        'F' -> listOf(i + w, i + 1)
        else -> emptyList()
    }.filter { it in input.indices && ((it - i) in setOf(w, -w) || (i / w) == (it / w)) }

    val s = input.indexOf('S')
    val cur = listOf(s - 1, s + 1, s - w, s + w).find { p -> p in input.indices && s in connected(p) }!!
    return generateSequence(linkedSetOf(s, cur)) { if (it.addAll(connected(it.last))) it else null }.last() to w
}

private fun String.inter(): Int = "(\\||L(-)*7|F(-)*J)".toRegex().findAll(this).count()
