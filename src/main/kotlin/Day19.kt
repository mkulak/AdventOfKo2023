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
        val next = ruleStr.substringAfter(":")
        val const = ruleStr.substringBefore(":").drop(2).toInt()
        val f = ruleStr[0]
        val prop = if (f == 'x') Part::x else if (f == 'm') Part::m else if (f == 'a') Part::a else Part::s
        Rule(next) { part -> if (ruleStr[1] == '>') prop.get(part) > const else prop.get(part) < const }
    } else Rule(ruleStr) { true }
}

fun parsePart(str: String): Part {
    val (x, m, a, s) = """\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}""".toRegex().matchEntire(str)!!.destructured
    return Part(x.toInt(), m.toInt(), a.toInt(), s.toInt())
}

data class Part(val x: Int, val m: Int, val a: Int, val s: Int)

data class Rule(val next: String, val predicate: (Part) -> Boolean)

private fun part2(input: String): Int {
    val pipelines = input.substringBefore("\n\n").lines().map(::parsePipeline).toMap()
//    println(pipelines)
    return 0
}
