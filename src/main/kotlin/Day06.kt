fun main() {
    val times = listOf<Long>(41, 77, 70, 96)
    val distance = listOf<Long>(249, 1362, 1127, 1011)
    println(part1(times.zip(distance)))
    println(part2(41777096L, 249136211271011L))
}

private fun part1(input: List<Pair<Long, Long>>): Int =
    input.map { (time, distance) -> solve(time, distance) }.reduce { a, b -> a * b }

private fun part2(time: Long, distance: Long): Int = solve(time, distance)

private fun solve(time: Long, distance: Long) = (1..<time).count { (time - it) * it > distance }

