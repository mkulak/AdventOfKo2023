fun main() {
    val input = readInput("Day14")
    println(part1(input)) // 110274
    println(part2(input)) // 90982
}

private fun part1(input: List<String>): Int {
    val arr = Array(input.size) { input[it].toCharArray() }
    shift(arr, Dir.Up)
    return score(arr)
}

private fun part2(input: List<String>): Int {
    val arr = Array(input.size) { input[it].toCharArray() }
    val hashes = LinkedHashSet<Long>()
    var hash = arr.hash()
    while (hashes.add(hash)) {
        Dir.entries.forEach { shift(arr, it) }
        hash = arr.hash()
    }
    val repeatedIndex = hashes.indexOf(hash)
    val cycleSize = hashes.size - repeatedIndex
    val steps = (1_000_000_000 - repeatedIndex) % cycleSize
    repeat(steps) {
        Dir.entries.forEach { shift(arr, it) }
    }
    return score(arr)
}

private fun shift(arr: Array<CharArray>, dir: Dir) = when (dir) {
    Dir.Up, Dir.Down ->
        arr[0].indices.forEach { x ->
            var emptySpace = -1
            val range = if (dir == Dir.Up) arr.indices else arr.indices.reversed()
            val next = if (dir == Dir.Up) 1 else -1
            range.forEach { y ->
                when {
                    arr[y][x] == '#' -> emptySpace = -1
                    arr[y][x] == '.' && emptySpace == -1 -> emptySpace = y
                    arr[y][x] == 'O' && emptySpace != -1 -> {
                        arr[emptySpace][x] = arr[y][x]
                        arr[y][x] = '.'
                        emptySpace += next
                    }
                }
            }
        }

    Dir.Left, Dir.Right ->
        arr.indices.forEach { y ->
            var emptySpace = -1
            val range = if (dir == Dir.Left) arr.indices else arr.indices.reversed()
            val next = if (dir == Dir.Left) 1 else -1
            range.forEach { x ->
                val ch = arr[y][x]
                when {
                    ch == '#' -> emptySpace = -1
                    ch == '.' && emptySpace == -1 -> emptySpace = x
                    ch == 'O' && emptySpace != -1 -> {
                        arr[y][emptySpace] = ch
                        arr[y][x] = '.'
                        emptySpace += next
                    }
                }
            }
        }
}


private fun score(s: Array<CharArray>): Int =
    s.foldIndexed(0) { i, acc, line -> acc + line.count { it == 'O' } * (s.size - i) }

private fun Array<CharArray>.hash(): Long =
    fold(1L) { acc, arr -> acc * 31 + arr.contentHashCode() }
