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

private fun solve(seeds: List<Interval>, layers: List<Layer>) =
    layers.fold(seeds) { intervals, layer -> intervals.flatMap(layer::split) }.minOf { it.start }

fun Layer.split(interval: Interval): List<Interval> {
    val res = ArrayList<Interval>()
    var cur = interval.copy(offset = 0)
    while (cur.start < cur.end) {
        val overlap = find { it.start in cur || cur.start in it }
        cur = if (overlap == null) {
            res += cur
            cur.copy(start = interval.end)
        } else if (overlap > cur) {
            res += cur.copy(end = overlap.start)
            cur.copy(start = overlap.start)
        } else {
            val end = min(overlap.end, cur.end)
            res += Interval(cur.start + overlap.offset, end + overlap.offset, overlap.offset)
            cur.copy(start = end)
        }
    }
    return res
}

private fun parseLayers(sections: List<String>): List<Layer> = sections.drop(1).map { section ->
    section.lines().drop(1).map { line ->
        val (dst, src, size) = line.split(" ").map { it.toLong() }
        Interval(src, src + size, dst - src)
    }.sorted()
}

data class Interval(val start: Long, val end: Long, val offset: Long) : Comparable<Interval> {
    override fun compareTo(other: Interval): Int = start.compareTo(other.start)
    operator fun contains(value: Long): Boolean = value in start..<end
}

typealias Layer = List<Interval>