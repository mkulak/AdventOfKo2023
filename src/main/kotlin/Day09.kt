fun main() {
    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Long = input.sumOf { line ->
    val values = line.split(" ").map { it.toInt() }
    val lastNums = mutableListOf(values.last().toLong())
    var diffs = values
    do {
        diffs = calcDiffs(diffs)
        lastNums += diffs.last().toLong()
    } while (diffs.toSet().size > 1)
    lastNums.reversed().sum()
}

private fun part2(input: List<String>): Long = input.sumOf { line ->
    val values = line.split(" ").map { it.toInt() }
    val firstNums = mutableListOf(values.first().toLong())
    var diffs = values
    do {
        diffs = calcDiffs(diffs)
        firstNums += diffs.first().toLong()
    } while (diffs.toSet().size > 1)
    firstNums.reversed().fold(0L) { acc, num -> num - acc }
}

private fun calcDiffs(values: List<Int>): List<Int> = values.zipWithNext { a, b -> b - a }

