fun main() {
    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val startY = input.indexOfFirst { 'S' in it }
    val startX = input[startY].indexOf('S')
    val start = XY(startX, startY)
    var prev1 = start
    var prev2 = start
    var (dir1, dir2) = start.neighbours(input).filter { start in it.connected(input) }
    var steps = 1
    while (dir1 != dir2) {
        val next1 = nextStep(input, dir1, prev1)
        val next2 = nextStep(input, dir2, prev2)
        prev1 = dir1
        prev2 = dir2
        dir1 = next1
        dir2 = next2
        steps++
    }
    return steps
}

fun XY.neighbours(input: List<String>): List<XY> =
    listOf(XY(x - 1, y), XY(x + 1, y), XY(x, y - 1), XY(x, y + 1))
        .filter { (x, y) -> y in input.indices && x in input[0].indices }

fun nextStep(input: List<String>, from: XY, prev: XY): XY {
    val (next1, next2) = from.connected(input)
    return if (next1 == prev) next2 else next1
}

fun XY.connected(input: List<String>): List<XY> = when (input[y][x]) {
    '|' -> listOf(XY(x, y - 1), XY(x, y + 1))
    '-' -> listOf(XY(x - 1, y), XY(x + 1, y))
    'L' -> listOf(XY(x, y - 1), XY(x + 1, y))
    'J' -> listOf(XY(x, y - 1), XY(x - 1, y))
    '7' -> listOf(XY(x, y + 1), XY(x - 1, y))
    'F' -> listOf(XY(x, y + 1), XY(x + 1, y))
    else -> emptyList()
}


private fun part2(input: List<String>): Int = 0

