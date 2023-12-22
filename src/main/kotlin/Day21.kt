fun main() {
    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val steps = 64
    val startY = input.indices.first { "S" in input[it] }
    val startX = input[startY].indexOf("S")
    val states = ArrayDeque<S>()
    states += S(XY(startX, startY), 0)
    val reached = HashMap<XY, Int>()
    reached[XY(startX, startY)] = 0
    while (states.isNotEmpty()) {
        val cur = states.removeFirst()
        Dir.entries.forEach { dir ->
            val newPos = cur.pos + dir.xy
            val newSteps = cur.steps + 1
            if (newPos.x in input[0].indices && newPos.y in input.indices && input[newPos.y][newPos.x] != '#') {
                val curMin = reached[newPos] ?: Int.MAX_VALUE
                if (newSteps < curMin) {
                    reached[newPos] = newSteps
                    states += S(newPos, newSteps)
                }
            }
        }
    }
//    debugPrint(input, reached)
    val res = reached.values.filter { it <= steps }.count { (steps - it) % 2 == 0 }
    return res
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

private data class S(val pos: XY, val steps: Int)

private fun part2(input: List<String>): Int {
//    val stepsLimit = 26501365
    val stepsLimit = 6
    val startY = input.indices.first { "S" in input[it] }
    val startX = input[startY].indexOf("S")
    val states = ArrayDeque<S>()
    states += S(XY(startX, startY), 0)
    val reached = HashMap<XY, Int>()
    reached[XY(startX, startY)] = 0
    while (states.isNotEmpty()) {
        val cur = states.removeFirst()
        Dir.entries.forEach { dir ->
            val newPos = cur.pos + dir.xy
            val newSteps = cur.steps + 1
            val x = newPos.x.remPos(input[0].length)
            val y = newPos.y.remPos(input.size)
            if (input[y][x] != '#') {
                val curMin = reached[newPos] ?: Int.MAX_VALUE
                if (newSteps < curMin) {
                    reached[newPos] = newSteps
                    if (newSteps < stepsLimit) states += S(newPos, newSteps)
                }
            }
        }
    }
//    debugPrint(input, reached)
    val res = reached.values.filter { it <= stepsLimit }.count { (stepsLimit - it) % 2 == 0 }
    return res
}

private fun Int.remPos(m: Int): Int = (this % m).let { if (it < 0) it + m else it }

