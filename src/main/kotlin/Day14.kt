fun main() {
    val input = readInput("Day14")
    println(part1(input)) // 110274
    println(part2(input)) // 90982
}

private fun part1(input: List<String>): Int {
    val arr = Array(input.size) { input[it].toCharArray() }
    shift(arr, Dir.North)
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

fun shift(arr: Array<CharArray>, dir: Dir) = when (dir) {
    Dir.North, Dir.South ->
        arr[0].indices.forEach { x ->
            var emptySpace = -1
            val range = if (dir == Dir.North) arr.indices else arr.indices.reversed()
            val next = if (dir == Dir.North) 1 else -1
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

    Dir.West, Dir.East ->
        arr.indices.forEach { y ->
            var emptySpace = -1
            val range = if (dir == Dir.West) arr.indices else arr.indices.reversed()
            val next = if (dir == Dir.West) 1 else -1
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

fun score(s: Array<CharArray>): Int =
    s.foldIndexed(0) { i, acc, line -> acc + line.count { it == 'O' } * (s.size - i) }

enum class Dir { North, West, South, East }

private fun Array<CharArray>.hash(): Long =
    fold(1L) { acc, arr -> acc * 31 + arr.contentHashCode() }
