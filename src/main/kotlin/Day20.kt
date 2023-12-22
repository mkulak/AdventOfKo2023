fun main() {
    val input = readInput("Day20")
    println(part1(input)) // 743871576
    println(part2(input))
}

private fun part1(input: List<String>): Long {
    val devices = parseDevices(input)
    var lowPulses = 0L
    var highPulses = 0L
    repeat(1000) {
        var current = listOf(Triple("button", "broadcaster", Pulse.Low))
        while (current.isNotEmpty()) {
            current = current.flatMap { (from, to, pulse) ->
                if (pulse == Pulse.High) highPulses++ else lowPulses++
                val device = devices.getValue(to)
                val newPulse = device.accept(pulse, from)
                if (newPulse != null) {
                    device.out.map { newTo -> Triple(to, newTo, newPulse) }
                } else emptyList()
            }
        }
    }
    return lowPulses * highPulses
}

private fun part2(input: List<String>): Long {
    val devices = parseDevices(input)
    var count = 0L
//    while (true) {
//        count++
//        var current = listOf(Triple("button", "broadcaster", Pulse.Low))
//        while (current.isNotEmpty()) {
//            current = current.flatMap { (from, to, pulse) ->
//                if (pulse == Pulse.Low && to == "rx") return@part2 count
//                val device = devices.getValue(to)
//                val newPulse = device.accept(pulse, from)
//                if (newPulse != null) {
//                    device.out.map { newTo -> Triple(to, newTo, newPulse) }
//                } else emptyList()
//            }
//        }
//        if (count % 10_000L == 0L) println(count)
//    }
    return 0
}

//50470000 still not found

private fun parseDevices(input: List<String>): Map<String, Device> {
    val devices = input.map { line ->
        val (id, outputList) = line.split(" -> ")
        val out = outputList.split(", ")
        when (id[0]) {
            '%' -> FlipFlop(id.drop(1), false, out)
            '&' -> Conjunction(id.drop(1), HashMap(), out)
            else -> Broadcaster(id, out)
        }
    }.associateBy { it.id }.toMutableMap()

    devices.values.toList().forEach { device ->
        device.out.forEach { id ->
            val outDevice = devices.getOrPut(id) { Broadcaster(id, emptyList()) }
            if (outDevice is Conjunction) outDevice.input[device.id] = Pulse.Low
        }
    }
    return devices
}

private fun Device.accept(pulse: Pulse, from: String): Pulse? = when (this) {
    is Broadcaster -> pulse
    is FlipFlop -> if (pulse == Pulse.High) null else {
        state = !state
        if (state) Pulse.High else Pulse.Low
    }
    is Conjunction -> {
        input[from] = pulse
        if (input.values.all { it == Pulse.High }) Pulse.Low else Pulse.High
    }
}

enum class Pulse { Low, High }

sealed interface Device {
    val id: String
    val out: List<String>
}

data class Broadcaster(override val id: String, override val out: List<String>) : Device
data class FlipFlop(override val id: String, var state: Boolean, override val out: List<String>) : Device
data class Conjunction(override val id: String, val input: MutableMap<String, Pulse>, override val out: List<String>) :
    Device

