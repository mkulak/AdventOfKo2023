package day24

import readInput
import kotlin.math.sign

fun main() {
    val input = readInput("Day24")
    println(part1(input))
}

private fun part1(input: List<String>): Int {
    val particles = input.map { line ->
        val (coords, velocity) = line.split(" @ ")
        val (x, y) = coords.split(",").map { it.trim().toLong() }
        val (vx, vy) = velocity.split(",").map { it.trim().toLong() }
        Particle(x, y, vx, vy)
    }
    println(particles)
    val bounds = 200000000000000.0..400000000000000.0
//    val bounds = 7.0..27.0
    val res = particles.indices.sumOf { i ->
        (i + 1..<particles.size).count { j ->
            val a = particles[i]
            val b = particles[j]
            val c = collision(a, b)
//            println("$a $b collision: $c")
            c != null && c.first in bounds && c.second in bounds && sameDir(a, c) && sameDir(b, c)
        }
    }
    return res
}

fun sameDir(p: Particle, c: Pair<Double, Double>): Boolean =
    sign(p.vx.toDouble()) == sign(c.first - p.x) &&
        sign(p.vy.toDouble()) == sign(c.second - p.y)

fun collision(p1: Particle, p2: Particle): Pair<Double, Double>? {
    val (a, b) = p1.koef()
    val (c, d) = p2.koef()
    if (a == c) return null
    val cx = (d - b) / (a - c)
    val cy = cx * a + b
    return cx to cy
}

data class Particle(val x: Long, val y: Long, val vx: Long, val vy: Long) {
    fun koef(): Pair<Double, Double> {
        val a = vy.toDouble() / vx
        val b = -x * a + y
        return a to b
    }
}

