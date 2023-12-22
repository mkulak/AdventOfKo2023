import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Long = parseGraph(input).findZ("AAA", input.first())

private fun part2(input: List<String>): Long {
    val steps = input.first()
    val graph = parseGraph(input)
    return graph.keys.filter { it.endsWith("A") }.map { graph.findZ(it, steps) }.reduce(::lcm)
}

private fun parseGraph(input: List<String>): Graph = input.drop(2).associate { line ->
    line.take(3) to (line.drop(7).take(3) to line.drop(12).take(3))
}

private tailrec fun Graph.findZ(cur: String, steps: String, pos: Long = 0): Long {
    if (cur.endsWith("Z")) return pos
    val (left, right) = getValue(cur)
    val instruction = steps[(pos % steps.length).toInt()]
    return findZ(if (instruction == 'L') left else right, steps, pos + 1)
}

fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

private tailrec fun gcd(a: Long, b: Long): Long {
    val min = min(a, b)
    val max = max(a, b)
    val rem = max % min
    return if (rem == 0L) min else gcd(rem, min)
}

typealias Graph = Map<String, Pair<String, String>>