import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInput("Day22")
    println(part1(input)) // 522
    println(part2(input)) // 83519
}

private fun part1(input: List<String>): Int {
    val bricks = parseAndFall(input)
    return bricks.count { brick ->
        bricks.filter(brick::supports).all { supportee -> bricks.filter { it.supports(supportee) }.size > 1 }
    }
}

private fun part2(input: List<String>): Int {
    val bricks = parseAndFall(input)
    val supportsMap = bricks.associateWith { brick -> bricks.filter(brick::supports) }
    val supportedByMap = bricks.associateWith { brick -> bricks.filter { it.supports(brick) } }
    return bricks.sumOf { brick ->
        var layer = supportsMap[brick].orEmpty()
        val moved = mutableSetOf(brick)
        while (layer.isNotEmpty()) {
            val toMove = layer.filter { b -> moved.containsAll(supportedByMap[b].orEmpty()) }
            moved += toMove
            layer = toMove.flatMap { supportsMap[it].orEmpty() }
        }
        moved.size - 1
    }
}

private fun parseAndFall(input: List<String>): List<Brick> {
    val bricks = input.map { line ->
        val (a, b) = line.split("~").map { str ->
            val (x, y, z) = str.split(",").map { it.toInt() }
            XYZ(x, y, z)
        }
        Brick(a, b)
    }
    bricks.sortedBy { it.minZ }.forEach { brick ->
        while (brick.minZ > 1 && bricks.none { it.supports(brick) }) {
            brick.a.z--
            brick.b.z--
        }
    }
    return bricks
}

private fun Brick.supports(other: Brick): Boolean =
    maxZ + 1 == other.minZ && (other.a.y in ys && a.x in other.xs || a.y in other.ys && other.a.x in xs)

private data class XYZ(val x: Int, val y: Int, var z: Int)

private data class Brick(val a: XYZ, val b: XYZ) {
    val maxZ: Int get() = max(a.z, b.z)
    val minZ: Int get() = min(a.z, b.z)
    val ys: IntRange get() = min(a.y, b.y)..max(a.y, b.y)
    val xs: IntRange get() = min(a.x, b.x)..max(a.x, b.x)
}
