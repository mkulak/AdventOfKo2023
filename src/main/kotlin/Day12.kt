fun main() {
    val input = readInput("Day12")
    println(part1(input)) // 7732
    println(part2(input)) // 4500070301581
}

private fun part1(input: List<String>) = solve(input, 1)

private fun part2(input: List<String>) = solve(input, 5)

private fun solve(input: List<String>, mul: Int) = input.sumOf { line ->
    val (pattern, nums) = line.split(" ")
    val str = List(mul) { pattern }.joinToString("?")
    val clues = nums.split(",").map { it.toInt() }.let { list -> (1..mul).flatMap { list } }
    cache.clear()
    solveRec(str, 0, clues, State.NotStarted)
}

private enum class State { NotStarted, Pending, Ended }

private val cache = HashMap<Triple<Int, List<Int>, State>, Long>()

private fun solveRec(s: String, i: Int, left: List<Int>, state: State): Long = cache.getOrPut(Triple(i, left, state)) {
    when {
        left.isEmpty() -> if (s.indexOf('#', i) == -1) 1 else 0
        i == s.length -> 0
        s[i] == '.' && state == State.Pending -> 0
        s[i] == '.' -> solveRec(s, i + 1, left, State.NotStarted)
        s[i] == '#' && state == State.Ended -> 0
        s[i] == '#' && left[0] > 1 -> solveRec(s, i + 1, left.decHead(), State.Pending)
        s[i] == '#' -> solveRec(s, i + 1, left.drop(1), State.Ended)
        state == State.Pending && left[0] > 1 -> solveRec(s, i + 1, left.decHead(), State.Pending)
        state == State.Pending -> solveRec(s, i + 1, left.drop(1), State.Ended)
        state == State.Ended -> solveRec(s, i + 1, left, State.NotStarted)
        left[0] > 1 -> solveRec(s, i + 1, left, State.NotStarted) + solveRec(s, i + 1, left.decHead(), State.Pending)
        else -> solveRec(s, i + 1, left, State.NotStarted) + solveRec(s, i + 1, left.drop(1), State.Ended)
    }
}

private fun List<Int>.decHead(): List<Int> = List(size) { this[it] - if (it == 0) 1 else 0 }