fun main() {
    val input = readInput("Day12")
    println(part1(input))
//    println(part2(input))
}
private fun part1(input: List<String>) = solve(input, 1)

private fun part2(input: List<String>) = solve(input, 5)

private fun solve(input: List<String>, mul: Int) = input.sumOf { line ->
    val (str, nums) = line.split(" ")
    val fullStr = List(mul) { str }.joinToString("?")
    val clues = nums.split(",").map { it.toInt() }.let { list -> (1..mul).flatMap { list } }

    val groups = fullStr.split(".").filter { it.isNotEmpty() }
    val damagedCount = fullStr.count { it == '#' }
    val totalDamagedCount = clues.sum()
    val toDistribute = totalDamagedCount - damagedCount
    val freeSpace = fullStr.count { it == '?' }
//    println(toDistribute)
//    if (str.none { it == '?'} ) return@map 0L
    if (matches(line, clues, false)) return@sumOf 1L
//    val (sGroups, sClues) = simplify(groups, clues)
//    if (sGroups.size != groups.size) {
//        println("$groups $clues -> $sGroups $sClues")
//        return@map 0L
//    }

    var counter = 0L
    states.clear()
    states += State("", toDistribute, freeSpace)
    while (states.isNotEmpty()) {
//        println(states.map { it.current })
        val state = states.removeFirst()
        if (state.current.length == fullStr.length) {
            if (matches(state.current, clues, false)) {
                counter++
            }
            continue
        }
        when (val ch = fullStr[state.current.length]) {
            '.', '#' -> states.addFirst(state.copy(current = state.current + ch))
            '?' -> {
                if (state.freeSpace > state.toDistribute) {
                    val next = state.current + '.'
                    if (matches(next, clues, true)) states.addFirst(State(next, state.toDistribute, state.freeSpace - 1))
                }
                if (state.toDistribute > 0) {
                    val next = state.current + '#'
                    if (matches(next, clues, true)) states.addFirst(State(next, state.toDistribute - 1, state.freeSpace - 1))
                }
            }
        }
    }
    println("$line $counter")
    if (counter == 0L) println("ERROR solution not found: $fullStr $clues")
    counter
}

val states = ArrayDeque<State>(10_000_000)

data class State(val current: String, val toDistribute: Int, val freeSpace: Int)

fun matches(line: String, clues: List<Int>, partially: Boolean): Boolean {
    var i = 0
    var cur = 0
    for (ch in line) {
        if (ch == '#') {
            cur++
            if (i >= clues.size || cur > clues[i]) return false
        } else if (cur > 0) {
            if (cur != clues[i]) return false
            i++
            cur = 0
        }
    }
    return partially || i == clues.size || ((i == clues.size - 1) && clues[i] == cur)
}

//??????.??#. 2,3 16016
//??.?###????????? 2,4,4 3515625
//?????????.??##? 1,2,1,1,5 6480
//?##?.????.?#?.?#? 3,1,1,2,2 7962624
//?#?.#?##???? 3,1,4 16
//??#????#?? 4,4 1950
//???????#?.??? 2,2 9185277
//?.???????#? 2,3 252684
