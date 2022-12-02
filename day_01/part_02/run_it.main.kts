import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

fun main() {
    val input = parseInput(loadInput())

    val converted = input.map { if (it.isBlank()) null else it.toInt(10) }

    val results = mutableListOf<Int>()
    var currentSum = 0
    converted.forEach {
        when(it) {
            null -> {
                results.add(currentSum)
                currentSum = 0
            }
            else -> currentSum += it
        }
    }
    if (converted.last() != null) {
        results.add(currentSum)
    }

    val top3 = results.sortedDescending().subList(0, 3)
    println(top3.sum())
}

main()

