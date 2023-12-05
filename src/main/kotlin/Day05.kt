fun main() {
    val input = readInputAsText("Day05")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Long {
    val sections = input.split("\n\n")
    val seeds = sections[0].removePrefix("seeds: ").split(" ").map { it.toLong() }
    val mappingsLayers = parseMappingLayers(sections)
    return seeds.map { findLocation(it, mappingsLayers) }.min()
}

private fun part2(input: String): Long {
    val sections = input.split("\n\n")
    val seeds = sections[0].removePrefix("seeds: ").split(" ").map { it.toLong() }.chunked(2) { (start, size) -> start..<(start + size) }
    val mappingsLayers = parseMappingLayers(sections)
    return seeds.minOf { it.minOf { findLocation(it, mappingsLayers) } }
}

private fun parseMappingLayers(sections: List<String>) = sections.drop(1).map { section ->
    section.lines().drop(1).map { line ->
        val (dst, src, size) = line.split(" ").map { it.toLong() }
        Mapping(src, size, dst - src)
    }
}

private fun findLocation(seed: Long, layers: List<List<Mapping>>): Long = layers.fold(seed) { acc, layer ->
    acc + (layer.find { acc >= it.start && acc < (it.start + it.size) }?.offset ?: 0)
}


data class Mapping(val start: Long, val size: Long, val offset: Long)
