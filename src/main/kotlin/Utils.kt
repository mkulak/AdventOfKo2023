fun readInput(name: String): List<String> = readInputAsText(name).lines()

fun readInputAsText(name: String): String = object {}::class.java.getResource("$name.txt")!!.readText()