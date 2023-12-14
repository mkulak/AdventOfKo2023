import kotlin.math.min

fun main() {
    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int =
    input.transpose().map(::shiftLeft).sumOf(::score)

fun shiftLeft(s: String): String {
    val res = CharArray(s.length)
    var emptySpace = -1
    s.forEachIndexed { i, ch ->
        res[i] = ch
        if (ch == '#') {
            emptySpace = -1
        } else if (ch == '.') {
            if (emptySpace == -1) emptySpace = i
        } else {
            if (emptySpace != -1) {
                res[emptySpace] = ch
                res[i] = '.'
                emptySpace++
            }
        }
    }
    return String(res)
}

fun score(s: String): Int = s.foldIndexed(0) { i, acc, ch -> acc + if (ch == 'O') (s.length - i) else 0 }

private fun part2(input: List<String>): Int = 0
