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

private fun part2(input: List<String>): Int {
    val startY = input.indexOfFirst { 'S' in it }
    val startX = input[startY].indexOf('S')
    val start = XY(startX, startY)
    val loop = mutableSetOf(start)
    var cur = start.neighbours(input).find { start in it.connected(input) }
    while (cur != null) {
        loop += cur
        cur = cur.connected(input).find { it !in loop }
    }
    var counter = 0
    val inside = HashSet<XY>()
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (XY(x, y) in loop) {
                continue
            }
            var intersectionCount = 0
            var goUp = false
            for (ax in (x + 1)..<input[y].length) {
                if (XY(ax, y) in loop && input[y][ax] != '-') {
                    val ch = input[y][ax]
                    when (ch) {
                        '|' -> intersectionCount++
                        'L' -> goUp = true
                        'F' -> goUp = false
                        '7' -> if (goUp) intersectionCount++
                        'J' -> if (!goUp) intersectionCount++
                    }
                }
            }
            if (intersectionCount % 2 == 1) {
                inside += XY(x, y)
                counter++
            }
        }
    }
    debugPrint(input, loop, inside)
    return counter
}

private fun debugPrint(input: List<String>, loop: Set<XY>, inside: Set<XY>) {
    println()
    for (y in input.indices) {
        for (x in input[y].indices) {
            val xy = XY(x, y)
            val color = when (xy) {
                in loop -> ANSI_RED
                in inside -> ANSI_GREEN
                else -> ANSI_BLACK
            }
            val bgColor = when (xy) {
                in loop -> ""
                in inside -> ANSI_GREEN_BACKGROUND
                else -> ""
            }
            val ch = when (input[y][x]) {
                '|' -> '│'
                '-' -> '-'
                'L' -> '└'
                'J' -> '┘'
                '7' -> '┐'
                'F' -> '┌'
                else -> input[y][x]
            }
            print(bgColor + color + ch + ANSI_RESET)
        }
        println()
    }
    println()
}
