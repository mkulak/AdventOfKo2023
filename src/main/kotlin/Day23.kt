package day23

import Dir
import XY
import readInput

fun main() {
    val input = readInput("Day23")
//    println(part1(input)) // 2218
    println(part2(input))
}

private fun debugPrint(input: List<String>, reached: Map<XY, Int>) {
    input.indices.forEach { y ->
        input[y].indices.forEach { x ->
            val value = input[y][x]
            val xy = XY(x, y)
            val ch =
                if (value == '#') "#" else if (xy !in reached) "." else if (reached[xy]!! < 10) reached[xy].toString() else "B"
            print(ch)
        }
        println()
    }
}

private data class State(val pos: XY, val steps: Int, val visited: Set<XY>, val dir: Dir, val prev: State?)

private fun part1(input: List<String>): Int {
    val start = XY(input.first().indexOf("."), 0)
    val finish = XY(input.last().indexOf("."), input.lastIndex)
    val states = ArrayDeque<State>()
    states += State(start, 0, setOf(start), Dir.Down, null)
    var maxSteps = 0
    while (states.isNotEmpty()) {
        val cur = states.removeFirst()
        if (cur.pos == finish) {
            if (maxSteps < cur.steps) maxSteps = cur.steps
            continue
        }
        val dirs = when (input[cur.pos.y][cur.pos.x]) {
            '.' -> Dir.entries
            '^' -> listOf(Dir.Up)
            '>' -> listOf(Dir.Right)
            '<' -> listOf(Dir.Left)
            'v' -> listOf(Dir.Down)
            else -> emptyList()
        }
        dirs.forEach { dir ->
            val newPos = cur.pos + dir.xy
            val newSteps = cur.steps + 1
            if (newPos !in cur.visited && newPos.x in input[0].indices && newPos.y in input.indices && input[newPos.y][newPos.x] != '#') {
                states += State(newPos, newSteps, cur.visited + newPos, dir, cur)
            }
        }
    }
    return maxSteps
}

private fun part2(input: List<String>): Int {
    val start = XY(input.first().indexOf("."), 0)
    val finish = XY(input.last().indexOf("."), input.lastIndex)
    val states = ArrayDeque<State>()
    states += State(start, 0, setOf(start), Dir.Down, null)
    val reached = HashMap<XY, IntArray>()
    reached[start] = IntArray(4)
    var maxSteps = 0
    while (states.isNotEmpty()) {
        val cur = states.removeFirst()
        if (cur.pos == finish) {
            println("Reached finish in ${cur.steps}")
            generateSequence(cur) { cur.prev }.forEach {
                val curMax = reached.getOrPut(it.pos) { IntArray(4) }
                if (it.steps > curMax[it.dir.ordinal]) {
                    curMax[it.dir.ordinal] = it.steps
                }
            }
            if (maxSteps < cur.steps) maxSteps = cur.steps
            continue
        }
        Dir.entries.forEach { dir ->
            val newPos = cur.pos + dir.xy
            val newSteps = cur.steps + 1
            if (newPos !in cur.visited && newPos.x in input[0].indices && newPos.y in input.indices && input[newPos.y][newPos.x] != '#') {
                val curMax = reached.getOrPut(newPos) { IntArray(4) }
                val oldSteps = curMax[dir.ordinal]
                if (oldSteps < newSteps) {
                    states.addFirst(State(newPos, newSteps, cur.visited + newPos, dir, cur))
                }
            }
        }
    }
//    println(reached.keys)
//    debugPrint(input, reached)
    return maxSteps
}

