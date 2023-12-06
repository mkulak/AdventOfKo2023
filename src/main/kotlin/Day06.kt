fun main() {
    val times = listOf(41, 77, 70, 96)
    val distance = listOf(249, 1362, 1127, 1011)
    println(part1(times.zip(distance)))
    println(part2(41777096L, 249136211271011L))
}

private fun part1(input: List<Pair<Int, Int>>): Int = input.map { (maxTime, distance) ->
    (1..<maxTime).count { (maxTime - it) * it > distance }
}.reduce { a, b -> a * b }

private fun part2(time: Long, distance: Long): Long {
    var count = 0L
    (1..<time).forEach {
        if ((time - it) * it > distance) count++
    }
    return count
}

