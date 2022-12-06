import java.io.File

fun loadInput(): String {
    return File("./input.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

fun getInitialStack(stackIdx: Int, stackInput: List<String>): MutableList<String> {
    val res = mutableListOf<String>()

    stackInput.forEach {
        val charIdx = 1 + 4 * stackIdx
        if (charIdx <= it.length && it[charIdx].toString() != " ") {
            res.add(0, it[charIdx].toString())
        }
    }

    return res
}

data class MoveCommand(val num: Int, val from: Int, val to: Int)

val commandRegex = Regex("^move (\\d+) from (\\d+) to (\\d+)$")
fun parseCommand(input: String): MoveCommand {
    val match = commandRegex.find(input)!!.groups
    return MoveCommand(
        match[1]!!.value.toInt(),
        match[2]!!.value.toInt(),
        match[3]!!.value.toInt()
    )
}

fun main() {
    val numStacks = 9
    val initStackHeight = 8
    val input = parseInput(loadInput())

    val stackInput = input.subList(0, initStackHeight)
    val movesInput = input.subList(initStackHeight + 2, input.size)

    val stacks = (0 until numStacks).map { idx ->
        getInitialStack(idx, stackInput)
    }

    val moves = movesInput.map { parseCommand(it) }

    moves.forEach { move ->
        val from = stacks[move.from - 1]
        val to = stacks[move.to - 1]
        val removed = from.subList(from.size - move.num, from.size)
        to.addAll(removed)
        repeat(move.num) { from.removeLast() }
//        val removed = stacks[move.from-1].subList(move.from)
//        repeat(move.num) {
//            stacks[move.from-1].sub
//            stacks[move.to - 1].add(stacks[move.from - 1].removeLast())
//        }
    }

    println(stacks.map { it.last() }.joinToString(""))
}

main()

