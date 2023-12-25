package day25

import readInput
import kotlin.time.TimeSource

fun main() {
    val input = readInput("Day25")
    println(part1(input))
}

private fun part1(input: List<String>): Int {
    val graph = HashMap<String, ArrayList<String>>()
    val edges = ArrayList<Pair<String, String>>()
    input.forEach { line ->
        val (a, bStr) = line.split(": ")
        val bs = bStr.split(" ")
        graph.getOrPut(a, ::ArrayList) += bs
        bs.forEach {
            graph.getOrPut(it, ::ArrayList) += a
            edges += edge(a, it)
        }
    }

//    graph.forEach { v, connected ->
//        println("$v: ${connected.joinToString(" ")}")
//    }
    println("size: ${edges.size}, estimated steps: ${edges.size.toLong() * edges.size * edges.size / 8}")
    var count = 0
    val mark = TimeSource.Monotonic.markNow()
    edges.indices.forEach { i ->
        (i + 1..edges.lastIndex).forEach { j ->
            (j + 1..edges.lastIndex).forEach { k ->
                val a = edges[i]
                val b = edges[j]
                val c = edges[k]
                val exclude = listOf(a, b, c)
//                val parts = graph.connectedParts(exclude)
//                if (parts.size == 2) return parts[0] * parts[1]
                val res = graph.connectedPartsSize(exclude)
                if (res != 0) return res
//                println("removed $exclude parts: $parts")
                count++
                if (count % 100_000 == 0) {
                    println("$count in ${mark.elapsedNow()}")
                }
            }
        }
    }
    return -1
}

private fun edge(a: String, b: String) = if (a < b) a to b else b to a

private fun Graph.connectedParts(exclude: List<Pair<String, String>>): List<Int> {
    val visited = HashSet<String>()
    val res = ArrayList<Int>()
    keys.forEach { key ->
        val oldSize = visited.size
        traverse(key, exclude, visited)
        val diff = visited.size - oldSize
        if (diff > 0) res += diff
    }
    return res
}

private fun Graph.connectedPartsSize(exclude: List<Pair<String, String>>): Int {
    val visited = HashSet<String>()
    keys.forEach { key ->
        val oldSize = visited.size
        traverse(key, exclude, visited)
        val diff = visited.size - oldSize
        if (oldSize > 0 && diff > 0) return oldSize * diff
    }
    return 0
}

fun Graph.traverse(v: String, exclude: List<Pair<String, String>>, visited: HashSet<String>) {
    if (visited.add(v)) {
        this[v]!!.forEach {
            if (edge(v, it) !in exclude) traverse(it, exclude, visited)
        }
    }
}

private fun Graph.deepCopyWithout(exclude: List<Pair<String, String>>): Graph {
    val res = HashMap<String, ArrayList<String>>()
    forEach { (key, value) -> res[key] = ArrayList(value) }
    exclude.forEach { (a, b) ->
        res[a]?.remove(b)
        res[b]?.remove(a)
    }
    return res
}

private typealias Graph = Map<String, List<String>>
