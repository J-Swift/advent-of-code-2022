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
        it.windowed(4, 1).forEach { option ->
            if (option.toSet().size == 4) {
                println("$it: $option")
                return@Next it.indexOf(option) + 4
            }
        }
    })
}

main()

