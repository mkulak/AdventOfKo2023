import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInput("Day18")
    println(part1(input)) // 67891
    println(part2(input)) //
}

private fun part1(input: List<String>): Int {
    val steps = input.map { parseDir(it.first()) to it.drop(2).substringBefore(" ").toInt() }
    val verticles = steps.fold(mutableListOf(XY(0, 0))) { acc, (dir, step) ->
        acc.add(acc.last() + XY(dir.dx * step, dir.dy * step))
        acc
    }
    val maxX = verticles.maxOf { it.x }
    val maxY = verticles.maxOf { it.y }
    val minX = verticles.minOf { it.x }
    val minY = verticles.minOf { it.y }
    val adjMaxX = maxX - minX
    val adjMaxY = maxY - minY

    println(verticles.size)
    println("${XY(minX, minY)} ${XY(maxX, maxY)}")
    val adjVerticles = verticles.map { (x, y) -> XY(x - minX, y - minY) }
    println(adjVerticles)

    val (vertical, horizontal) = adjVerticles.zipWithNext().partition { (a, b) -> a.x == b.x }

    val verticlesSet = (0..adjMaxY).flatMap { y ->
        (0..adjMaxX).filter { x ->
            horizontal.any { (a, b) -> a.y == y && x.isBetweenInclusive(a.x, b.x) } ||
                vertical.any { (a, b) -> x == a.x && y.isBetweenInclusive(a.y, b.y) }
        }.map { XY(it, y) }
    }.toSet()

    val insideSet = (0..adjMaxY).flatMap { y ->
        (0..adjMaxX).filter { x ->
            val fit = XY(x, y) !in verticlesSet && vertical.indices.count { i ->
                val v = vertical[i]
                val vNext = if (i < vertical.lastIndex) vertical[i + 1] else vertical[0]
                x < v.first.x
                    && (y.isBetweenExclusive(v.first.y, v.second.y) || (y == v.second.y && v.vdir() == vNext.vdir()))
            } % 2 == 1
            if (fit && x == 0) {
                println("Fit: ($x, $y) verticals: ${vertical.filter { y.isBetweenInclusive(it.first.y, it.second.y) }}")
            }
            fit
        }.map { XY(it, y) }
    }.toSet()
    debugPrint(adjMaxX, adjMaxY, verticlesSet, insideSet)
    return verticlesSet.size + insideSet.size
}

fun debugPrint(maxX: Int, maxY: Int, verticlesSet: Set<XY>, insideSet: Set<XY>) {
    (0..maxY).forEach { y ->
        (0..maxX).forEach { x ->
            val edge = XY(x, y) in verticlesSet
            val inside = XY(x, y) in insideSet
            val color = when {
                edge && inside -> ANSI_PURPLE
                edge -> ANSI_RED
                inside -> ANSI_GREEN
                else -> ANSI_BLACK
            }
            val ch = if (edge || inside) '#' else "."
            print(color + ch + ANSI_RESET)
        }
        println()
    }
}

private fun Pair<XY, XY>.vdir(): Dir = if (first.y < second.y) Dir.Down else Dir.Up

fun Int.isBetweenInclusive(a: Int, b: Int) = this in min(a, b)..max(a, b)
fun Int.isBetweenExclusive(a: Int, b: Int) = this in min(a + 1, b + 1)..max(a - 1, b - 1)
fun Int.isBetweenEndExclusive(a: Int, b: Int) = this in min(a, b)..<max(a, b)

private fun parseDir(ch: Char): Dir = Dir.entries.first { it.name.first() == ch }

private fun part2(input: List<String>): Int = 0
