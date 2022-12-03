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

    val halves = input.map {
        val mid = it.length / 2
        listOf(it.substring(0, mid), it.substring(mid))
    }

    val commonElements = halves.map {
        val lefts = it[0].fold(mutableSetOf<Char>()) { memo, c ->
            memo.add(c)
            memo
        }
        val rights = it[1].fold(mutableSetOf<Char>()) { memo, c ->
            memo.add(c)
            memo
        }

        lefts.intersect(rights).first()
    }

    println(commonElements.map { scoreFor(it) }.sum())
}

main()

