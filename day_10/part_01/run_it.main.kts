import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

sealed class Instruction(val cyclesNeeded: Int, val execute: (Int) -> Int) {
    object Noop : Instruction(1, { it })
    data class Add(val amount: Int) : Instruction(2, { it + amount })
}

fun parseInstruction(input: String): Instruction {
    return when {
        input == "noop" -> Instruction.Noop
        input.startsWith("addx") -> {
            val parts = input.split(" ")
            return Instruction.Add(parts[1].toInt(10))
        }
        else -> throw IllegalArgumentException("Invalid value [$input]")
    }
}

fun main() {
    val input = parseInput(loadInput())

    val instructions = input.map { parseInstruction(it) }

    val importantCycles = setOf(20, 60, 100, 140, 180, 220)
    val importantCyclesSeen = mutableSetOf<Int>()
    var sumOfImportantCyclesSeen = 0

    var pc = 0
    var curX = 1

    instructions.forEach { instruction ->
        if (importantCyclesSeen == importantCycles) {
            return@forEach
        }

        var cyclesNeeded = instruction.cyclesNeeded
        while (cyclesNeeded > 0) {
            cyclesNeeded--
            pc += 1
            if (importantCycles.contains(pc)) {
//                println("Cycle [$pc] val [$curX] [${pc * curX}]")
                importantCyclesSeen.add(pc)
                sumOfImportantCyclesSeen += pc * curX
            }
        }
        curX = instruction.execute(curX)
    }

    println("Val [$pc] [$curX] [$sumOfImportantCyclesSeen]")
}

main()

