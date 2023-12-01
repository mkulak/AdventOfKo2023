fun main() {
    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int = input.sumOf { line ->
    line.first { it.isDigit() }.code * 10 + line.last { it.isDigit() }.code - '0'.code * 11
}

private fun part2(input: List<String>): Int {
    val words = listOf(
        "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    )
    val map = words.mapIndexed { i, word -> word to i }.toMap() + List(10) { "$it" to it}.toMap()
    return input.sumOf { line ->
        val first = map.keys.minBy { line.indexOf(it).takeIf { it != -1 } ?: Int.MAX_VALUE }
        val last = map.keys.maxBy { line.lastIndexOf(it) }
        map.getValue(first) * 10 + map.getValue(last)
    }
}