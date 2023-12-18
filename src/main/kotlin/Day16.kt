fun main() {
    val input = readInput("Day16")
    println(part1(input)) // 7996
    println(part2(input)) // 8239
}

private fun part1(input: List<String>): Int = countEnergized(input, -1, 0, Dir.Right)

private fun part2(input: List<String>): Int =
    ((input[0].indices).flatMap { listOf(Triple(it, -1, Dir.Down), Triple(it, input.size, Dir.Up)) } +
        (input.indices).flatMap { listOf(Triple(-1, it, Dir.Right), Triple(input[0].length, it, Dir.Left)) })
        .maxOf { (x, y, dir) -> countEnergized(input, x, y, dir) }

private fun countEnergized(input: List<String>, startX: Int, startY: Int, startDir: Dir): Int =
    Cache().also { input.traceLight(startX, startY, startDir, it) }.associateBy { it.first }.size

private fun List<String>.traceLight(prevX: Int, prevY: Int, dir: Dir, cache: Cache) {
    val x = prevX + dir.dx
    val y = prevY + dir.dy
    if (x !in this[0].indices || y !in this.indices) return
    val key = (y * this[0].length + x) to dir
    if (!cache.add(key)) return
    when (this[y][x]) {
        '.' -> traceLight(x, y, dir, cache)
        '\\' -> traceLight(x, y, Dir.find(dir.dy, dir.dx), cache)
        '/' -> traceLight(x, y, Dir.find(-dir.dy, -dir.dx), cache)
        '-' -> if (dir.dy == 0) traceLight(x, y, dir, cache) else {
            traceLight(x, y, Dir.Left, cache)
            traceLight(x, y, Dir.Right, cache)
        }

        '|' -> if (dir.dx == 0) traceLight(x, y, dir, cache) else {
            traceLight(x, y, Dir.Up, cache)
            traceLight(x, y, Dir.Down, cache)
        }
    }
}
//10888 too low

private typealias Cache = HashSet<Pair<Int, Dir>>
