import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Long = solve(input, 2)

private fun part2(input: List<String>): Long = solve(input, 1_000_000)

private fun solve(input: List<String>, mul: Long): Long {
    val galaxies = input.indices.flatMap { y ->
        input[y].indices.filter { x -> input[y][x] == '#' }.map { x -> XY(x, y) }
    }
    val emptyX = input[0].indices.filter { x -> input.indices.all { y -> input[y][x] == '.' } }.toSet()
    val emptyY = input.indices.filter { y -> input[y].all { it == '.' } }.toSet()
    return galaxies.indices.sumOf { i ->
        galaxies.indices.drop(i + 1).sumOf { j ->
            distance(galaxies[i], galaxies[j], emptyX, emptyY, mul)
        }
    }
}

fun distance(g1: XY, g2: XY, emptyX: Set<Int>, emptyY: Set<Int>, mul: Long): Long {
    val (sx, dx) = min(g1.x, g2.x) to max(g1.x, g2.x)
    val (sy, dy) = min(g1.y, g2.y) to max(g1.y, g2.y)
    return (dx - sx) + (dy - sy) + ((sx..dx).count(emptyX::contains) + (sy..dy).count(emptyY::contains)) * (mul - 1)
}
