fun main() {
    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    var sum = 0
    var acc = 0
    var isEnginePart = false
    fun onNumEnd() {
        if (isEnginePart) sum += acc
        acc = 0
        isEnginePart = false
    }
    for (i in input.indices) {
        for (j in input[i].indices) {
            val ch = input[i][j]
            if (ch in '0'..'9') {
                acc = acc * 10 + ch.code - '0'.code
                isEnginePart = isEnginePart || checkNeighbours(input, i, j)
            } else {
                onNumEnd()
            }
        }
        onNumEnd()
    }
    return sum
}

fun checkNeighbours(input: List<String>, i: Int, j: Int): Boolean {
    for (y in (i - 1)..(i + 1)) {
        for (x in (j - 1)..(j + 1)) {
            if (y in input.indices && x in input[y].indices && input[y][x] !in '0'..'9' && input[y][x] != '.') {
                return true
            }
        }
    }
    return false
}

private fun part2(input: List<String>): Int {
    var acc = 0
    val gears = HashSet<XY>()
    val gears2parts = HashMap<XY, MutableList<Int>>()
    fun onNumEnd() {
        gears.forEach { xy -> gears2parts.getOrPut(xy, ::ArrayList) += acc }
        acc = 0
        gears.clear()
    }
    for (i in input.indices) {
        for (j in input[i].indices) {
            val ch = input[i][j]
            if (ch in '0'..'9') {
                acc = acc * 10 + ch.code - '0'.code
                gears += findGears(input, i, j)
            } else {
                onNumEnd()
            }
        }
        onNumEnd()
    }
    return gears2parts.values.filter { it.size == 2 }.sumOf { (a, b) -> a * b }
}

fun findGears(input: List<String>, i: Int, j: Int): Set<XY> {
    val res = HashSet<XY>()
    for (y in (i - 1)..(i + 1)) {
        for (x in (j - 1)..(j + 1)) {
            if (y in input.indices && x in input[y].indices && input[y][x] == '*') {
                res += XY(x, y)
            }
        }
    }
    return res
}

data class XY(val x: Int, val y: Int)