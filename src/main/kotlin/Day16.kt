import kotlin.math.max

fun main() {
    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int = countEnergized(input, -1, 0, LDir.Right)

private fun part2(input: List<String>): Int = max(
    (input[0].indices).maxOf {
        max(countEnergized(input, it, -1, LDir.Down), countEnergized(input, it, input.size, LDir.Up))
    },
    (input.indices).maxOf {
        max(countEnergized(input, -1, it, LDir.Right), countEnergized(input, input[0].length, it, LDir.Left))
    }
)

private fun countEnergized(input: List<String>, startX: Int, startY: Int, startLDir: LDir): Int =
    Cache().also { input.traceLight(startX, startY, startLDir, it) }.associateBy { it.first }.size

private fun List<String>.traceLight(prevX: Int, prevY: Int, dir: LDir, cache: Cache) {
    val x = prevX + dir.dx
    val y = prevY + dir.dy
    if (x !in this[0].indices || y !in this.indices) return
    val key = (y * this[0].length + x) to dir
    if (!cache.add(key)) return
    when (this[y][x]) {
        '.' -> traceLight(x, y, dir, cache)
        '\\' -> traceLight(x, y, findDir(dir.dy, dir.dx), cache)
        '/' -> traceLight(x, y, findDir(-dir.dy, -dir.dx), cache)
        '-' -> if (dir.dy == 0) traceLight(x, y, dir, cache) else {
            traceLight(x, y, LDir.Left, cache)
            traceLight(x, y, LDir.Right, cache)
        }
        '|' -> if (dir.dx == 0) traceLight(x, y, dir, cache) else {
            traceLight(x, y, LDir.Up, cache)
            traceLight(x, y, LDir.Down, cache)
        }
    }
}

private enum class LDir(val dx: Int, val dy: Int) { Up(0, -1), Left(-1, 0), Down(0, 1), Right(1, 0) }

private fun findDir(dx: Int, dy: Int) = LDir.entries.find { it.dx == dx && it.dy == dy }!!

private typealias Cache = HashSet<Pair<Int, LDir>>
