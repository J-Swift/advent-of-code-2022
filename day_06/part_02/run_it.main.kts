import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

fun main() {
    val input = parseInput(loadInput())

    println(input.map Next@ {
        it.windowed(14, 1).forEach { option ->
            if (option.toSet().size == 14) {
                println("$it: $option")
                return@Next it.indexOf(option) + 14
            }
        }
    })
}

main()

