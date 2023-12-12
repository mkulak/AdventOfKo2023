fun main() {
    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
//    ???.### 1,1,3
//    .??..??...?##. 1,1,3
//    ?#?#?#?#?#?#?#? 1,3,1,6
    println(matches("#.#.###", listOf(1, 1, 3)))
    println(matches("##..###", listOf(1, 1, 3)))
}

private fun part1(input: List<String>) = input.sumOf { line ->
    val (str, nums) = line.split(" ")
    val groups = str.split(".").filter { it.isNotEmpty() }
    val clues = nums.split(",").map { it.toInt() }
    val damagedCount = str.count { it == '#' }
    val totalDamagedCount = clues.sum()
    val toDistribute = totalDamagedCount - damagedCount
    val freeSpace = str.count { it == '?' }
//    println(toDistribute)
//    if (str.none { it == '?'} ) return@map 0L
    if (matches(line, clues)) return@sumOf 1L
//    val (sGroups, sClues) = simplify(groups, clues)
//    if (sGroups.size != groups.size) {
//        println("$groups $clues -> $sGroups $sClues")
//        return@map 0L
//    }

// ???.### 1,1,3
    var counter = 0L
    val states = ArrayDeque<State>()
    states += State("", toDistribute, freeSpace)
    while (states.isNotEmpty()) {
        val state = states.removeFirst()
        if (state.current.length == str.length) {
            if (/* check clues*/matches(state.current, clues)) counter++
            continue
        }
        when (val ch = str[state.current.length]) {
            '.', '#' -> states.addFirst(state.copy(current = state.current + ch))
            '?' -> {
                if (state.freeSpace > state.toDistribute) {
                    states.addFirst(State(state.current + '.', state.toDistribute, state.freeSpace - 1))
                }
                if (state.toDistribute > 0) {
                    states.addFirst(State(state.current + '#', state.toDistribute - 1, state.freeSpace - 1))
                }
            }
        }
    }
    if (counter == 0L) println("ERROR solution not found: $str $clues")
    counter
}

data class State(val current: String, val toDistribute: Int, val freeSpace: Int)

//fun simplify(groups: List<String>, clues: List<Int>): Pair<List<String>, List<Int>> {
//    val sGroups = groups.dropWhile { group -> group.all { it == '#' } }
//    val sGroups2 = sGroups.dropLastWhile { group -> group.all { it == '#' } }
//    return sGroups2 to clues.drop(groups.size - sGroups.size).dropLast(sGroups.size - sGroups2.size)//.dropLastWhile { it.first.length == it.second }.unzip()
//}

val regex = "#+".toRegex()

fun matches(line: String, clues: List<Int>): Boolean =
    regex.findAll(line).map { it.value.length }.toList() == clues
//    groups.size == clues.size && groups.indices.all { groups[it].length == clues[it] }



private fun part2(input: List<String>): Long = 0

