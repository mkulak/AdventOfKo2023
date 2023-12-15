fun main() {
    val input = readInput("Day12")
    println(part1(input)) // 7732
    println(part2(input)) // 4500070301581
}
private fun part1(input: List<String>) = solve(input, 1)

private fun part2(input: List<String>) = solve(input, 5)

private fun solve(input: List<String>, mul: Int) = input.sumOf { line ->
    val (str, nums) = line.split(" ")
    val fullStr = List(mul) { str }.joinToString("?")
    val clues = nums.split(",").map { it.toInt() }.let { list -> (1..mul).flatMap { list } }
    solveRec(fullStr, clues, GroupState.NotStarted)
}

enum class GroupState { NotStarted, Pending, Ended }

val cache = HashMap<Triple<String, List<Int>, GroupState>, Long>()

fun solveRec(line: String, left: List<Int>, state: GroupState): Long {
    val key = Triple(line, left, state)
    val answer = cache[key]
    if (answer != null) return answer
    if (left.size * 2 - 1 > line.length) return 0
    if (left.isEmpty()) return if ("#" in line) 0 else 1
    if (line.isEmpty()) return 0
    val next = line.substring(1)
    val res = when (line.first()) {
        '.' -> {
            if (state == GroupState.Pending) 0
            else solveRec(next, left, GroupState.NotStarted)
        }
        '#' -> {
            if (state == GroupState.Ended) 0
            else if (left[0] > 1) solveRec(next, left.decHead(), GroupState.Pending)
            else /*left[0] == 1*/ solveRec(next, left.drop(1), GroupState.Ended)
        }
        else -> {
            if (state == GroupState.Pending) {
                if (left[0] > 1) solveRec(next, left.decHead(), GroupState.Pending)
                else /*left[0] == 1*/ solveRec(next, left.drop(1), GroupState.Ended)
            } else if (state == GroupState.Ended) solveRec(next, left, GroupState.NotStarted)
            else /*state == GroupState.NotStarted*/ solveRec(next, left, GroupState.NotStarted) +
                if (left[0] > 1) solveRec(next, left.decHead(), GroupState.Pending)
                else solveRec(next, left.drop(1), GroupState.Ended)
        }
    }
    cache[key] = res
    return res
}

private fun List<Int>.decHead(): List<Int> = List(size) { this[it] - if (it == 0) 1 else 0 }