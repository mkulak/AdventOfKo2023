fun main() {
    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int = findLoop(input).size / 2

private fun part2(input: List<String>): Int {
    val loop = findLoop(input)
    return input.indices.map { y ->
        input[y].indices.map { x -> if (XY(x, y) in loop) input[y][x] else '.' }.joinToString("")
    }.sumOf { line ->
        line.indices.count { line[it] == '.' && line.drop(it + 1).calcIntersections() % 2 == 1 }
    }
}

private fun findLoop(input: List<String>): Set<XY> {
    val sy = input.indexOfFirst { 'S' in it }
    val sx = input[sy].indexOf('S')
    val candidates = listOf(XY(sx - 1, sy), XY(sx + 1, sy), XY(sx, sy - 1), XY(sx, sy + 1))
        .filter { (x, y) -> y in input.indices && x in input[0].indices }
    val cur = candidates.find { XY(sx, sy) in it.connected(input) }!!
    return generateSequence(linkedSetOf(XY(sx, sy), cur)) { set ->
        if (set.addAll(set.last.connected(input))) set else null
    }.last()
}

fun XY.connected(input: List<String>): List<XY> = when (input[y][x]) {
    '|' -> listOf(XY(x, y - 1), XY(x, y + 1))
    '-' -> listOf(XY(x - 1, y), XY(x + 1, y))
    'L' -> listOf(XY(x, y - 1), XY(x + 1, y))
    'J' -> listOf(XY(x, y - 1), XY(x - 1, y))
    '7' -> listOf(XY(x, y + 1), XY(x - 1, y))
    'F' -> listOf(XY(x, y + 1), XY(x + 1, y))
    else -> emptyList()
}

private fun String.calcIntersections(): Int = "(\\||L(-)*7|F(-)*J)".toRegex().findAll(this).count()
