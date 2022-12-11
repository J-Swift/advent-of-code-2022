import java.io.File
import kotlin.math.abs

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

fun hasSprite(spritePos: Int, targetPos: Int): Boolean {
    return abs((spritePos + 1) - targetPos) <= 1
}

fun main() {
    val input = parseInput(loadInput())

    val instructions = input.map { parseInstruction(it) }

    var pc = 0
    var spriteX = 1
    val monitorWidth = 40
    val monitorHeight = 6

    val output = mutableListOf<String>()
    val curScanLine = StringBuilder("")

    instructions.forEach { instruction ->
        if (pc > monitorWidth * monitorHeight) {
            return@forEach
        }

        var cyclesNeeded = instruction.cyclesNeeded
        while (cyclesNeeded > 0) {
            val curX = 1 + (pc % monitorWidth)
            cyclesNeeded--
            pc += 1
            if (hasSprite(spriteX, curX)) {
//                println("[$pc] [${spriteX}] [$curX] yes")
                curScanLine.append('#')
            } else {
//                println("[$pc] [${spriteX}] [$curX] no")
                curScanLine.append(' ')
            }
            if (curX == monitorWidth) {
//                println("Cycle [$pc] val [$curX] [${pc * curX}]")
                output.add(curScanLine.toString())
                curScanLine.clear()
            }
        }
        spriteX = instruction.execute(spriteX)
    }

    println(output.joinToString("\n"))
}

main()

