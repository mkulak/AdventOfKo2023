fun main() {
    val input = readInput("Day12")
    println(part1(input)) // 7732
    println(part2(input)) // 4500070301581
}

private fun part1(input: List<String>) = solve(input, 1)

private fun part2(input: List<String>) = solve(input, 5)

private fun solve(input: List<String>, mul: Int) = input.sumOf { line ->
    val (pattern, nums) = line.split(" ")
    val s = List(mul) { pattern }.joinToString("?")
    val clues = nums.split(",").map { it.toInt() }.let { list -> (1..mul).flatMap { list } }
    val cache = HashMap<Pair<Int, List<Int>>, Long>()
    fun solveRec(i: Int, left: List<Int>): Long = cache.getOrPut(i to left) {
        when {
            left.isEmpty() || left == listOf(0) -> if (s.indexOf('#', i) == -1) 1 else 0
            i == s.length -> 0
            s[i] == '.' && left[0] == 0 -> solveRec(i + 1, left.drop(1))
            s[i] == '.' && left[0] < clues[clues.size - left.size] -> 0
            s[i] == '.' -> solveRec(i + 1, left)
            s[i] == '#' && left[0] == 0 -> 0
            s[i] == '#' -> solveRec(i + 1, left.decHead())
            left[0] == 0 -> solveRec(i + 1, left.drop(1))
            left[0] < clues[clues.size - left.size] -> solveRec(i + 1, left.decHead())
            else -> solveRec(i + 1, left) + solveRec(i + 1, left.decHead())
        }
    }
    solveRec(0, clues)
}

private fun List<Int>.decHead(): List<Int> = List(size) { this[it] - if (it == 0) 1 else 0 }