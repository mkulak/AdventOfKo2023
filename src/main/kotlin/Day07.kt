import java.util.Comparator

fun main() {
    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    fun calculateStrength(hand: String): Int =
        hand.fold(0) { acc, ch -> acc * 14 + "23456789TJQKA".indexOf(ch) }

    val comparator = compareBy(::calculateType).thenComparing(::calculateStrength)
    return solve(input, comparator)
}

private fun part2(input: List<String>): Int {
    fun calculateType2(hand: String): Int =
        "23456789TQKA".maxOf { calculateType(hand.replace("J", "$it")) }

    fun calculateStrength2(hand: String): Int =
        hand.fold(0) { acc, ch -> acc * 14 + "J23456789TQKA".indexOf(ch) }

    val comparator = compareBy(::calculateType2).thenComparing(::calculateStrength2)
    return solve(input, comparator)
}

val types = listOf(listOf(1, 1, 1, 2), listOf(1, 2, 2), listOf(1, 1, 3), listOf(2, 3), listOf(1, 4), listOf(5))

fun calculateType(hand: String): Int =
    types.indexOf(hand.groupBy { it }.values.map { it.size }.sorted())

private fun solve(input: List<String>, cmp: Comparator<String>) =
    input.map(::parseHand)
        .sortedWith { (hand1, _), (hand2, _) -> cmp.compare(hand1, hand2) }
        .mapIndexed { i, (_, bid) -> (i + 1) * bid }
        .sum()

private fun parseHand(line: String) = line.split(" ").let { (hand, bid) -> hand to bid.trim().toInt() }


