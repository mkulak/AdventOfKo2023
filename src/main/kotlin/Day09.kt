fun main() {
    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Long = input.sumOf { line ->
    val values = line.split(" ").map { it.toLong() }
    generateSequence(values, ::calcDiffs).takeWhile { it.any { it != 0L } }.sumOf { it.last().toLong() }
}

private fun part2(input: List<String>): Long = input.sumOf { line ->
    val values = line.split(" ").map { it.toLong() }
    generateSequence(values, ::calcDiffs)
        .takeWhile { it.any { it != 0L } }
        .toList()
        .reversed()
        .fold(0L) { acc, s -> s.first() - acc }
}

private fun calcDiffs(values: List<Long>): List<Long> = values.zipWithNext { a, b -> b - a }

