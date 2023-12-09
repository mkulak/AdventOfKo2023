fun main() {
    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Long = input.sumOf { line ->
    generateSequence(parseValues(line), ::calcDiffs).takeWhile { it.any { it != 0L } }.sumOf { it.last() }
}

private fun part2(input: List<String>): Long = input.sumOf { line ->
    val sequence = generateSequence(parseValues(line), ::calcDiffs)
    sequence.takeWhile { it.any { it != 0L } }.toList().reversed().fold(0L) { acc, s -> s.first() - acc }
}

private fun parseValues(line: String) = line.split(" ").map { it.toLong() }

private fun calcDiffs(values: List<Long>) = values.zipWithNext { a, b -> b - a }
