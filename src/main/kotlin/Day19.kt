import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInputAsText("Day19")
    println(part1(input)) // 425811
    println(part2(input))
}

private fun part1(input: String): Int {
    val (pipelinesStr, partsStr) = input.split("\n\n")
    val pipelines = pipelinesStr.lines().map(::parsePipeline).toMap()
    val parts = partsStr.lines().map(::parsePart)
    return parts.filter { pipelines.accept("in", it) }.sumOf { p -> p.x + p.m + p.a + p.s }
}

private tailrec fun Map<String, List<Rule>>.accept(id: String, part: Part): Boolean =
    when (val nextId = getValue(id).first { it.predicate(part) }.next) {
        "A" -> true
        "R" -> false
        else -> this@accept.accept(nextId, part)
    }

fun parsePipeline(s: String) = s.substringBefore("{") to s.substringAfter("{").dropLast(1).split(",").map { ruleStr ->
    if (":" in ruleStr) {
        val v = ruleStr.substringBefore(":").drop(2).toInt()
        Rule(ruleStr.substringAfter(":"), if (ruleStr[1] == '>') Greater(ruleStr[0], v) else Less(ruleStr[0], v))
    } else Rule(ruleStr, True)
}

fun parsePart(str: String): Part {
    val (x, m, a, s) = """\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}""".toRegex().matchEntire(str)!!.destructured
    return Part(x.toInt(), m.toInt(), a.toInt(), s.toInt())
}

data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    operator fun get(field: Char): Int = when (field) {
        'x' -> x
        'm' -> m
        'a' -> a
        else -> s
    }
}

data class Rule(val next: String, val predicate: PartPredicate)

sealed interface PartPredicate
data class Less(val field: Char, val value: Int) : PartPredicate
data class Greater(val field: Char, val value: Int) : PartPredicate
data object True : PartPredicate

operator fun PartPredicate.invoke(part: Part): Boolean = when (this) {
    is Less -> part.get(field) < value
    is Greater -> part.get(field) > value
    is True -> true
}

fun PartPredicate.split(ranges: PartRanges): Pair<PartRanges, PartRanges> = when (this) {
    is True -> ranges to empty
    is Less -> {
        val range = ranges.getValue(field)
        val less = range.first..min(range.last, value - 1)
        val more = max(range.first, value)..range.last
        val matched = ranges + (field to less)
        val nonMatched = ranges + (field to more)
        matched to nonMatched
    }
    is Greater -> {
        val range = ranges.getValue(field)
        val less = range.first..min(range.last, value)
        val more = max(range.first, value + 1)..range.last
        val matched = ranges + (field to more)
        val nonMatched = ranges + (field to less)
        matched to nonMatched
    }
}

typealias PartRanges = Map<Char, IntRange>

val empty: PartRanges = "xmas".associateWith { 0..0 }

private fun IntRange.size(): Int = last - first + 1
private fun PartRanges.size(): Int = values.sumOf { it.size() }

private fun part2(input: String): Long {
    val pipelines = input.substringBefore("\n\n").lines().map(::parsePipeline).toMap()
    //    println(pipelines)
    fun countAccepted(ranges: PartRanges, id: String): Long {
        if (id == "A") return ranges.values.fold(1) { acc, r -> acc * r.size() }
        if (id == "R") return 0
        val pipeline = pipelines.getValue(id)
        return pipeline.fold(0L to ranges) { (count, lastRange), rule ->
            val (matched, nonMatched) = rule.predicate.split(lastRange)
            val inc = if (matched.size() > 0) countAccepted(matched, rule.next) else 0
            (count + inc) to nonMatched
        }.first
    }
    return countAccepted("xmas".associateWith { 1..4000 }, "in")
}

