import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInput("Day18")
    println(part1(input)) // 67891
    println(part2(input)) // 94116351948493
}

private fun part1(input: List<String>): Long {
    val steps = input.map { line ->
        Dir.entries.first { it.name[0] == line[0] } to line.drop(2).substringBefore(" ").toLong()
    }
    return solve(steps)
}

private fun part2(input: List<String>): Long {
    val steps = input.map {
        val hex = it.substringAfter('#').dropLast(1)
        val dir = listOf(Dir.Right, Dir.Down, Dir.Left, Dir.Up)[hex.last().code - '0'.code]
        dir to hex.dropLast(1).toLong(16)
    }
    return solve(steps)
}

private fun solve(steps: List<Pair<Dir, Long>>): Long {
    val edge = steps.fold(listOf(XYL(0, 0))) { acc, (dir, step) ->
        acc + (acc.last() + XYL(dir.dx * step, dir.dy * step))
    }
    val xs = edge.map { it.x }.toSet().sorted() + edge.maxOf { it.x + 1 }
    val ys = edge.map { it.y }.toSet().sorted() + edge.maxOf { it.y + 1 }
    val (vertical, horizontal) = edge.zipWithNext().partition { (a, b) -> a.x == b.x }

    fun isEdge(x: Long, y: Long) =
        vertical.any { it.first.x == x && y in it.ys } || horizontal.any { it.first.y == y && x in it.xs }

    fun isInside(x: Long, y: Long) =
        vertical.indices.count { i ->
            val v1 = vertical[i]
            val v2 = vertical[(i + 1) % vertical.size]
            x < v1.first.x && (y in v1.ys.exlusive || y == v1.second.y && v1.vdir == v2.vdir)
        } % 2 == 1

    return ys.zipWithNext().sumOf { (y1, y2) ->
        xs.zipWithNext().sumOf { (x1, x2) ->
            when {
                isEdge(x2 - 1, y2 - 1) || isInside(x2 - 1, y2 - 1) -> (x2 - x1) * (y2 - y1)
                isEdge(x1, y2 - 1) && isEdge(x2 - 1, y1) -> x2 - x1 + y2 - y1 - 1
                isEdge(x1, y2 - 1) -> y2 - y1
                isEdge(x2 - 1, y1) -> x2 - x1
                isEdge(x1, y1) -> 1
                else -> 0
            }
        }
    }
}

private typealias Segment = Pair<XYL, XYL>
private val Segment.vdir: Dir get() = if (first.y < second.y) Dir.Down else Dir.Up
private val Segment.xs: LongRange get() = min(first.x, second.x)..max(first.x, second.x)
private val Segment.ys: LongRange get() = min(first.y, second.y)..max(first.y, second.y)

private val LongRange.exlusive: LongRange get() = (first + 1)..(last - 1)

private data class XYL(val x: Long, val y: Long)
private operator fun XYL.plus(xy: XYL): XYL = XYL(x + xy.x, y + xy.y)

