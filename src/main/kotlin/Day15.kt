fun main() {
    val input = readInputAsText("Day15")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Int = input.split(",").sumOf(::hash)

private fun part2(input: String): Int =
    input.split(",").map(::parse).fold(List(256) { LinkedHashMap<String, Int>() }) { boxes, (label, foc) ->
        val box = boxes[hash(label)]
        if (foc == null) box.remove(label) else box[label] = foc
        boxes
    }.mapIndexed { i, box ->
        box.entries.mapIndexed { j, (_, foc) -> (i + 1) * (j + 1) * foc }.sum()
    }.sum()

private fun parse(s: String) =
    if (s.last() == '-') s.dropLast(1) to null else s.substringBefore('=') to s.substringAfter('=').toInt()

private fun hash(s: String): Int = s.fold(0) { acc, ch -> ((acc + ch.code) * 17) % 256 }
