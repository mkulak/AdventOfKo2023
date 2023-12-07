fun main() {
    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int = input.map { line ->
    val (cards, bid) = line.split(" ")
    cards to bid.trim().toInt()
}.sortedWith(comparator).mapIndexed { i, hand -> (i + 1) * hand.second }.sum()

fun calculateType(hand: Pair<String, Int>): Int =
    types.indexOf(hand.first.groupBy { it }.values.map { it.size }.sorted())

fun calculateStrength(hand: Pair<String, Int>): Int =
    hand.first.fold(0) { acc, ch -> acc * 14 + "23456789TJQKA".indexOf(ch) }

val types = listOf(listOf(1, 1, 1, 2), listOf(1, 2, 2), listOf(1, 1, 3), listOf(2, 3), listOf(1, 4), listOf(5))

val comparator = compareBy(::calculateType).thenComparing(::calculateStrength)

private fun part2(input: List<String>): Int = 0


