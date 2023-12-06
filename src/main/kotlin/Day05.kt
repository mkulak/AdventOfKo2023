import java.util.TreeSet
import kotlin.math.min

fun main() {
    val input = readInputAsText("Day05")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Long {
    val sections = input.split("\n\n")
    val seeds = sections[0].removePrefix("seeds: ").split(" ").map { it.toLong() }
    val layers = parseMappingLayers(sections)
    return seeds.map { findLocation(it, layers) }.min()
}

private fun parseMappingLayers(sections: List<String>): List<List<Mapping>> = sections.drop(1).map { section ->
    section.lines().drop(1).map { line ->
        val (dst, src, size) = line.split(" ").map { it.toLong() }
        Mapping(src, dst, size)
    }
}

private fun findLocation(seed: Long, layers: List<List<Mapping>>): Long = layers.fold(seed) { acc, layer ->
    acc + (layer.find { acc >= it.src && acc < (it.src + it.size) }?.let { it.dst - it.src } ?: 0)
}

data class Mapping(val src: Long, val dst: Long, val size: Long)


private fun part2(input: String): Long {
    val sections = input.split("\n\n")
    val seeds = sections[0].removePrefix("seeds: ").split(" ").map { it.toLong() }.chunked(2) { (start, size) ->
        Interval(start, start + size, 0)
    }
    val initial = IntervalTree()
    seeds.forEach(initial::add)
    val layers = parseLayers(sections)
    return layers.fold(initial) { cur, layer ->
        val res = IntervalTree()
        cur.data.forEach { interval -> splitRange(interval, layer, res) }
        res
    }.data.first.start
}

fun splitRange(interval: Interval, layer: IntervalTree, out: IntervalTree) {
    var cur = interval.copy(offset = 0)
    while (cur.start < cur.end) {
        val overlap = layer.findLeftmostOverlap(cur)
        cur = if (overlap == null) {
            out.add(cur)
            Interval(interval.end,  interval.end, 0)
        } else if (overlap.start <= cur.start) {
            val end = min(overlap.end, cur.end)
            out.add(Interval(cur.start + overlap.offset, end + overlap.offset, overlap.offset))
            Interval(end, interval.end, 0)
        } else {
            out.add(Interval(cur.start, overlap.start, 0))
            Interval(overlap.start, cur.end, 0)
        }
    }
}

private fun parseLayers(sections: List<String>): List<IntervalTree> = sections.drop(1).map { section ->
    section.lines().drop(1).fold(IntervalTree()) { tree, line ->
        val (dst, src, size) = line.split(" ").map { it.toLong() }
        tree.add(Interval(src, src + size, dst - src))
        tree
    }
}

class IntervalTree {
    val data = TreeSet<Interval>()

    fun add(interval: Interval) {
        data += interval
    }

    fun findLeftmostOverlap(interval: Interval): Interval? {
        val left = data.floor(interval)
        if (left != null && interval.start < left.end) {
            return left
        }
        val right = data.ceiling(interval)
        if (right != null && interval.end > right.start) {
            return right
        }
        return null
    }
}

data class Interval(val start: Long, val end: Long, val offset: Long) : Comparable<Interval> {
    override fun compareTo(other: Interval): Int = start.compareTo(other.start)
}
