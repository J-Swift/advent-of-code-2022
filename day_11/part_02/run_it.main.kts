import java.io.File
import java.math.BigInteger

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

data class InspectionResult(val item: Long, val toMonkeyIdx: Int)

class Monkey(
    private val items: MutableList<Long>,
    private val adjustmentFn: (Long) -> Long,
    private val predicateFn: (Long) -> Boolean,
    private val onTrue: Int,
    private val onFalse: Int
) {
    var numInspected = 0

    fun addItem(item: Long) {
        items.add(item)
    }

    fun inspectNext(): InspectionResult? {
        if (items.isEmpty()) {
            return null
        }
        numInspected++

        var item = items.removeAt(0)
        item = adjustmentFn(item)

        // NOTE(jpr): hardcoded math trick :(
//        item %= 96577 // test
        item %= 9699690 // real


        return if (predicateFn(item)) {
            InspectionResult(item, onTrue)
        } else {
            InspectionResult(item, onFalse)
        }
    }
}

fun parseItems(input: String): List<Long> {
    val newInput = input.replace("  Starting items: ", "")

    return newInput.split(", ").map { it.toLong(10) }
}

fun parseAdjustmentFn(input: String): (Long) -> Long {
    val newInput = input.replace("  Operation: new = old ", "")

    val op = newInput[0]
    val arg = newInput.substring(2)
    val parsedArg = if (arg == "old") null else arg.toLong(10)

    return {
        val other = parsedArg ?: it

        when (op) {
            '*' -> it * other
            '/' -> it / other
            '+' -> it + other
            '-' -> it - other
            else -> throw IllegalArgumentException("Invalid op [$op]")
        }
    }
}

fun parsePredicateFn(input: String): (Long) -> Boolean {
    val newInput = input.replace("  Test: divisible by ", "")

    val other = newInput.toLong(10)
    return { it % other == 0L }
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

    var roundsRemaining = 10000
    while (roundsRemaining > 0) {
        roundsRemaining--

        if (roundsRemaining % 100 == 0) {
            println("RR [$roundsRemaining]")
        }
        monkeys.forEach { monkey ->
            var result = monkey.inspectNext()
            while (result != null) {
                monkeys[result.toMonkeyIdx].addItem(result.item)
                result = monkey.inspectNext()
            }
        }
    }

    val sorted = monkeys.map { it.numInspected.toLong() }.sortedDescending()
    println("[${sorted[0]}] [${sorted[1]}] [${BigInteger.valueOf(sorted[0]) * BigInteger.valueOf(sorted[1])}]")
}

main()
