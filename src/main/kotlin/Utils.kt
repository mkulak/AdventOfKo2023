fun readInput(name: String): List<String> = readInputAsText(name).lines()

fun readInputAsText(name: String): String = object {}::class.java.getResource("$name.txt")!!.readText()

enum class Dir(val dx: Int, val dy: Int) {
    Up(0, -1), Left(-1, 0), Down(0, 1), Right(1, 0);

    val next: Dir get() = entries[(ordinal + 1) % entries.size]
    val prev: Dir get() = entries[(ordinal - 1 + entries.size) % entries.size]
    val xy: XY get() = XY(dx, dy)

    companion object {
        fun find(dx: Int, dy: Int): Dir = Dir.entries.first { it.dx == dx && it.dy == dy }
    }
}

data class XY(val x: Int, val y: Int) {
    operator fun plus(xy: XY) = XY(x + xy.x, y + xy.y)
    override fun toString(): String = "[$x, $y]"
}

const val ANSI_RESET = "\u001B[0m"
const val ANSI_BOLD = "\u001B[1m"

const val ANSI_BLACK = "\u001B[30m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_PURPLE = "\u001B[35m"
const val ANSI_CYAN = "\u001B[36m"
const val ANSI_WHITE = "\u001B[37m"

const val ANSI_BLACK_BACKGROUND = "\u001B[40m"
const val ANSI_RED_BACKGROUND = "\u001B[41m"
const val ANSI_GREEN_BACKGROUND = "\u001B[42m"
const val ANSI_YELLOW_BACKGROUND = "\u001B[43m"
const val ANSI_BLUE_BACKGROUND = "\u001B[44m"
const val ANSI_PURPLE_BACKGROUND = "\u001B[45m"
const val ANSI_CYAN_BACKGROUND = "\u001B[46m"
const val ANSI_WHITE_BACKGROUND = "\u001B[47m"
