fun main() {
    val input = readInputAsText("Day19")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Int {
    val (pipelinesStr, partsStr) = input.split("\n\n")
    val pipelines = pipelinesStr.lines().map(::parsePipeline).associateBy { it.id }
    val parts = partsStr.lines().map(::parsePart)
    return parts.filter { accept(pipelines, it) }.sumOf { p -> p.x + p.m + p.a + p.s }
}

fun accept(pipelines: Map<String, Pipeline>, part: Part): Boolean {
    var pipeline: Pipeline = pipelines["in"]!!
    while (true) {
        val nextId = pipeline.rules.firstOrNull { it.predicate(part) }?.next
        if (nextId == "A") return true
        if (nextId == "R") return false
        pipeline = pipelines[nextId]!!
    }
}

fun parsePipeline(s: String): Pipeline {
    val id = s.substringBefore("{")
    val rules = s.substringAfter("{").dropLast(1).split(",").map { ruleStr ->
        if (":" in ruleStr) {
            val next = ruleStr.substringAfter(":")
            val const = ruleStr.substringBefore(":").drop(2).toInt()
            val f = ruleStr[0]
            val prop = if (f == 'x') Part::x else if (f == 'm') Part::m else if (f == 'a') Part::a else Part::s
            Rule(next) { part -> if (ruleStr[1] == '>') prop.get(part) > const else prop.get(part) < const }
        } else Rule(ruleStr) { true }
    }
    return Pipeline(id, rules)
}

fun parsePart(str: String): Part {
    val (x, m, a, s) = """\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}""".toRegex().matchEntire(str)!!.destructured
    return Part(x.toInt(), m.toInt(), a.toInt(), s.toInt())
}

data class Part(val x: Int, val m: Int, val a: Int, val s: Int)

data class Pipeline(val id: String, val rules: List<Rule>)

data class Rule(val next: String, val predicate: (Part) -> Boolean)

private fun part2(input: String): Int = 0
