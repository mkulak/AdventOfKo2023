fun main() {
    val input = readInput("Day20")
    println(part1(input)) // 743871576
//    println(part2(input)) // 244151741342687
    
// vn period = 3797
// hn period = 4021
// kt period = 4093
// ph period = 3907
    println(listOf(3797L, 4021, 4093, 3907).reduce(::lcm))
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
                val newPulse = device.accept(pulse, from, 0)
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
    val inMap = HashMap<String, MutableList<String>>()
    devices.values.forEach { device ->
        device.out.forEach { inMap.getOrPut(it, ::ArrayList) += device.id }
    }
    var layer = listOf("rx")
    val seen = HashSet<String>()
    while (layer.isNotEmpty()) {
        val rawIn = layer.flatMap { inMap[it].orEmpty() }.toSet()
        val (oldIn, newIn) = rawIn.partition { it in seen }
        println(newIn.joinToString(" ") + " [" + oldIn.joinToString(" ") + "]")
        layer = newIn
        seen += newIn
    }

    var count = 0L
    while (true) {
        count++
        var current = listOf(Triple("button", "broadcaster", Pulse.Low))
        while (current.isNotEmpty()) {
            current = current.flatMap { (from, to, pulse) ->
//                if (to == "kc") println("$from -> kc $pulse @ $count ${devices.getValue("kc")}")

                if (pulse == Pulse.Low && to == "rx") return@part2 count
                val device = devices.getValue(to)
                val newPulse = device.accept(pulse, from, count)
                if (newPulse != null) {
                    device.out.map { newTo -> Triple(to, newTo, newPulse).also {
//                        if (to == "kc" && newPulse == Pulse.High) println("$from -> kc at $count")
//                        if (to == "kc" && from == "vn") println("$from -> $newPulse -> kc at $count")
//                        if (to == "kc" && from == "hn") println("$from -> $newPulse -> kc at $count")
//                        if (to == "kc" && from == "kt") println("$from -> $newPulse -> kc at $count")
//                        if (to == "kc" && from == "ph") println("$from -> $newPulse -> kc at $count")
                    } }
                } else emptyList()
            }
        }
//        if (count == 1000L) return 0
//        if (count % 10_000L == 0L) println(count)
    }
//    return 0
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

private fun Device.accept(pulse: Pulse, from: String, count: Long): Pulse? = when (this) {
    is Broadcaster -> pulse
    is FlipFlop -> if (pulse == Pulse.High) null else {
        state = !state
        if (state) Pulse.High else Pulse.Low
    }
    is Conjunction -> {
//        if (id == "kc" && input[from] != pulse) println("$from sent $pulse to $this @ $count")
        if (id == "kc" && from == "ph" && pulse == Pulse.High) println("$from sent $pulse to $id @ $count")
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

