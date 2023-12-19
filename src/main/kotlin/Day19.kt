import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInputAsText("Day19")
    println(part1(input)) // 425811
    println(part2(input)) // 131796824371749
}

private fun part1(input: String): Int {
    val (pipelinesStr, partsStr) = input.split("\n\n")
    val pipelines = pipelinesStr.lines().map(::parsePipeline).toMap()
    val parts = partsStr.lines().map(::parsePart)
    return parts.filter { pipelines.accepts("in", it) }.sumOf { it.values.sum() }
}

private fun part2(input: String): Long {
    val pipelines = input.substringBefore("\n\n").lines().map(::parsePipeline).toMap()
    return pipelines.countAccepted("xmas".associateWith { 1..4000 }, "in")
}

fun parsePipeline(s: String) = s.substringBefore("{") to s.substringAfter("{").dropLast(1).split(",").map { ruleStr ->
    if (":" in ruleStr) {
        val v = ruleStr.substringBefore(":").drop(2).toInt()
        Rule(ruleStr.substringAfter(":"), if (ruleStr[1] == '>') Greater(ruleStr[0], v) else Less(ruleStr[0], v))
    } else Rule(ruleStr, True)
}

fun parsePart(str: String): Part {
    val values = """\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}""".toRegex().matchEntire(str)!!.destructured.toList()
    return "xmas".toList().zip(values) { ch, value -> ch to value.toInt() }.toMap()
}

private tailrec fun Pipeline.accepts(id: String, part: Part): Boolean =
    when (id) {
        "A" -> true
        "R" -> false
        else -> accepts(getValue(id).first { it.predicate(part) }.next, part)
    }

private fun Pipeline.countAccepted(ranges: PartRanges, id: String): Long =
    when (id) {
        "A" -> ranges.values.fold(1) { acc, r -> acc * r.size() }
        "R" -> 0
        else -> getValue(id).fold(0L to ranges) { (count, lastRange), rule ->
            val (matched, nonMatched) = rule.predicate.split(lastRange)
            val inc = if (matched.size() > 0) countAccepted(matched, rule.next) else 0
            (count + inc) to nonMatched
        }.first
    }

data class Rule(val next: String, val predicate: PartPredicate)

sealed interface PartPredicate
data class Less(val field: Char, val value: Int) : PartPredicate
data class Greater(val field: Char, val value: Int) : PartPredicate
data object True : PartPredicate

operator fun PartPredicate.invoke(part: Part): Boolean = when (this) {
    is Less -> part.getValue(field) < value
    is Greater -> part.getValue(field) > value
    is True -> true
}

fun PartPredicate.split(ranges: PartRanges): Pair<PartRanges, PartRanges> = when (this) {
    is True -> ranges to "xmas".associateWith { 0..0 }
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

typealias Part = Map<Char, Int>
typealias PartRanges = Map<Char, IntRange>
typealias Pipeline = Map<String, List<Rule>>

private fun IntRange.size(): Int = last - first + 1
private fun PartRanges.size(): Int = values.sumOf { it.size() }


