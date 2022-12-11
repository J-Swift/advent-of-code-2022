import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

data class InspectionResult(val item: Int, val toMonkeyIdx: Int)

class Monkey(
    private val items: MutableList<Int>,
    private val adjustmentFn: (Int) -> Int,
    private val predicateFn: (Int) -> Boolean,
    private val onTrue: Int,
    private val onFalse: Int
) {
    var numInspected = 0

    fun addItem(item: Int) {
        items.add(item)
    }

    fun inspectNext(): InspectionResult? {
        if (items.isEmpty()) {
            return null
        }
        numInspected++

        var item = items.removeAt(0)
        item = adjustmentFn(item)
        item /= 3

        return if (predicateFn(item)) {
            InspectionResult(item, onTrue)
        } else {
            InspectionResult(item, onFalse)
        }
    }
}

fun parseItems(input: String): List<Int> {
    val newInput = input.replace("  Starting items: ", "")

    return newInput.split(", ").map { it.toInt(10) }
}

fun parseAdjustmentFn(input: String): (Int) -> Int {
    val newInput = input.replace("  Operation: new = old ", "")

    val op = newInput[0]
    val arg = newInput.substring(2)

    return {
        val other = if (arg == "old") it else arg.toInt(10)

        when (op) {
            '*' -> it * other
            '/' -> it / other
            '+' -> it + other
            '-' -> it - other
            else -> throw IllegalArgumentException("Invalid op [$op]")
        }
    }
}

fun parsePredicateFn(input: String): (Int) -> Boolean {
    val newInput = input.replace("  Test: divisible by ", "")

    val other = newInput.toInt(10)
    return { it % other == 0 }
}

fun parseOnTrue(input: String): Int {
    val newInput = input.replace("    If true: throw to monkey ", "")

    return newInput.toInt(10)
}

fun parseOnFalse(input: String): Int {
    val newInput = input.replace("    If false: throw to monkey ", "")

    return newInput.toInt(10)
}

fun parseMonkeys(input: List<String>): List<Monkey> {
    val result = mutableListOf<Monkey>()

    input.windowed(7, 7, true) { monkeyChunk ->
        result.add(
            Monkey(
                parseItems(monkeyChunk[1]).toMutableList(),
                parseAdjustmentFn(monkeyChunk[2]),
                parsePredicateFn(monkeyChunk[3]),
                parseOnTrue(monkeyChunk[4]),
                parseOnFalse(monkeyChunk[5])
            )
        )
    }

    return result
}

fun main() {
    val input = parseInput(loadInput())

    val monkeys = parseMonkeys(input)

    var roundsRemaining = 20
    while (roundsRemaining > 0) {
        roundsRemaining--
        monkeys.forEach { monkey ->
            var result = monkey.inspectNext()
            while (result != null) {
                monkeys[result.toMonkeyIdx].addItem(result.item)
                result = monkey.inspectNext()
            }
        }
    }

    val sorted = monkeys.map { it.numInspected }.sortedDescending()
    println("[${sorted[0]}] [${sorted[1]}] [${sorted[0] * sorted[1]}]")
}

main()
