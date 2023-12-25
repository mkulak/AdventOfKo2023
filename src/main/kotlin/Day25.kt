package day25
import readInput

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
            edges += a to it
        }
    }

//    graph.forEach { v, connected ->
//        println("$v: ${connected.joinToString(" ")}")
//    }
    edges.indices.forEach { i ->
        (i+1..edges.lastIndex).forEach { j ->
            (j+1..edges.lastIndex).forEach { k ->
                val a = edges[i]
                val b = edges[j]
                val c = edges[k]
                val exclude = listOf(a, b, c)
                val newGraph = graph.deepCopyWithout(exclude)
                val parts = newGraph.connectedParts()
//                println("removed $exclude parts: $parts")
                if (parts.size == 2) return parts[0] * parts[1]
            }
        }
    }
    return -1
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

private fun Graph.connectedParts(): List<Int> {
    val visited = HashSet<String>()
    val res = ArrayList<Int>()
    val buf = ArrayDeque<String>()
    keys.forEach { key ->
        if (key !in visited) {
            buf += key
            var count = 0
            while (buf.isNotEmpty()) {
                val k = buf.removeFirst()
                if (visited.add(k)) {
                    count++
                    buf += this[k]!!
                }
            }
            res += count
        }
    }
    return res
}

private typealias Graph = Map<String, List<String>>
