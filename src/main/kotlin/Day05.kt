import java.util.TreeSet
import kotlin.math.min

fun main() {
    val input = readInputAsText("Day05")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Long {
    val sections = input.split("\n\n")
    val seeds = sections[0].removePrefix("seeds: ").split(" ").map { Interval(it.toLong(), it.toLong() + 1, 0) }
    return solve(seeds, parseLayers(sections))
}

private fun part2(input: String): Long {
    val sections = input.split("\n\n")
    val seeds = sections[0].removePrefix("seeds: ").split(" ").map { it.toLong() }.chunked(2) { (start, size) ->
        Interval(start, start + size, 0)
    }
    return solve(seeds, parseLayers(sections))
}

private fun solve(seeds: List<Interval>, layers: List<IntervalTree>) =
    layers.fold(seeds) { cur, layer ->
        cur.flatMap { interval ->
            splitRange(interval, layer)
        }
    }.minOf { it.start }

fun splitRange(interval: Interval, layer: IntervalTree): List<Interval> {
    val res = ArrayList<Interval>()
    var cur = interval.copy(offset = 0)
    while (cur.start < cur.end) {
        val overlap = layer.findLeftmostOverlap(cur)
        cur = if (overlap == null) {
            res += cur
            cur.copy(start = interval.end)
        } else if (overlap.start <= cur.start) {
            val end = min(overlap.end, cur.end)
            res += Interval(cur.start + overlap.offset, end + overlap.offset, overlap.offset)
            cur.copy(start = end)
        } else {
            res += cur.copy(end = overlap.start)
            cur.copy(start = overlap.start)
        }
    }
    return res
}

private fun parseLayers(sections: List<String>) = sections.drop(1).map { section ->
    section.lines().drop(1).fold(IntervalTree()) { tree, line ->
        val (dst, src, size) = line.split(" ").map { it.toLong() }
        tree.add(Interval(src, src + size, dst - src))
        tree
    }
}

fun IntervalTree.findLeftmostOverlap(interval: Interval): Interval? {
    val left = floor(interval)
    if (left != null && interval.start < left.end) {
        return left
    }
    val right = ceiling(interval)
    if (right != null && interval.end > right.start) {
        return right
    }
    return null
}

data class Interval(val start: Long, val end: Long, val offset: Long) : Comparable<Interval> {
    override fun compareTo(other: Interval): Int = start.compareTo(other.start)
}

typealias IntervalTree = TreeSet<Interval>