import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

fun scoreFor(c: Char): Int {
    return when (c) {
        in 'a'..'z' -> c - 'a' + 1
        in 'A'..'Z' -> c - 'A' + 27
        else -> throw IllegalArgumentException("Invalid value [$c]")
    }
}

fun main() {
    val input = parseInput(loadInput())

    val commonElements = input.windowed(3, 3).map {
        val sets = it.map { str ->
            str.fold(mutableSetOf<Char>()) { memo, c ->
                memo.add(c)
                memo
            }
        }
        sets.reduce { memo, set ->
            memo.intersect(set).toMutableSet()
        }.first()
    }

    println(commonElements.sumOf { scoreFor(it) })
}

main()

