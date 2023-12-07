import java.util.Comparator

fun main() {
    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int =
    solve(input, compareBy(::calculateType).thenComparing("23456789TJQKA"::calculateStrength))

private fun part2(input: List<String>): Int =
    solve(input, compareBy(::calculateType2).thenComparing("J23456789TQKA"::calculateStrength))

private val types = listOf(listOf(1, 1, 1, 2), listOf(1, 2, 2), listOf(1, 1, 3), listOf(2, 3), listOf(1, 4), listOf(5))

private fun calculateType(hand: String): Int =
    types.indexOf(hand.groupBy { it }.values.map { it.size }.sorted())

private fun calculateType2(hand: String): Int =
    "23456789TQKA".maxOf { calculateType(hand.replace("J", "$it")) }

private fun String.calculateStrength(hand: String): Int =
    hand.fold(0) { acc, ch -> acc * 14 + indexOf(ch) }

private fun solve(input: List<String>, cmp: Comparator<String>) =
    input.map(::parseHand)
        .sortedWith { (hand1, _), (hand2, _) -> cmp.compare(hand1, hand2) }
        .mapIndexed { i, (_, bid) -> (i + 1) * bid }
        .sum()

private fun parseHand(line: String) = line.split(" ").let { (hand, bid) -> hand to bid.trim().toInt() }
