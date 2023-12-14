import kotlin.math.min

fun main() {
    val input = readInputAsText("Day13")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Int = solve(input, false)

private fun part2(input: String): Int = solve(input, true)

private fun solve(input: String, smudge: Boolean): Int = input.split("\n\n").sumOf { pattern ->
    val lines = pattern.split("\n")
    findMirror(lines, smudge)?.let { (it + 1) * 100 } ?: (findMirror(lines.transpose(), smudge)!! + 1)
}

private fun findMirror(lines: List<String>, smudge: Boolean): Int? =
    (0..<lines.size - 1).find { i ->
        val diff = diff(lines[i], lines[i + 1])
        val (limit1, limit2) = if (smudge) 1 to (1 - diff) else 0 to 0
        val count = min(i, lines.size - i - 2)
        diff <= limit1 && (1..count).sumOf { diff(lines[i - it], lines[i + 1 + it]) } == limit2
    }

private fun diff(s1: String, s2: String): Int = s1.zip(s2).count { (a, b) -> a != b }
