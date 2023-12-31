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
            if (ch.isDigit()) {
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
            if (y in input.indices && x in input[y].indices && !input[y][x].isDigit() && input[y][x] != '.') {
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
        gears.clear()
        acc = 0
    }

    fun addGears(i: Int, j: Int) {
        for (y in (i - 1)..(i + 1)) {
            for (x in (j - 1)..(j + 1)) {
                if (y in input.indices && x in input[y].indices && input[y][x] == '*') {
                    gears += XY(x, y)
                }
            }
        }
    }
    for (i in input.indices) {
        for (j in input[i].indices) {
            val ch = input[i][j]
            if (ch.isDigit()) {
                acc = acc * 10 + ch.code - '0'.code
                addGears(i, j)
            } else {
                onNumEnd()
            }
        }
        onNumEnd()
    }
    return gears2parts.values.filter { it.size == 2 }.sumOf { (a, b) -> a * b }
}

