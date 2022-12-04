import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

fun isPartiallyContained(r1: String, r2: String): Boolean {
    val pair1 = Pair(r1.split("-")[0].toInt(), r1.split("-")[1].toInt())
    val pair2 = Pair(r2.split("-")[0].toInt(), r2.split("-")[1].toInt())

    return (pair1.first <= pair2.second && pair1.second >= pair2.first)
}

fun main() {
    val input = parseInput(loadInput())

    val pairs = input.map { it.split((",")) }

    println(pairs.sumOf { if (isPartiallyContained(it[0], it[1])) 1L else 0L })
}

main()

