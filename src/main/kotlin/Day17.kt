import kotlin.time.measureTime

fun main() {
    val input = readInput("Day17")
    println(part1(input)) // 928
    println(part2(input)) // 1104
}

private fun part1(input: List<String>): Int = solve(input, 0, 3)

private fun part2(input: List<String>): Int = solve(input, 4, 10)

private fun solve(input: List<String>, minSteps: Int, maxSteps: Int): Int {
    val costs = input.map { it.map { it.code - '0'.code } }
    val target = XY(costs[0].lastIndex, costs.lastIndex)
    val visited = HashMap<RecordKey, Int>().withDefault { Int.MAX_VALUE }
    val states = ArrayDeque<State>().apply { add(State(XY(0, 0), Dir.Right, 0, 0)) }
    var best: State? = null
    while (states.isNotEmpty()) {
        val cur = states.removeFirst()
        if (cur.pos == target && (best?.cost ?: Int.MAX_VALUE) > cur.cost) {
            best = cur
//            continue
        }
        val recordKey = RecordKey(cur.pos, cur.dir, cur.steps)
        if (visited.getValue(recordKey) <= cur.cost) continue
        visited[recordKey] = cur.cost
        listOfNotNull(
            cur.dir.takeIf { cur.steps < maxSteps },
            cur.dir.next.takeIf { cur.steps >= minSteps },
            cur.dir.prev.takeIf { cur.steps >= minSteps },
        ).forEach { newDir ->
            val newPos = cur.pos + newDir
            if (newPos.x in costs[0].indices && newPos.y in costs.indices) {
                val newSteps = if (newDir == cur.dir) cur.steps + 1 else 1
                val newCost = cur.cost + costs[newPos.y][newPos.x]
                val newKey = RecordKey(newPos, newDir, newSteps)
                if (visited.getValue(newKey) > newCost) {
                    states.add(State(newPos, newDir, newSteps, newCost))
                }
            }
        }
    }
    return best?.cost!!
}

private data class State(val pos: XY, val dir: Dir, val steps: Int, val cost: Int)

private data class RecordKey(val pos: XY, val dir: Dir, val steps: Int)
