import kotlin.math.max
import kotlin.math.pow

fun main() {
    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Long = input.sumOf { line ->
    val (win, has) = line.replace("Card(\\s+)(\\d+):".toRegex(), "").split("|")
    val winSet = win.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }.toSet()
    val winCount = has.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }.count { it in winSet }
    2.0.pow(winCount - 1).toLong()
}

private fun part2(input: List<String>): Int = 0

