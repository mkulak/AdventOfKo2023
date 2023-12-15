fun main() {
    val input = readInputAsText("Day15")
    println(hash("HASH"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Int = input.split(",").sumOf(::hash)

private fun part2(input: String): Int {
    val boxes = Array(256) { LinkedHashMap<String, Int>() }
    input.split(",").map { s ->
//        val parts = s.split('=')
//        if (parts.size == 1) parts[0] to null else parts[0] to parts[1].toInt()
        if (s.last() == '-') s.dropLast(1) to null else s.substringBefore('=') to s.substringAfter('=').toInt()
    }.forEach { (label, foc) ->
        val box = boxes[hash(label)]
        if (foc == null) box.remove(label) else box[label] = foc
    }
    return boxes.mapIndexed { i, box ->
        box.entries.mapIndexed { j, (_, foc) ->  (i + 1) * (j + 1) * foc }.sum()
    }.sum()
}

private fun hash(s: String): Int = s.fold(0) { acc, ch -> ((acc + ch.code) * 17) % 256 }
