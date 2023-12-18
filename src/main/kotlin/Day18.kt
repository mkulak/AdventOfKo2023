import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInput("Day18")
    println(part1(input)) //
    println(part2(input)) //
}
//67983-92 = 67891
private fun part1(input: List<String>): Int {
    val steps = input.map { parseDir(it.first()) to it.drop(2).substringBefore(" ").toInt() }
    val verticles = steps.fold(mutableListOf(XY(0, 0))) { acc, (dir, step) ->
        acc.add(acc.last() + XY(dir.dx * step, dir.dy * step))
        acc
    }
    println(verticles)
    println(verticles.size)
    val maxX = verticles.maxOf { it.x }
    val maxY = verticles.maxOf { it.y }
    val maxBounds = XY(maxX + 1, maxY + 1)
    val minX = verticles.minOf { it.x }
    val minY = verticles.minOf { it.y }
    val minBounds = XY(minX + 1, minY + 1)
    println("$minBounds $maxBounds")
    val adjMaxX = maxX - minX
    val adjMaxY = maxY - minY

    val (vertical, horizontal) = verticles.map { (x, y) -> XY(x - minX, y - minY) }.zipWithNext().partition { (a ,b) -> a.x == b.x }


    val verticlesSet = (0..adjMaxY).flatMap { y ->
        (0..adjMaxX).filter { x ->
            horizontal.any { (a, b) -> a.y == y && x.isBetweenInclusive(a.x, b.x) } ||
                vertical.any { (a, b) -> x == a.x && y.isBetweenInclusive(a.y, b.y) }
        }.map { XY(it, y) }
    }.toSet()
    val insideSet = (0..adjMaxY).flatMap { y ->
        (0..adjMaxX).filter { x ->
//            (x + 1)..()
//            verticlesSet.count { (a, b) -> a.x }
//            horizontal.any { (a, b) -> a.y == y && x.isBetweenInclusive(a.x, b.x) } ||
            XY(x, y) !in verticlesSet && 
            vertical.indices.count { i ->
                val v1 = vertical[i]
                val v2 = if (i < vertical.lastIndex) vertical[i + 1] else null
                x < v1.first.x && (y.isBetweenExclusive(v1.first.y, v1.second.y) || y == v1.second.y && v1.vdir() == v2?.vdir())
            } % 2 == 1
//                vertical.count { (a, b) ->  && y.isBetweenExclusive(a.y, b.y) }
        }.map { XY(it, y) }
    }.toSet()                        //############ #################################################################################### ....
    debugPrint(adjMaxX, adjMaxY, verticlesSet, insideSet)
//    return (0..maxY).sumOf { y ->
//        (0..maxX).count { x ->
//            horizontal.any { (a, b) -> a.y == y && x.isBetweenInclusive(a.x, b.x) } ||
//            vertical.count { (a, b) -> x < a.x && y.isBetweenExclusive(a.y, b.y) } % 2 == 1
//        }
//    }
//    return steps.sumOf { it.second }
    return verticlesSet.size + insideSet.size
}

private fun Pair<XY, XY>.vdir(): Dir = if (first.y < second.y) Dir.Down else Dir.Up

//[2, 7]
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

fun Int.isBetweenInclusive(a: Int, b: Int) = this in min(a, b)..max(a, b)
fun Int.isBetweenExclusive(a: Int, b: Int) = this in min(a + 1, b + 1)..max(a - 1, b - 1)
fun Int.isBetweenEndExclusive(a: Int, b: Int) = this in min(a, b)..<max(a, b)


private fun part2(input: List<String>): Int = 0

private fun parseDir(ch: Char): Dir = Dir.entries.first { it.name.first() == ch }
