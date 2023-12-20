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
        val isLess = ruleStr[1] == '<'
        val v = ruleStr.substringBefore(":").drop(2).toInt() + if (isLess) 0 else 1
        Rule(ruleStr.substringAfter(":"), ruleStr[0], v, isLess)
    } else Rule(ruleStr, 'x', 0, false)
}

fun parsePart(str: String): Part {
    val values = """\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}""".toRegex().matchEntire(str)!!.destructured.toList()
    return "xmas".toList().zip(values) { ch, value -> ch to value.toInt() }.toMap()
}

private tailrec fun Pipeline.accepts(id: String, part: Part): Boolean =
    when (id) {
        "A" -> true
        "R" -> false
        else -> accepts(getValue(id).first { it.matches(part) }.next, part)
    }

private fun Pipeline.countAccepted(ranges: PartRanges, id: String): Long =
    when (id) {
        "A" -> ranges.values.fold(1) { acc, range -> acc * range.size() }
        "R" -> 0
        else -> getValue(id).fold(0L to ranges) { (count, lastRange), rule ->
            val (matched, nonMatched) = rule.split(lastRange)
            val inc = if (matched.empty()) 0 else countAccepted(matched, rule.next)
            (count + inc) to nonMatched
        }.first
    }

data class Rule(val next: String, val field: Char, val value: Int, val isLess: Boolean)

fun Rule.matches(part: Part): Boolean =
    if (isLess) part.getValue(field) < value else part.getValue(field) >= value

fun Rule.split(ranges: PartRanges): Pair<PartRanges, PartRanges> {
    val range = ranges.getValue(field)
    val less = range.first..range.last.coerceAtMost(value - 1)
    val more = range.first.coerceAtLeast(value)..range.last
    val rangesWithLess = ranges + (field to less)
    val rangesWithMore = ranges + (field to more)
    return if (isLess) rangesWithLess to rangesWithMore else rangesWithMore to rangesWithLess
}

private fun IntRange.size(): Int = last - first + 1
private fun PartRanges.empty(): Boolean = values.all { it.size() == 0 }

typealias Part = Map<Char, Int>
typealias PartRanges = Map<Char, IntRange>
typealias Pipeline = Map<String, List<Rule>>
