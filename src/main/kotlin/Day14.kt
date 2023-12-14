import java.util.LinkedHashSet
import kotlin.time.measureTime

fun main() {
    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

//110274
//90604

private fun part1(input: List<String>): Int {
    val arr = Array(input.size) { input[it].toCharArray() }
    shift(arr, Dir.North)
    return score(arr)
}

fun score(s: Array<CharArray>): Int = s.foldIndexed(0) { i, acc, line -> acc + line.count { it == 'O' } * (s.size - i) }

enum class Dir { North, West, South, East }

private fun part2(input: List<String>): Int {
    val arr = Array(input.size) { input[it].toCharArray() }
    val hashes = LinkedHashSet<Long>()
    do {
        Dir.entries.forEach { shift(arr, it) }
        val hash = arr.hash()
    } while (hashes.add(hash))
//    val list = generateSequence { Dir.entries.forEach { shift(arr, it) }; arr.hash() }.onEach { hash ->
//        hashes.add(hash).also { if (!it) println("first repeating: $hash" ) }
//    }.take(200).toList()
//    println(list)
//    println(hashes)
    val hash = arr.hash()
    val repeatedIndex = hashes.indexOf(hash)
    val cycleSize = hashes.size - repeatedIndex
    val steps = (1_000_000_000 - repeatedIndex - 1) % cycleSize
    println("hashes: $hashes")
    println("last hash: $hash")
    println("hash size: ${hashes.size}")
    println("repeatedIndex: $repeatedIndex")
    println("cycleSize: $cycleSize")
    println("steps: $steps")
    repeat(steps) {
        Dir.entries.forEach { shift(arr, it) }
    }
    return score(arr)
}


private fun Array<CharArray>.hash(): Long =
    fold(1L) { acc, arr -> acc * 31 + arr.contentHashCode() }

private fun Array<CharArray>.debugPrint() {
    forEach { line ->
        line.forEach { print(it) }
        println()
    }
}

fun shift(arr: Array<CharArray>, dir: Dir) = when (dir) {
    Dir.North ->
        arr[0].indices.forEach { x ->
            var emptySpace = -1
            arr.indices.forEach { y ->
                val ch = arr[y][x]
                if (ch == '#') {
                    emptySpace = -1
                } else if (ch == '.') {
                    if (emptySpace == -1) emptySpace = y
                } else if (ch == 'O' && emptySpace != -1) {
                    arr[emptySpace][x] = ch
                    arr[y][x] = '.'
                    emptySpace++
                }
            }
        }

    Dir.South ->
        arr[0].indices.forEach { x ->
            var emptySpace = -1
            arr.indices.reversed().forEach { y ->
                val ch = arr[y][x]
                if (ch == '#') {
                    emptySpace = -1
                } else if (ch == '.') {
                    if (emptySpace == -1) emptySpace = y
                } else if (ch == 'O' && emptySpace != -1) {
                    arr[emptySpace][x] = ch
                    arr[y][x] = '.'
                    emptySpace--
                }
            }
        }

    Dir.West ->
        arr.indices.forEach { y ->
            var emptySpace = -1
            arr[0].indices.forEach { x ->
                val ch = arr[y][x]
                if (ch == '#') {
                    emptySpace = -1
                } else if (ch == '.') {
                    if (emptySpace == -1) emptySpace = x
                } else if (ch == 'O' && emptySpace != -1) {
                    arr[y][emptySpace] = ch
                    arr[y][x] = '.'
                    emptySpace++
                }
            }
        }

    Dir.East ->
        arr.indices.forEach { y ->
            var emptySpace = -1
            arr[0].indices.reversed().forEach { x ->
                val ch = arr[y][x]
                if (ch == '#') {
                    emptySpace = -1
                } else if (ch == '.') {
                    if (emptySpace == -1) emptySpace = x
                } else if (ch == 'O' && emptySpace != -1) {
                    arr[y][emptySpace] = ch
                    arr[y][x] = '.'
                    emptySpace--
                }
            }
        }
}